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
		  $("#includedContent").load("menu.php"); 
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
					<table class="table table-hover">
						<thead>
						  <tr>
							<th>Task #</th>
							<th>Name</th>
							<th>Solutions count</th>
							<th>Time Limit</th>
							<th>Memory Limit</th>
						  </tr>
						</thead>
						<tbody>
							<?php
							$servername = "localhost";
							$username = "root";
							$password = "";
							$dbname = "olymp";

							$conn = new mysqli($servername, $username, $password, $dbname);
							if ($conn->connect_error) {
								die("Connection failed: " . $conn->connect_error);
							} 

							$sql = "SELECT Id, Name FROM Task";
							$result = $conn->query($sql);

							if ($result->num_rows > 0) {
								while($row = $result->fetch_assoc()) {
									echo "<tr>";
									echo "<td><font color=\"white\">" . $row["Id"] . "</font></td>" . "<td><a href=\"task.php?id=" . $row["Id"] . "\"><font color=\"white\">" . $row["Name"] . "</font></a></td>";
									echo "<td><font color=\"white\">0</font></td><td><font color=\"white\">1 sec.</font></td><td><font color=\"white\">64 MB.</font></td>";
									echo "</tr>\n";
								}
							}

							$conn->close();
							?>			  						  
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
