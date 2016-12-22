package ru.sibint.olymp.checker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class ResultChecker {

    private String checkerCode;
    private String checkerLanguage;
    private String javaClassName;

    public ResultChecker(String _checkerCode, String _checkerLanguage) {
        checkerCode = _checkerCode;
        checkerLanguage = _checkerLanguage;
        if(checkerLanguage.equals("JAVA")) {
            File f = null;
            try {
                f = File.createTempFile("temp/" + String.valueOf(System.nanoTime()) + "/Checker", ".java");
                PrintWriter pw = new PrintWriter(f);
                pw.print(checkerCode);
                pw.flush();
                pw.close();

                ProcessBuilder pb = new ProcessBuilder("javac", f.getAbsolutePath());
                try {
                    Process currentProcess = pb.start();
                    currentProcess.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                javaClassName = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf('.'));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            checkerCode = null;
        }
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
            ProcessBuilder pb = new ProcessBuilder("java", "-classpath", javaClassName);
            try {
                File f = File.createTempFile("temp" + String.valueOf(System.nanoTime()), ".in");
                PrintWriter pw = new PrintWriter(f);
                pw.print(checkerCode);
                pw.flush();
                pw.close();

                pb.redirectInput(f);
                Process currentProcess = pb.start();
                currentProcess.waitFor();
                InputStream is = currentProcess.getInputStream();

                Scanner S = new Scanner(is);
                return S.next().equals("OK");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
