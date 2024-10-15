package traffic;

import java.util.Scanner;

public class MVVMTest {
    ViewModel viewModel = new ViewModel();
    View view = new View(viewModel);
    public void start() {
        viewModel.start();
    }
    //set state NOT_STARTED
}

