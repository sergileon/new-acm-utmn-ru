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
		<table class="table table-hover">
			<thead>
			  <tr>
				<th><center><font color="white">Name</font></center></th>
				<th><center><font color="white">Problems solved</font></center></th>
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

$sql = "SELECT UserApp.Name as UserName, COUNT(DISTINCT Submission.TaskId) AS Rating FROM UserApp LEFT JOIN Submission ON UserApp.Id = Submission.UserId GROUP BY UserApp.Name ORDER BY Rating DESC;";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
		echo "<tr>";
        echo "<td><center><font color=\"white\">" . $row["UserName"] . "</font></center></td>" . "<td><center><font color=\"white\">" . $row["Rating"] . "</font></center></td>";
		echo "</tr>\n";
    }
}

$conn->close();
?>			  
			  
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