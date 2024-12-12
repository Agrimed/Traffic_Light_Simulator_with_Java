package traffic;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class View {

    private final Scanner scanner  = new Scanner(System.in);
    private ViewModel viewModel = null;


    public View(ViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.registerView(this);
    }

    public void draw(Model data) {
        String inputLine;
        switch (data.state) {
            case NOT_STARTED:
                //handle not started
                handleStartApp();
                break;
            case INPUT_ROAD:
                inputLine = handelInputRoad();
                viewModel.sendEvent(inputLine);
                break;
            case INPUT_INTERVAL:
                inputLine = handleInputInterval();
                viewModel.sendEvent(inputLine);
                break;
            case MAIN_MENU:
                inputLine = handleMainMenu();
                // send event to controller
                viewModel.sendEvent(inputLine);
                break;
            case ADD_ROAD:
                inputLine = handleAddRoad();
                viewModel.sendEvent(inputLine);
                break;
            case SHOW_ADDED_ROAD:
                System.out.println(data.addedRoad + " added!");
                waitingForInput();
                viewModel.sendEvent(null);
                break;
            case SHOW_ADD_ROAD_ERROR:
                System.out.println("queue is full\n");
                waitingForInput();
                viewModel.sendEvent(null);
                break;
            case SHOW_DELETE_ROAD_ERROR:
                System.out.println("queue is empty\n");
                waitingForInput();
                viewModel.sendEvent(null);
                break;
            case DELETE_ROAD:
                //handleDeleteRoad();
                viewModel.sendEvent(null);
                break;
            case SHOW_DELETED_ROAD:
                System.out.println(data.deletedRoad + " deleted!");
                waitingForInput();
                viewModel.sendEvent(null);
                break;
            case SYSTEM:
                //drawSystemInfo(data.roadsNumber, data.intervalNumber, data.seconds);
                handleSystem();
                viewModel.sendEvent(null);
                break;
            case EXIT:
                handleExit();
                break;
            default:
                break;
        }
    }

    private void handleStartApp() {
        System.out.println("Welcome to the traffic management system!");
    }

    private String handelInputRoad() {
        System.out.println("Input the number of roads: ");
        StreetDB db = StreetDB.getINSTANCE();
        int minRoads = db.countRoad();
        while (true) {
            String inputLine = waitingForInput();
            if (!validateIntegerRange(inputLine, minRoads, Integer.MAX_VALUE)) {
                System.out.println("Incorrect input. Please try again");
            } else {
                return inputLine;
            }
        }
    }

    private String handleInputInterval() {
        System.out.println("Input the interval: ");
        while (true) {
            String inputLine = waitingForInput();
            if (!validateIntegerRange(inputLine, 1, Integer.MAX_VALUE)) {
                System.out.println("Incorrect input. Please try again");
            } else {
                return inputLine;
            }
        }
    }

    private String handleAddRoad() {
        String inputLine;
        System.out.println("Input road name:");
        inputLine = waitingForInput();
        return inputLine;
    }

//    private void handleDeleteRoad() {
//
//    }

    private void handleExit() {
        System.out.println("Bye!");
        //3System.exit(0);
    }

    private String handleMainMenu() {
        String inputLine;
        while (true) {
            drawMainMenu();
            inputLine = waitingForInput();
            if (!validateIntegerRange(inputLine, 0, 3)) {
                System.out.println("Incorrect option. Please try again");
                waitingForInput();
            } else {
                break;
            }
        }
        return inputLine;

    }

    private void handleSystem() {
        waitingForInput();
    }

    private void cleanConsole() {
        try {
            var clearCommand = System.getProperty("os.name").contains("Windows")
                    ? new ProcessBuilder("cmd", "/c", "cls")
                    : new ProcessBuilder("clear");
            clearCommand.inheritIO().start().waitFor();
        }
        catch (IOException | InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Clean screen exception");
        }
    }

    public void drawSystemInfo(Model model) {
        cleanConsole();
        System.out.println(model.seconds + "s. have passed since system startup");
        System.out.println("Number of roads:" + model.roadsNumber);
        System.out.println("Interval:" + model.intervalNumber);
        System.out.println();
        drawRoads(model.roads, model.intervalNumber, model.openRoadIndex, model.elapsedTime);
        System.out.println("Press \"Enter\" to open menu");
    }

    private void drawRoads(List<String> roads, int interval, int openRoadIndex, int elapsedTime) {

        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RESET = "\u001B[0m";

        int cntDown = interval - elapsedTime;

        for (int i = 0; i < roads.size(); i++) {
            int ans = cntDown + interval * multCoef(openRoadIndex, i, roads.size());
            if (openRoadIndex == -1) {
                System.out.println(roads.get(i) + " will be " + ANSI_RED + "closed for " + ans + "s." + ANSI_RESET);
            } else if (openRoadIndex == i) {
                System.out.println(roads.get(i) + " will be "  + ANSI_GREEN + "open for " + cntDown + "s." + ANSI_RESET);
            } else {
                System.out.println(roads.get(i) + " will be " + ANSI_RED + "closed for " + ans + "s." + ANSI_RESET);
            }
        }
    }

    private int multCoef(int openRoadIndex, int current, int size) {
        int coef = current  - openRoadIndex - 1;
        if (coef < 0) {
            coef += size;
        }
        return coef ;
    }

    private void drawMainMenu() {
        System.out.println("Menu:");
        System.out.println("1. Add");
        System.out.println("2. Delete");
        System.out.println("3. System");
        System.out.println("0. Quit");
    }

    private String waitingForInput() {
        return scanner.nextLine();
    }

    private boolean validateIntegerRange(String str, int start, int end) {
        try {
            int number = Integer.parseInt(str);
            if (number < start || number > end ) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}