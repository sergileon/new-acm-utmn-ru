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
			<h2><font color="white">Create contest:</font></h2>
			<form action="createContest.jsp">
				<table>
					<tr><td>Contest description: </td><td><input type="text" size="40"/></td></tr>
					<tr><td></td><td><br></td></tr>
					<tr><td>Contest start: </td><td><input type="datetime-local"/></td></tr>
					<tr><td></td><td><br></td></tr>
					<tr><td>Contest end: </td><td><input type="datetime-local"/></td></tr>
					<tr><td></td><td><br></td></tr>
					<tr><td>Contest length: </td><td><input type="text" disabled="disabled"/></td></tr>
					<tr><td></td><td><br></td></tr>
					<tr><td>Contest tasks (comma-separated ids): </td><td><input type="text" size="40"/></td></tr>
					<tr><td></td><td><br></td></tr>
					<tr><td></td><td><input type="submit"/></td></tr>
				</table>
			</form>
		</center>
	</body> 
</html>