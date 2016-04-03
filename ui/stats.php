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
		<table class="table">
			<thead>
			  <tr>
				<th><center><font color="white">Name</font></center></th>
				<th><center><font color="white">Problems solved</font></center></th>
			  </tr>
			</thead>
			<tbody>
<?php
$ch = curl_init();
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_URL, "http://localhost/olymp/api/rest/getstats");
$stats = curl_exec($ch);
curl_close($ch);

$obj = json_decode($stats);

foreach($obj as $stat) {
	echo "<tr>";
	echo "<td><center><font color=\"white\">" . $stat->Name . "</font></center></td>" . "<td><center><font color=\"white\">" . $stat->Count . "</font></center></td>";
	echo "</tr>\n";
}
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