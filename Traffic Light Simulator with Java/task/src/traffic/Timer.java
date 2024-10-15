package traffic;

import java.util.List;
import java.util.function.IntSupplier;

public class Timer extends Thread{

    private final Model model;
    private final View view;
    private final IntSupplier getRoadCount;

    public Timer(Model model, View view, IntSupplier getRoadCount) {
        this.model = model;
        this.view = view;
        this.getRoadCount = getRoadCount;
        model.elapsedTime = 0;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (model.state == traffic.State.SYSTEM) {
                view.drawSystemInfo(model);
            } else {
                // TODO send signal to viewModel
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            model.seconds++;
            if (model.seconds == 1) {
                continue;
            }
            increaseElapsedTime();
            if (model.elapsedTime == 0) {
                increaseOpenRoadIndex();
            }
        }
    }

    private void increaseElapsedTime() {
        model.elapsedTime = (model.elapsedTime + 1) % model.intervalNumber;
    }

    private void increaseOpenRoadIndex() {
        model.openRoadIndex = getRoadCount.getAsInt() == 0 ? 0 : (model.openRoadIndex + 1) % getRoadCount.getAsInt();
    }
}
