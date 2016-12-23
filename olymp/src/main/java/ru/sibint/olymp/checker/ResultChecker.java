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
        if (checkerLanguage != null) {
            if (checkerLanguage.equals("JAVA")) {
                File f = null;
                try {
                    File f_dir = File.createTempFile("temp" + String.valueOf(System.nanoTime()), "");
                    f_dir.delete();
                    f_dir.mkdirs();
                    f = new File(f_dir.getAbsolutePath() + "/Checker.java");
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

                    javaClassName = f_dir.getAbsolutePath();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                checkerCode = null;
            }
        }
    }

    public boolean check(String inputData, String outputData, String inputTestData) {
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
            try {
                File f_in = File.createTempFile("temp" + String.valueOf(System.nanoTime()), ".in");
                PrintWriter pw = new PrintWriter(f_in);
                pw.print(inputTestData);
                pw.flush();
                pw.close();

                File f_out = File.createTempFile("temp" + String.valueOf(System.nanoTime()), ".out");
                pw = new PrintWriter(f_out);
                pw.print(outputData);
                pw.flush();
                pw.close();

                ProcessBuilder pb = new ProcessBuilder("java", "-classpath", javaClassName, "Checker", f_in.getAbsolutePath(), f_out.getAbsolutePath());
                Process currentProcess = pb.start();
                currentProcess.waitFor();
                InputStream is = currentProcess.getInputStream();

                System.out.println("java -classpath " + javaClassName + " Checker " + f_in.getAbsolutePath() + " " + f_out.getAbsolutePath());

                Scanner S = new Scanner(is);
                StringBuilder sb = new StringBuilder();
                while(S.hasNext()) {
                    sb.append(S.next());
                }
                String answer = sb.toString();
                return answer.equals("OK");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}

