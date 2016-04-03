package ru.sibint.olymp.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import org.json.JSONObject;

import ru.sibint.olymp.checker.Checker;
import ru.sibint.olymp.checker.CheckingInfo;
import ru.sibint.olymp.checker.CheckingResult;
import ru.sibint.olymp.dbsync.DBProxy;

@Path("/rest/")
public class APIEndpoint {

	final static String propertiesFile = "config.properties";
	static String tempDir = "C:\\temp\\";
	static String archivePath = "";
	static String unzip = "";
	Properties properties = null; 
	
	static Logger logger = Logger.getGlobal();
	
	{
		properties = new Properties();
		InputStream configStream = getClass().getClassLoader().getResourceAsStream(propertiesFile);
		if(configStream != null) {
			try {
				properties.load(configStream);
				tempDir = properties.getProperty("tempdir");
				archivePath = properties.getProperty("archivedir");
				unzip = properties.getProperty("unzipcommand");
				System.out.println(tempDir);
				System.out.println(archivePath);				
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Can not load properties file");
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}
	
	@Path("/submit/")
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String processSubmission(
			@QueryParam("ext") String ext, 
			@QueryParam("taskId") Integer taskId, 
			@QueryParam("userId") String userId, 
			InputStream data) {
		StringBuilder stringData = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = "";
			while((line = in.readLine()) != null) {
				stringData.append("\n");
				stringData.append(line);
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		}

		int id = DBProxy.addSubmission(userId, taskId.toString(), CheckingResult.WAIT.toString(), "0", "0", "0");
		if(id == -1) {
			return "FAILED";
		}
		String fileName = String.valueOf(id) + "." + ext;
		File F = new File(tempDir + fileName);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(F);
			pw.printf("%s", stringData.toString());
			pw.flush();
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Can not create temp file");
			logger.log(Level.SEVERE, e.getMessage());
		} finally {
			pw.close();
		}
		
		CheckingInfo result = Checker.checkProgram(archivePath, tempDir, fileName, taskId);
		//CheckingInfo result = new CheckingInfo(); result.setVerdict(CheckingResult.AC);
		DBProxy.updateSubmission(String.valueOf(id), result.getCheckingResult().toString(), String.valueOf(result.getTestNumber()), String.valueOf(result.getTime()), String.valueOf(result.getMemory()));
		
		try {
			System.out.println(Paths.get(String.valueOf(id) + ".obj").toAbsolutePath().toString());
			System.out.println(tempDir + String.valueOf(id) + ".exe");
			System.out.println(Paths.get(tempDir + String.valueOf(id) + ".exe").toAbsolutePath().toString());
			Files.deleteIfExists(Paths.get(String.valueOf(id) + ".obj"));
			Files.deleteIfExists(Paths.get(tempDir + String.valueOf(id) + ".exe"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "SUCCESS: " + result.toString();
	}
	
	@Path("/adduser/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addUser(InputStream data) {
		StringBuilder stringData = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = "";
			while((line = in.readLine()) != null)
				stringData.append(line);
			System.out.println(stringData.toString());
			
			JSONObject obj = new JSONObject(stringData.toString());
			DBProxy.addUser(obj.getString("username"), obj.getString("email"));
			
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		}
		return "{\"Status\":\"SUCCESS\"}";
	}
	
	@Path("/addtask/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addTask(InputStream data) {
		StringBuilder stringData = new StringBuilder();
		Integer id = -1;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = "";
			while((line = in.readLine()) != null)
				stringData.append(line);

			System.out.println(stringData.toString());
			JSONObject obj = new JSONObject(stringData.toString());
			id = DBProxy.addTask(obj.getString("title"), obj.getString("description"));
			System.out.println(id);
			if(id == -1) {
				return "{\"Status\":\"FAIL\",\"id\":\"" + id.toString() + "\"}";
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		}
		return "{\"Status\":\"SUCCESS\",\"id\":\"" + id.toString() + "\"}";
	}
	
	
	@Path("/addtasktest/")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String addTaskTest(
			@QueryParam("taskId") Integer taskId, 
			@FormDataParam("tests") InputStream uploadedInputStream,
			@FormDataParam("tests") FormDataContentDisposition fileMetaData) {
		OutputStream out = null;
		String extractCommand = "";
		try {

			String uploadedFileLocation = archivePath + "/" + taskId.toString() + "/tests/";

			if(!Files.exists(Paths.get(archivePath + "/" + taskId.toString()), LinkOption.NOFOLLOW_LINKS)) {
				System.out.println("Dir doesn't exists.");
				(new File(uploadedFileLocation)).mkdirs();
			}
			System.out.println("Saving file as " + uploadedFileLocation + "tests.zip");
			
	        int read = 0;
	        byte[] bytes = new byte[1024];

	        out = new FileOutputStream(new File(uploadedFileLocation + "tests.zip"));
	        while ((read = uploadedInputStream.read(bytes)) != -1) {
	            out.write(bytes, 0, read);
	        }
	        System.out.println("Uploading done.");
	        extractCommand = unzip + " e \"" + uploadedFileLocation + "tests.zip\" -o\"" + uploadedFileLocation + "\"";
	        System.out.println(extractCommand);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		} finally {
	        try {
				out.flush();
		        out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ru.sibint.olymp.compiler.Compiler.executeCommand(extractCommand);
		return "{\"Status\":\"success\"}";
	}
	
	@Path("/serverstatus/")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getServerStatus() {
		return "Server is up and runs application of version " + properties.getProperty("version") + ".\n" + "Operation system is " + System.getProperty("os.name");
	}
	
	@Path("/getsubmission/")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getSubmission(@QueryParam("id") Integer id) {
		String fileName = tempDir + String.valueOf(id) + ".cpp";
		try {
			Scanner S = new Scanner(new File(fileName));
			String result = "";
			while(S.hasNextLine()) {
				result = result + S.nextLine() + "\n";
			}
			return result;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@Path("/submitstatus/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getSubmissions() {
		return DBProxy.getSubmissions(10);
	}
	
}
