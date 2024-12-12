package traffic;

import java.util.ArrayList;
import java.util.List;

public class ViewModel {
    private final Model model = new Model();
    private View view;
    private Thread timer = null;
    private CircularQueue<String> circularQueue;

    public void registerView(View view) {
        this.view = view;
    }

    public void sendEvent(String event) {
        switch (model.state) {
            case NOT_STARTED:
                changeState(State.INPUT_ROAD);
                break;
            case INPUT_ROAD:
                handleInputRoad(event);
                break;
            case INPUT_INTERVAL:
                handleInputInterval(event);
                break;
            case MAIN_MENU:
                handleMainMenu(event);
                break;
            case ADD_ROAD:
                handleAddRoad(event);
                break;
            case SHOW_ADDED_ROAD, SHOW_ADD_ROAD_ERROR, SHOW_DELETED_ROAD, SHOW_DELETE_ROAD_ERROR:
                changeState(State.MAIN_MENU);
                break;
            case DELETE_ROAD:
                handleDeleteRoad();
                break;
            case SYSTEM:
                model.state = State.MAIN_MENU;
                // TODO wait for timer signal
                view.draw(model);
                break;
            case EXIT:
                break;
            default:
                break;
        }
    }

    private void handleInputRoad(String event) {
        model.roadsNumber = Integer.parseInt(event);
        circularQueue = new CircularQueue<>(model.roadsNumber);

        StreetDB db = StreetDB.getINSTANCE();
        List<String> roads = db.selectStreet().stream().map(dao -> dao.name).toList();
        roads.forEach(road -> circularQueue.add(road));
        changeState(State.INPUT_INTERVAL);
    }

    private void handleAddRoad(String event) {
        try {
            StreetDB db = StreetDB.getINSTANCE();
            db.insertStreet(event);

            circularQueue.add(event);
            model.addedRoad = event;
            changeState(State.SHOW_ADDED_ROAD);
        } catch (IllegalStateException e) {
            changeState(State.SHOW_ADD_ROAD_ERROR);
        }
    }

    private void handleShowAddRoad() {
        changeState(State.MAIN_MENU);
    }

    private void handleDeleteRoad() {
        try {
            StreetDB db = StreetDB.getINSTANCE();
            model.deletedRoad = circularQueue.remove();
            db.deleteStreet(model.deletedRoad);
            model.openRoadIndex--;
            changeState(State.SHOW_DELETED_ROAD);
        } catch (IllegalStateException e) {
            changeState(State.SHOW_DELETE_ROAD_ERROR);
        }
    }

    private void handleInputInterval(String event) {
        if (timer == null) {
            timer = new Timer(model, view, () -> circularQueue.size());
            timer.setName("QueueThread");
            timer.start();
        }
        model.intervalNumber = Integer.parseInt(event);
        changeState(State.MAIN_MENU);
    }

    private void handleMainMenu(String event) {
        int command = Integer.parseInt(event);
        switch (command) {
            case 1:
                changeState(State.ADD_ROAD);
                break;
            case 2:
                changeState(State.DELETE_ROAD);
                break;
            case 3:
                ArrayList<String> list = new ArrayList<>();
                for (String road : circularQueue) {
                    list.add(road);
                }
                model.roads = list;
                changeState(State.SYSTEM);
                break;
            case 0:
                timer.interrupt();
                try {
                    timer.join();
                } catch (Exception ignore) {}

                changeState(State.EXIT);
                break;
            default:
                break;
        }
    }

    private void changeState(State newState) {
        model.state = newState;
        view.draw(model);
    }

    public void start() {
        changeState(State.NOT_STARTED);
        sendEvent(null);
    }

}
