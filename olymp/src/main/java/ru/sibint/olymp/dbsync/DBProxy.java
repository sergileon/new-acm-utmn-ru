package ru.sibint.olymp.dbsync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBProxy {
	public static String getSubmissions(int count) {
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		}

		String result = "[";
		try {
			Statement stm = connectionMysql.createStatement();
			stm.setFetchSize(1000);
			String qury = "SELECT Submission.Id, UserApp.Name as Name, TaskId, Verdict, TestId, TimeSpent, MemorySpent FROM Submission LEFT JOIN UserApp ON Submission.UserId = UserApp.Id ORDER BY Submission.Id DESC LIMIT 0," + String.valueOf(count);
			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
		    stm.execute(qury);
			ResultSet rs = stm.getResultSet();
			boolean bl = false;
			while(rs.next()) {
				if(bl) result += ",";
				result += 
						"{\"id\":\"" + rs.getString(1) + "\"," +  
						"\"auth\":\"" + rs.getString(2) + "\"," +
						"\"task\":\"" + rs.getString(3) + "\"," +
						"\"verd\":\"" + rs.getString(4) + "\"," +
						"\"testid\":\"" + rs.getString(5) + "\"," +
						"\"time\":\"" + rs.getString(6) + "\"," +
						"\"mem\":\"" + rs.getString(7) + "\"}";
				bl = true;
			}
			result += "]";
			System.out.println(result);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public static String getTasks() {
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		}

		JSONArray jsonArray = new JSONArray();
		try {
			Statement stm = connectionMysql.createStatement();
			stm.setFetchSize(1000);
			String qury = "SELECT Id, Name, Description FROM Task";
			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
		    stm.execute(qury);
			ResultSet rs = stm.getResultSet();
			while(rs.next()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("Id", rs.getString(1));
				hm.put("Name", rs.getString(2));
				hm.put("Desc", rs.getString(3));
				jsonArray.put(hm);
			}
			return jsonArray.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public static String getDescription(Integer id) {
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		}

		try {
			Statement stm = connectionMysql.createStatement();
			stm.setFetchSize(1000);
			String qury = "SELECT Id, Name, Description FROM Task WHERE Id = " + id.toString();
			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
		    stm.execute(qury);
			ResultSet rs = stm.getResultSet();
			while(rs.next()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("Id", rs.getString(1));
				hm.put("Name", rs.getString(2));
				hm.put("Desc", rs.getString(3));
				return (new JSONObject(hm)).toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
		}
		return "[{\"status\":\"error\"}]";
	}
	
	public static String getStats() {
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		}

		JSONArray jsonArray = new JSONArray();
		try {
			Statement stm = connectionMysql.createStatement();
			stm.setFetchSize(1000);
			String qury = "SELECT UserApp.Name as UserName, COUNT(DISTINCT Submission.TaskId) AS Rating FROM UserApp LEFT JOIN Submission ON UserApp.Id = Submission.UserId GROUP BY UserApp.Name ORDER BY Rating DESC";
			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
		    stm.execute(qury);
			ResultSet rs = stm.getResultSet();
			while(rs.next()) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("Name", rs.getString(1));
				hm.put("Count", rs.getString(2));
				jsonArray.put(hm);
			}
			return jsonArray.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return "[{\"status\":\"error\"}]";
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
		}
	}
	
	@SuppressWarnings("finally")
	public static int addUser(String userName, String userMail, String contestToken) {
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return -1;
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

		int code = -1;
		try {
			Statement stmSel = connectionMysql.createStatement();
			String selectIdQuery = "SELECT Id FROM UserApp WHERE email = '" + userMail + "'";
			System.out.println(selectIdQuery);
			if(stmSel.executeQuery(selectIdQuery).next()) {
				System.out.println("User already exists");
				code = -2;
			}

			System.out.println("Insert new user");
			Statement stm = connectionMysql.createStatement();
			stm.setFetchSize(1000);
			String qury = "INSERT INTO UserApp (Name, Email, Token) VALUES ('" + userName + "', '" + userMail + "', '" + contestToken + "')";
			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
		    stm.execute(qury);
		} catch (SQLException e) {
			e.printStackTrace();
			code = -1;
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
			return code;
		}
	}
	
	@SuppressWarnings("finally")
	public static Integer addTask(String title, String description) {
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return -1;
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

		Integer id = -1;
		try {
			Statement stm = connectionMysql.createStatement();
			stm.setFetchSize(1000);
			String qury = "INSERT INTO Task (Name, Description) VALUES ('" + title + "', '" + description + "')";
			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
			stm = connectionMysql.prepareStatement(qury, Statement.RETURN_GENERATED_KEYS);
			stm.executeUpdate(qury);
			ResultSet rs1 = stm.getGeneratedKeys();
		    if(rs1.next()) {
		    	id = rs1.getInt(1);
		    }
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
			return id;
		}
	}
	
	@SuppressWarnings("finally")
	public static int addSubmission(String userEmail, String contestToken, String taskId, String verdict, String timeForTask, String memoryForTask, String testId) {
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return -1;
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

		int id = -1;
		String userId = "1";
		String selectIdQuery = "SELECT Id FROM UserApp WHERE email = '" + userEmail + "' AND token = '" + contestToken + "'";
		System.out.println(selectIdQuery);
		try {
			Statement stm = connectionMysql.createStatement();
			stm.setFetchSize(1000);
		    
			ResultSet rs1 = stm.executeQuery(selectIdQuery);
			if(!rs1.next()) {
				return -2;
			}
			while(rs1.next()) {
				userId = rs1.getString(1);
				System.out.println(rs1.getString(1));
			}
			String qury = "INSERT INTO Submission (TaskId, Verdict, TimeSpent, MemorySpent, UserId, TestId) VALUES ('" + taskId + "', '" + verdict+ "', '" + timeForTask + "', '" + memoryForTask + "', '" + userId + "', '" + testId + "')";

			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
			stm = connectionMysql.prepareStatement(qury, Statement.RETURN_GENERATED_KEYS);
			stm.executeUpdate(qury);
			rs1 = stm.getGeneratedKeys();
		    if(rs1.next()) {
		    	id = rs1.getInt(1);
		    }
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
			return id;
		}
	}

	@SuppressWarnings("finally")
	public static void updateSubmission(String id, String verdict, String testId, String timeForTask, String memoryForTask) {
		Connection connectionMysql = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}		
		
		Logger.getGlobal().log(Level.INFO, "Driver for MySQL is founded");
		
		try {
			connectionMysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/olymp?user=root");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		try {
			Statement stm = connectionMysql.createStatement();
			String qury = "UPDATE Submission SET "
					+ "Verdict = '" + verdict + "', "
					+ "TimeSpent = " + timeForTask + ", "
					+ "MemorySpent = " + memoryForTask + ", "
					+ "TestId = " + testId + " WHERE Id = " + id;

			Logger.getGlobal().log(Level.INFO, "Try to execute\n" + qury);
			stm.execute(qury);
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				connectionMysql.close();
			} catch (SQLException e) {
			}
			return;
		}
	}

	public static void main(String args[]) {
		System.out.println(getSubmissions(2));
	}
	
}
