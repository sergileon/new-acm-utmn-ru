import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MakeInsertQuery {

	private static final String[] source = {
			"C:/Users/pc/YandexDisk/acm/labs/tests/2/"
			//"C:/Users/pc/YandexDisk/acm/labs/tests/1/",
			//"C:/Users/pc/YandexDisk/acm/labs/tests/2/",
			//"C:/Users/pc/YandexDisk/acm/labs/tests/3/"
		};
	
	public static String getFileContents(String absolutePath) {
		StringBuilder contents = new StringBuilder("");
		try {
			Scanner S = new Scanner(new File(absolutePath), "UTF-8");
			if(S.hasNextLine()) {
				while(S.hasNextLine()) {
					String currentLine = S.nextLine();
					currentLine = currentLine.replaceAll("ÿþ", "");
					contents.append(currentLine + "\\n");
				}
				S.close();
			} else {
				S = new Scanner(new File(absolutePath));
				while(S.hasNextLine()) {
					String currentLine = S.nextLine();
					currentLine = currentLine.replaceAll("ÿþ", "");
					String currentString = "";
					for(int i = 0; i < currentLine.length(); i++) {
						if(currentLine.codePointAt(i) == 0) continue;
						currentString += currentLine.charAt(i); 
					}
					if(currentString.length() > 0) contents.append(currentString + "\\n");
				}
				S.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return contents.toString();
	}

	public static void FileToSQL(File dir) {
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if(child.isDirectory()) {
					String name = child.getName();
					String labNum = name.substring(0, name.indexOf("."));
					String labTaskNum = name.substring(name.indexOf(".") + 1);
					File[] testFiles = child.listFiles();
					if(labNum.startsWith("_")) continue;
					for(File test: testFiles) {
						if(test.getName().endsWith(".out")) continue;
						String testName = test.getAbsolutePath().substring(0, test.getAbsolutePath().lastIndexOf("."));
						System.out.println("INSERT INTO Test(TaskId, InputData, OutputData) VALUES ((SELECT Id FROM Task WHERE LabTaskNum = " + labTaskNum + " AND LabNum = " + labNum + "), \"" + getFileContents(testName + ".in") + "\", \"" + getFileContents(testName + ".out") + "\");");
					}
				}
			}
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		for(int i = 0; i < source.length; i++) {
			File dir = new File(source[i]);
			FileToSQL(dir);
		}
	}
}
