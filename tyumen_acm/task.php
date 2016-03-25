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
		  $("#includedContent").load("menu.php"); 
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
	
<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "olymp";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "SELECT Id, Name, Description FROM Task " . " WHERE Id = " . $_GET["id"];
$result = $conn->query($sql);
$taskid = 1;

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
		//echo "<tr><td></td><td><center><h2>" . $row["Id"] . ". " . $row["Name"] . "</h2><br><br></center></td><td></td></tr>";
		$taskid = $row["Id"];
		echo "<tr><td colspan=\"3\"><center><h2>" . $row["Id"] . ". " . $row["Name"] . "</h2><br><br></center></td></tr>";
		echo "<tr><td></td><td><h3>Description:</h3><br><p class=\"text-info\" align=\"justify\">" . $row["Description"] . "</p><br><br></td><td></td></tr>";
    }
	echo "<tr><td></td><td><h3>Sample input: </h3><br></td><td></td></tr>"; 
	echo "<tr><td></td><td><h3>Sample output: </h3><br></td><td></td></tr>"; 
}

$conn->close();
?>			  
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
