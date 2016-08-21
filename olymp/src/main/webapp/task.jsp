<%@page import="ru.sibint.olymp.api.APIEndpoint"%>
<%@page import="org.json.JSONObject"%>
<%@page import="ru.sibint.olymp.dbsync.DBProxy"%>
<html> 
	<head> 
		<title>Association of Tyumen Coders</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<link href="css/bootstrap.css" rel="stylesheet" media="screen">
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

	<br><br><br>
	<center>
	
	<center>
	<table width="100%">
		<tbody>
			<tr>
			<td width="10%"></td>
			<td>
	<div class="table-responsive">
	<table class="table table-borderless" width="80%">
		<thead>
		  <tr>
			<th></th>
			<th></th>
			<th></th>
		  </tr>
		</thead>
		<tbody>
			<tr>
		
			<%
				APIEndpoint ae = new APIEndpoint();
				String json = DBProxy.getDescription(Integer.valueOf(request.getParameter("id")));
				JSONObject jo = new JSONObject(json);
				String inTest = ae.getTaskTestIn(Integer.valueOf(request.getParameter("id")), 1);
				String outTest = ae.getTaskTestOut(Integer.valueOf(request.getParameter("id")), 1);
				String name = jo.getString("Name");
				String description = jo.getString("Description");
				out.println("<tr><td colspan=\"3\"><center><h2><font color=\"white\">" + name + "</font></h2><br><br></center></td></tr>");
				out.println("<tr><td></td><td><h3><font color=\"white\">Description:</font></h3><br><p class=\"text-info\" align=\"justify\"><font color=\"white\">" + description + "</font></p><br><br></td><td></td></tr>");
				out.println("<tr><td></td><td><h3><font color=\"white\">Sample input: </font></h3><br></td><td></td></tr>"); 
				out.println("<tr><td></td><td><font color=\"white\">" + inTest + "</font></td><td></td></tr>"); 
				out.println("<tr><td></td><td><h3><font color=\"white\">Sample output: </font></h3><br></td><td></td></tr>"); 
				out.println("<tr><td></td><td><font color=\"white\">" + outTest + "</font></td><td></td></tr>"); 
			%>
			</tr>
			<tr>
				<td></td><td></td>
				<td>
					<center>
						<button><a href="forum.php?taskid=<?php echo $taskid; ?>">Forum</a></button>
						<button id="openSubmit"><a href="#" data-toggle="modal" data-target="#submitWindow" id="oS">Submit</a></button>
					</center>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
			</td><td width="10%"></td>
			</tr>
		</tbody>
	</table>
	</center>
	
	<script>
	
		function getParameterByName(name, url) {
			if (!url) url = window.location.href;
			url = url.toLowerCase();
			name = name.replace(/[\[\]]/g, "\\$&").toLowerCase();
			var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
				results = regex.exec(url);
			if (!results) return null;
			if (!results[2]) return '';
			return decodeURIComponent(results[2].replace(/\+/g, " "));
		}
	
		$("#oS").click(function() {
			$("#taskid").val(getParameterByName('id'));
		});
	</script>
	</body>
</html>
