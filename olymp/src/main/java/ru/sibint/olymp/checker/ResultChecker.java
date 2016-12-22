package ru.sibint.olymp.checker;

import java.util.Scanner;

public class ResultChecker {

    private String checkerCode;
    private String checkerLanguage;

    public ResultChecker(String _checkerCode, String _checkerLanguage) {
        checkerCode = _checkerCode;
        checkerLanguage = _checkerLanguage;
    }

    public boolean check(String inputData, String outputData) {
        if(checkerCode == null) { //default checker
            Scanner inputScanner = new Scanner(inputData);
            Scanner outputScanner = new Scanner(outputData);

            while(inputScanner.hasNext() && outputScanner.hasNext()) {
                String inputToken = inputScanner.next();
                String outputToken = outputScanner.next();

                if(!inputToken.equals(outputToken)) return false;
            }
            if(inputScanner.hasNext() || outputScanner.hasNext()) return false;
        } else {

        }
        return true;
    }

}
