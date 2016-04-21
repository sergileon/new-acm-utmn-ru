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
$ch = curl_init();
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_URL, "http://localhost/olymp/api/rest/getasktestin?taskId=" . $_GET["id"] . "&testId=1");
$inTest = curl_exec($ch);
curl_setopt($ch, CURLOPT_URL, "http://localhost/olymp/api/rest/getasktestout?taskId=" . $_GET["id"] . "&testId=1");
$outTest = curl_exec($ch);
curl_setopt($ch, CURLOPT_URL, "http://localhost/olymp/api/rest/getdescription?id=" . $_GET["id"]);
$desc = curl_exec($ch);
curl_close($ch);

$obj = json_decode($desc);
$tmp = $obj->Description;
//$tmp = iconv('UTF-8', 'CP1251', $tmp);
$tmp1 = $obj->Name;
//$tmp1 = iconv('UTF-8', 'CP1251', $tmp1);
$taskid = $_GET["id"];
echo "<tr><td colspan=\"3\"><center><h2><font color=\"white\">" . $_GET["id"] . ". " . $tmp1 . "</font></h2><br><br></center></td></tr>";
echo "<tr><td></td><td><h3><font color=\"white\">Description:</font></h3><br><p class=\"text-info\" align=\"justify\"><font color=\"white\">" . $tmp . "</font></p><br><br></td><td></td></tr>";

$inTest = iconv('UTF-8', 'CP1251', $inTest);
$outTest = iconv('UTF-8', 'CP1251', $outTest);
echo "<tr><td></td><td><h3><font color=\"white\">Sample input: </font></h3><br></td><td></td></tr>"; 
echo "<tr><td></td><td><font color=\"white\">" . $inTest . "</font></td><td></td></tr>"; 
echo "<tr><td></td><td><h3><font color=\"white\">Sample output: </font></h3><br></td><td></td></tr>"; 
echo "<tr><td></td><td><font color=\"white\">" . $outTest . "</font></td><td></td></tr>"; 
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
