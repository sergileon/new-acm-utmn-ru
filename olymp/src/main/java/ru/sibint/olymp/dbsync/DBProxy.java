package ru.sibint.olymp.dbsync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSetMetaData;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBProxy {
	
	public static List<Object> evaluateQuery(String query, QueryType qt) {
		ArrayList<Object> ans = new ArrayList<Object>();
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			if(qt == QueryType.SELECT) {
				Statement stm = connectionMysql.createStatement();
				ResultSet rs = stm.executeQuery(query);
				ResultSetMetaData rsmd = rs.getMetaData();
				while(rs.next()) {
					HashMap<String, String> element = new HashMap<String, String>();
					for(int i = 1; i <= rsmd.getColumnCount(); i++) {
						element.put(rsmd.getColumnName(i), rs.getString(i));
					}
					ans.add(element);
				}
			}
			if(qt == QueryType.INSERT) {
				Statement stm = connectionMysql.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				stm.executeUpdate(query);
				ResultSet rs = stm.getGeneratedKeys();
			    if(rs.next()) {
			    	ans.add(rs.getInt(1));
			    }
			}
			if(qt == QueryType.UPDATE) {
				Statement stm = connectionMysql.createStatement();
				stm.execute(query);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return ans;
	}
	
	@SuppressWarnings("unchecked")
	public static String getSubmissions(int count) {
		List<Object> list = evaluateQuery(
				"SELECT Submission.Id as Id, UserApp.Name as Name, UserId, TaskId, Verdict, TestId, TimeSpent, MemorySpent, Lang, Commentary "
				+ "FROM Submission LEFT JOIN UserApp ON Submission.UserId = UserApp.Id ORDER BY Submission.Id DESC LIMIT 0," + String.valueOf(count)
				, QueryType.SELECT);
		if(list == null) return "[{\"status\":\"error\"}]";
		String result = "[";
		boolean bl = false;
		for(Object element : list) {
			String comment = "";
			HashMap<String, String> hm = (HashMap<String, String>)element;
			if(hm.get("Commentary") != null && !hm.get("Commentary").equals("")) {
				comment = "Compilation Error";
			}
			if(bl) result += ",";
			result += 
					"{\"id\":\"" + hm.get("Id") + "\"," +  
					"\"auth\":\"" + hm.get("Name") + "\"," +
					"\"authid\":\"" + hm.get("UserId") + "\"," +
					"\"task\":\"" + hm.get("TaskId") + "\"," +
					"\"verd\":\"" + hm.get("Verdict") + "\"," +
					"\"testid\":\"" + hm.get("TestId") + "\"," +
					"\"time\":\"" + hm.get("TimeSpent") + "\"," +
					"\"comment\":\"" + comment + "\"," +
					"\"lang\":\"" + hm.get("Lang") + "\"," +
					"\"mem\":\"" + hm.get("MemorySpent") + "\"}";
			bl = true;
		}
		result += "]";
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static String getTasks() {
		List<Object> list = evaluateQuery("SELECT Id, Name, Description as 'Desc' FROM Task WHERE IsAvailable = 1", QueryType.SELECT);
		if(list == null) return "[{\"status\":\"error\"}]";
		JSONArray jsonArray = new JSONArray();
		for(Object element : list) {
			HashMap<String, String> hm = (HashMap<String, String>)element;
			jsonArray.put(hm);
		}
		return jsonArray.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static String getLabTasks(Integer lid) {
		List<Object> list = evaluateQuery("SELECT Id, Name, Description as 'Desc' FROM Task WHERE LabNum = " + lid.toString(), QueryType.SELECT);
		if(list == null) return "[{\"status\":\"error\"}]";
		JSONArray jsonArray = new JSONArray();
		for(Object element : list) {
			HashMap<String, String> hm = (HashMap<String, String>)element;
			jsonArray.put(hm);
		}
		return jsonArray.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static String getDescription(Integer id) {
		List<Object> list = evaluateQuery("SELECT Id, Name, Description, LabNum, LabTaskNum FROM Task WHERE Id = " + id.toString(), QueryType.SELECT);
		if(list == null) return "[{\"status\":\"error\"}]";
		for(Object element : list) {
			HashMap<String, String> hm = (HashMap<String, String>)element;
			return (new JSONObject(hm)).toString();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static String getStats() {
		List<Object> list = evaluateQuery("SELECT UserApp.Name as UserName, COUNT(DISTINCT Submission.TaskId) AS Rating FROM UserApp LEFT JOIN Submission ON UserApp.Id = Submission.UserId GROUP BY UserApp.Name ORDER BY Rating DESC", QueryType.SELECT);
		if(list == null) return "[{\"status\":\"error\"}]";
		JSONArray jsonArray = new JSONArray();
		for(Object element : list) {
			HashMap<String, String> hm = (HashMap<String, String>)element;
			jsonArray.put(hm);
		}
		return jsonArray.toString();
	}
	
	public static int addUser(String userName, String userMail, String contestToken) {
		List<Object> list = evaluateQuery("SELECT Id FROM UserApp WHERE email = '" + userMail + "'", QueryType.SELECT);
		if(list == null) return -1;
		if(list.size() > 0) return -2;
		evaluateQuery("INSERT INTO UserApp (Name, Email, Token) VALUES ('" + userName + "', '" + userMail + "', '" + contestToken + "')", QueryType.INSERT);
		return 1;
	}
	
	public static Integer addTask(String title, String description) {
		description = description.replace('\'', ' ').replace('\"', ' ').replace('\\', ' ');
		title = title.replace('\'', ' ').replace('\"', ' ').replace('\\', ' ');
		List<Object> list = evaluateQuery("INSERT INTO Task (Name, Description) VALUES ('" + title + "', '" + description + "')", QueryType.INSERT);
		if(list == null) return -1;
		return ((Integer)list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	public static int addSubmission(String userEmail, String contestToken, String taskId, String verdict, String timeForTask, String memoryForTask, String testId, String lang, String comment) {
		List<Object> list = evaluateQuery("SELECT Id FROM UserApp WHERE email = '" + userEmail + "' AND token = '" + contestToken + "'", QueryType.SELECT);
		if(list == null) return -2;
		String userId = ((HashMap<String, String>)list.get(0)).get("Id");
		list = evaluateQuery("INSERT INTO Submission (TaskId, Verdict, TimeSpent, MemorySpent, UserId, TestId, Lang, Commentary) VALUES ('" + taskId + "', '" + verdict+ "', '" + timeForTask + "', '" + memoryForTask + "', '" + userId + "', '" + testId + "', '" + lang + "', '" + comment + "')", QueryType.INSERT);
		if(list == null) return -1;
		return ((Integer)list.get(0));
	}

	public static void updateSubmission(String id, String verdict, String testId, String timeForTask, String memoryForTask, String comment) {
		String qury = "UPDATE Submission SET "
				+ "Verdict = '" + verdict + "', "
				+ "TimeSpent = " + timeForTask + ", "
				+ "MemorySpent = " + memoryForTask + ", "
				+ "Commentary = '" + comment + "', "
				+ "TestId = " + testId + " WHERE Id = " + id;
		evaluateQuery(qury, QueryType.UPDATE);
	}

	public static Integer updateTask(Integer Id, String title, String description) {
		description = description.replace('\'', ' ').replace('\"', ' ').replace('\\', ' ');
		title = title.replace('\'', ' ').replace('\"', ' ').replace('\\', ' ');
		List<Object> list = evaluateQuery("UPDATE Task SET Name = '" + title + "', Description = '" + description + "' WHERE Id = " + Id.toString(), QueryType.UPDATE);
		if(list == null) return -1;
		return 1;
	}
	
}
