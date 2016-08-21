<%@page import="org.json.JSONObject"%>
<%@page import="ru.sibint.olymp.dbsync.DBProxy"%>
<%@page import="org.json.JSONArray"%>
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
		
		<br><br><br><br>
		<center>
			<h2><font color="white">Authors rating:</font></h2>
		</center>

	<center>
	<table width="100%">
		<tbody>
			<tr>
			<td width="25%"></td>
			<td>
	<div class="table-responsive">
		<center>
		<table class="table">
			<thead>
			  <tr>
				<th><center><font color="white">Name</font></center></th>
				<th><center><font color="white">Problems solved</font></center></th>
			  </tr>
			</thead>
			<tbody>
				<%
					String json = DBProxy.getStats();
					JSONArray jo = new JSONArray(json);
					for(int i = 0; i < jo.length(); i++)
					{
						JSONObject j = jo.getJSONObject(i); 
						out.println("<tr>");
						out.println("<td><center><font color=\"white\">" + j.getString("Name") + "</font></center></td>" + "<td><center><font color=\"white\">" + j.getString("Rating") + "</font></center></td>");
						out.println("</tr>");							
					}
				%>
			</tbody>
		  </table>
  		<center>
	</div>
			</td><td width="25%"></td>
			</tr>
		</tbody>
	</table>
	</center>
		
		
	</body> 
</html>