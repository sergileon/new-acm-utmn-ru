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
							<?php
								$ch = curl_init();
								curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
								curl_setopt($ch, CURLOPT_URL, "http://localhost/olymp/api/rest/gettasks");
								$tasks = curl_exec($ch);
								curl_close($ch);
								
								$obj = json_decode($tasks);
								$rowNum = 0;
								foreach($obj as $task) {
									$rowNum++;
									echo "<tr>";
									echo "<td><font color=\"white\">" . $rowNum . "</font></td>" . "<td><a href=\"task.php?id=" . $task->Id . "\"><font color=\"white\">" . $task->Name . "</font></a></td>";
									echo "<td><font color=\"white\">0</font></td><td><font color=\"white\">1 sec.</font></td><td><font color=\"white\">64 MB.</font></td>";
									echo "</tr>\n";
								}

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
