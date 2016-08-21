<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page import="ru.sibint.olymp.dbsync.DBProxy"%>
<%@page import="ru.sibint.olymp.api.APIEndpoint"%>
<html> 
	<head> 
		<title>Association of Tyumen Coders</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
		<link href="css/dopstyle.css" rel="stylesheet" media="screen">	  
		<script src="js/jquery.js"></script> 
		<script> 
		$(function(){
		  $("#includedContent").load("menu.jsp"); 
		});
		</script> 
	</head> 

	<body> 
		<div id="includedContent"></div>
	</body> 

	<font color="white">
	<br><br><br>
	<center>
		<h2>Online Contester: Problem Set</h2>
	</center>
	<br><br><br>	
	
	<center>
	<table width="100%">
		<tbody>
			<tr>
			<td width="10%"></td>
			<td>
				<div class="table-responsive">
					<center>
					<table class="table">
						<thead>
						  <tr>
							<th align="right"><font color="white">Task #</font></th>
							<th><font color="white">Name</font></th>
							<th><font color="white">Solutions count</font></th>
							<th><font color="white">Time Limit</font></th>
							<th><font color="white">Memory Limit</font></th>
						  </tr>
						</thead>
						<tbody>
							<%
								String json = DBProxy.getTasks();
								JSONArray jo = new JSONArray(json);
								for(int i = 0; i < jo.length(); i++)
								{
									JSONObject j = jo.getJSONObject(i); 
									out.println("<tr>");
									out.println("<td><font color=\"white\">" + (i + 1) + "</font></td>" +  "<td><a href=\"task.jsp?id="  + j.getString("Id") +  "\"><font color=\"white\">" + j.getString("Name") + "</font></a></td>");
									out.println("<td><font color=\"white\">0</font></td><td><font color=\"white\">1 sec.</font></td><td><font color=\"white\">64 MB.</font></td>");
									out.println("</tr>");							
								}
							%>
						</tbody>
					  </table>
					<center>
				</div>
			</td><td width="10%"></td>
			</tr>
		</tbody>
	</table>
	</center>
	</font>

	</body>
</html>
