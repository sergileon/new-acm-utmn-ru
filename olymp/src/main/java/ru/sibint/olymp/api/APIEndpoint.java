package ru.sibint.olymp.api;

import java.io.BufferedReader;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
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
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	static String tempDir = "";
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
			@QueryParam("token") String contestToken, 
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

		int id = DBProxy.addSubmission(userId, contestToken, taskId.toString(), CheckingResult.WAIT.toString(), "0", "0", "0");
		if(id == -1) {
			return "{\"status\":\"FAILED\",\"message\":\"System ERROR\"}";
		}
		if(id == -2) {
			return "{\"status\":\"FAILED\",\"message\":\"User with such pair email-token is not founded\"}";
		}
		String fileName = String.valueOf(id) + "." + ext;
		File F = new File(tempDir + fileName);
		System.out.println(tempDir + fileName);
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
		
		String path = archivePath + taskId.toString() + "\\tests\\";
		JSONObject jo = new JSONObject(getDescription(taskId));
		if(jo.getString("Name").startsWith("L")) {
			String labId = jo.getString("Name").substring(1).split("_")[0];
			String labTaskId = jo.getString("Name").split("_")[1];
			path = archivePath + "\\..\\labs\\tests\\" + labId + "\\" + labId + "." + labTaskId + "\\";
		}
		CheckingInfo result = Checker.checkProgram(path, tempDir, fileName, taskId);
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
	
	private String genToken() {
		String ret = "";
		Random r = new Random(System.currentTimeMillis());
		for(int i = 0; i < 8; i++) {
			int x = r.nextInt(); if(x < 0) x = -x; x = x % 26;
			char c = (char)(x + 65);
			ret += c;
		}
		return ret;
	}
	
	private void SendEmail(String email, String contestToken) {
		String to = email;
		String from = "i107th@gmail.com";
        String host = "smtp.gmail.com";
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", host);
		props.put("mail.stmp.user" , "i107th@gmail.com");
		props.put("mail.smtp.password", "107475508th");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
	    props.setProperty("mail.smtp.socketFactory.fallback", "false");
	    props.setProperty("mail.smtp.port", "465");
	    props.setProperty("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.debug", "true");
	    props.put("mail.store.protocol", "pop3");
	    props.put("mail.transport.protocol", "smtp");

	    Session session = Session.getDefaultInstance(props, new SmtpAuthenticator());
	
		try {
			MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Registration on acm.utmn.ru");
			message.setText("Hello! Thank you for registration on acm.utmn.ru! Use next token to submit tasks: " + contestToken);
			Transport t = session.getTransport("smtp");
			t.send(message);
			System.out.println("Message was successfully sent.");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
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
			String contestToken = genToken();
			int code = DBProxy.addUser(obj.getString("username"), obj.getString("email"), contestToken);
			System.out.println(code);
			if(code == -1) {
				return "{\"status\":\"FAILED\",\"message\":\"System ERROR\"}";
			}
			if(code == -2) {
				return "{\"status\":\"FAILED\",\"message\":\"User already exists.\"}";
			}
			SendEmail(obj.getString("email"), contestToken);
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		}
		return "{\"Status\":\"SUCCESS\",\"message\":\"Check your email and find token to proceed with online contester.\"}";
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

			String uploadedFileLocation = archivePath + "\\" + taskId.toString() + "\\tests\\";

			if(!Files.exists(Paths.get(archivePath + "\\" + taskId.toString()), LinkOption.NOFOLLOW_LINKS)) {
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

	@Path("/getasktestin/")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getTaskTestIn(
			@QueryParam("taskId") Integer taskId, 
			@QueryParam("testId") Integer testId) {
		
		String testContents = "";
		String path = archivePath + "\\" + taskId + "\\tests\\" + testId + ".in";
		JSONObject jo = new JSONObject(getDescription(taskId));
		if(jo.getString("Name").startsWith("L")) {
			String labId = jo.getString("Name").substring(1).split("_")[0];
			String labTaskId = jo.getString("Name").split("_")[1];
			path = archivePath + "\\..\\labs\\tests\\" + labId + "\\" + labId + "." + labTaskId + "\\" + testId + ".in";
		}
		System.out.println(path);
		File F = new File(path);
		try {
			Scanner S = new Scanner(F);
			while(S.hasNextLine()) {
				testContents += S.nextLine() + "<br>";
			}
			S.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return testContents;
	}
	
	@Path("/getasktestout/")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getTaskTestOut(
			@QueryParam("taskId") Integer taskId, 
			@QueryParam("testId") Integer testId) {
		
		String testContents = "";
		String path = archivePath + "\\" + taskId + "\\tests\\" + testId + ".out";
		JSONObject jo = new JSONObject(getDescription(taskId));
		if(jo.getString("Name").startsWith("L")) {
			String labId = jo.getString("Name").substring(1).split("_")[0];
			String labTaskId = jo.getString("Name").split("_")[1];
			path = archivePath + "\\..\\labs\\tests\\" + labId + "\\" + labId + "." + labTaskId + "\\" + testId + ".out";
		}
		System.out.println(path);
		File F = new File(path);
		try {
			Scanner S = new Scanner(F);
			while(S.hasNextLine()) {
				testContents += S.nextLine() + "<br>";
			}
			S.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return testContents;
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
			S.close();
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
	
	@Path("/gettasks/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getTasks() {
		return DBProxy.getTasks();
	}
	
	@Path("/getlabtasks/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getLabTasks(@QueryParam("id") Integer id) {
		return DBProxy.getLabTasks(id);
	}
	
	@Path("/getstats/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getStats() {
		return DBProxy.getStats();
	}
	
	@Path("/getdescription/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getDescription(@QueryParam("id") Integer id) {
		return DBProxy.getDescription(id);
	}
		
	@Path("/updatetask/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateTask(@QueryParam("id") Integer id, InputStream data) {
		StringBuilder stringData = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			String line = "";
			while((line = in.readLine()) != null)
				stringData.append(line);

			System.out.println(stringData.toString());
			JSONObject obj = new JSONObject(stringData.toString());
			DBProxy.updateTask(id, obj.getString("title"), obj.getString("description"));
			if(id == -1) {
				return "{\"Status\":\"FAIL\",\"id\":\"" + id.toString() + "\"}";
			}
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage());
		}
		return "{\"Status\":\"SUCCESS\",\"id\":\"" + id.toString() + "\"}";
	}
}

class SmtpAuthenticator extends Authenticator {
	public SmtpAuthenticator() {

	    super();
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		String username = "i107th";
		String password = "107475508th";
        return new PasswordAuthentication(username, password);
	}
}