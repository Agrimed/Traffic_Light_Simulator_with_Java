package traffic;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
  public static void main(String[] args){
    StreetDB streetDB = StreetDB.getINSTANCE();
    MVVMTest mvvmTest = new MVVMTest();
    mvvmTest.start();
    streetDB.disconnect();
  }
}
