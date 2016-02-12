package ru.sibint.olymp.api;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Compiler {

	static Logger logger = Logger.getGlobal();
	
	private static void executeCommand(String command) {
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Can not execute command");
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	
	public static void compileJava(String path, String fileName) {
		executeCommand("javac " + path + fileName);
		executeCommand("java " + path + fileName);
	}
	
	public static void compileCPlusPlus(String path, String fileName) {
		executeCommand("C:\\Program Files (x86)\\Microsoft Visual Studio 12.0\\VC\\vcvarsall.bat");
		executeCommand("C:\\Program Files (x86)\\Microsoft Visual Studio 12.0\\VC\\bin\\cl" + path + fileName);
		executeCommand(path + fileName + ".exe");
	}
	
	public static void compileCSharp(String path, String fileName) {
		executeCommand("C:\\Windows\\Microsoft.NET\\Framework\\v4.0.30319\\csc.exe" + path + fileName);
	}
	
}
