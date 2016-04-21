
	<div id="statsWindow" class="modal fade" role="dialog">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Statisics</h4>
	      </div>
	      <div class="modal-body">
		<div class="modal-isi-body">

		</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	      </div>
	    </div>
	  </div>
	</div>

	<div id="statusWindow" class="modal fade" role="dialog">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Status</h4>
	      </div>
			<div class="modal-body">
				<div class="modal-isi-body">
					<div class="table-responsive">
						<center>
						<table class="table table-hover" id="submissions">
							<thead>
							  <tr>
								<th><center>#</center></th>
								<th><center>Author</center></th>
								<th><center>Task #</center></th>
								<th><center>Verdict</center></th>
								<th><center>Test #</center></th>
							  </tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" id="updatestatus">Update</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	      </div>
	    </div>
	  </div>
	</div>


	<div id="submitWindow" class="modal fade" role="dialog">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Submit task</h4>
	      </div>
	      <div class="modal-body">
		<div class="form-group">
			<label for="comment"><font color="black">User Email:</font></label>
			<input type="text" class="form-control" id="usermail"/>
			<br>
			<label for="comment"><font color="black">User Token:</font></label>
			<input type="text" class="form-control" id="token"/>
			<br>
			<label for="comment"><font color="black">Task Id:</font></label>
			<input type="text" class="form-control" id="taskid"/>
			<br>
			<label for="sel1"><font color="black">Language:</font></label>
			<select class="form-control" id="language">
				<option value="cpp">MS Visual C++ 2013</option>
				<option value="cs">MS Visual C# 2013</option>
				<option value="java">Java 1.8</option>
				<option value="pas">Pascal</option>
			</select>
			<br>
			<label for="comment">Your code:</label>
			<code class="language-c"> 
				<textarea class="form-control" rows="25" id="sourcecode"></textarea>
			</code>
		</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" id="submit">Submit</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	      </div>
	    </div>
	  </div>
	</div>

	<div id="registerWindow" class="modal fade" role="dialog">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Register user</h4>
	      </div>
	      <div class="modal-body">
		<div class="form-group">
			<label for="comment"><font color="black">Your Name:</font></label>
			<input type="text" class="form-control" id="username"/>
			<br>
			<label for="comment"><font color="black">Your Email:</font></label>
			<input type="text" class="form-control" id="useremail"/>
		</div>
		<div class="g-recaptcha" data-sitekey="6LdPIh0TAAAAAKsPG3hTx7VHWoqTqApR4OF-yrDv"></div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" id="register">Register</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	
	<nav class="navbar navbar-default  navbar-fixed" role="navigation">
	  <div class="container-fluid">
		<div class="navbar-header">
		  <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		  </button>
		  <a class="navbar-brand" href="index.php">Tyumen ACM</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

		  <ul class="nav navbar-nav">
			<li class="dropdown">
			  <a href="#" class="dropdown-toggle" data-toggle="dropdown">Main Menu<b class="caret"></b></a>
			  <ul class="dropdown-menu">
				<li><a href="volume.php?volume=1">Problem Set</a></li>
				<li class="divider"></li>
				<li><a href="contest.php">Contest</a></li>
				<li class="divider"></li>
				<li><a href="#" data-toggle="modal" data-target="#submitWindow">Submit</a></li>
				<li class="divider"></li>
				<li><a href="forum.php">Forum</a></li>
			  </ul>
			</li>
			<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">First Steps<b class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="lab.php?lab=0">Basic Level</a></li>
					<li><a href="lab.php?lab=1">Conditions and Cycles</a></li>
					<li><a href="lab.php?lab=2">Arrays and Strings</a></li>
					<li><a href="lab.php?lab=3">Objects and Classes</a></li>
				</ul>
			</li>
			<li><a href="#" data-toggle="modal" data-target="#statusWindow">Status</a></li>
			<li><a href="stats.php">Statistics</a></li>
		  </ul>
		  <ul class="nav navbar-nav navbar-right">
			<li class="dropdown">
			  <a href="#" class="dropdown-toggle" data-toggle="dropdown">User<b class="caret"></b></a>
			  <ul class="dropdown-menu">
				<li><a href="#" data-toggle="modal" data-target="#registerWindow"><center>Register</center></a></li>
				<li class="divider"></li>
			  </ul>
			</li>
		  </ul>
		  <form class="navbar-form navbar-right" role="search">
			<div class="form-group">
			  <input type="text" class="form-control" placeholder="Search" disabled="true">
			</div>
			<button type="submit" class="btn btn-default disabled">Search</button>
		  </form>
		</div>
	  </div>
	</nav>


	<script src='https://www.google.com/recaptcha/api.js'></script>
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<script>
	
		$("#updatestatus").click(function () {
			$.ajax({
				type: "GET",
				url: "/olymp/api/rest/submitstatus",
				dataType: "json",
				success: function(data) {
					while($('#submissions tr').length > 1) {
						$("#submissions tr:last").remove();
					}
					for(i = 0; i < data.length; i++) {
						//$("#submissions tr:last").after("<tr><td><center>" + data[i].id + "</center></td><td><center>" + data[i].auth + "</center></td><td><center>"  + data[i].task + "</center></td><td><center>" + data[i].verd + "</center></td><td><center>" + data[i].testid + "</center></td><td><center>" + data[i].time + "</center></td><td><center>" + data[i].mem + "</center></td></tr>");
						var verdict = data[i].verd;
						if(verdict == 'WA') verdict = 'Wrong Answer';
						if(verdict == 'TLE') verdict = 'Time Limit Exceeded';
						if(verdict == 'MLE') verdict = 'Memory Limit Exceeded';
						if(verdict == 'AC') verdict = 'Accepted';
						$("#submissions tr:last").after("<tr><td><center>" + data[i].id + "</center></td><td><center>" + data[i].auth + "</center></td><td><center>"  + data[i].task + "</center></td><td><center>" + verdict + "</center></td><td><center>" + data[i].testid + "</center></td></tr>");
					}
				},
				error: function (jqXHR, exception) {
					var msg = '';
					if (jqXHR.status === 0) {
						msg = 'Not connect. Verify Network.';
					} else if (jqXHR.status == 404) {
						msg = 'Requested page not found. [404]';
					} else if (jqXHR.status == 500) {
						msg = 'Internal Server Error [500].';
					} else if (exception === 'parsererror') {
						msg = 'Requested JSON parse failed.';
					} else if (exception === 'timeout') {
						msg = 'Time out error.';
					} else if (exception === 'abort') {
						msg = 'Ajax request aborted.';
					} else {
						msg = 'Uncaught Error.\n' + jqXHR.responseText;
					}
					alert(msg);
				}
			});
			return true;
		});
	
		$("#submit").click(function () {
			var sc = $("#sourcecode").val();
			var lng = $("#language").val();
			var tid = $("#taskid").val();
			var usm = $("#usermail").val();
			var tok = $("#token").val();
			var url = "/olymp/api/rest/submit?" + "taskId=" + tid + "&ext=" + lng + "&userId=" + usm + "&token=" + tok;
			$.ajax({
				type: "POST",
				url: url,
				data: sc,
				contentType: "text/plain",
				success: function(jqXHR, data, textStatus) {
				},
				error: function (jqXHR, exception) {
					var msg = '';
					if (jqXHR.status === 0) {
						msg = 'Not connect. Verify Network.';
					} else if (jqXHR.status == 404) {
						msg = 'Requested page not found. [404]';
					} else if (jqXHR.status == 500) {
						msg = 'Internal Server Error [500].';
					} else if (exception === 'parsererror') {
						msg = 'Requested JSON parse failed.';
					} else if (exception === 'timeout') {
						msg = 'Time out error.';
					} else if (exception === 'abort') {
						msg = 'Ajax request aborted.';
					} else {
						msg = 'Uncaught Error.\n' + jqXHR.responseText;
					}
					alert(msg);
				}
			});			
						
			$("#submitWindow").modal('hide');
			return true;
		});
	
		$("#register").click(function () {
			var nam = $("#username").val();
			var ema = $("#useremail").val();
			var url = "/olymp/api/rest/adduser";
			var json = '{"username":"' + nam + '", "email":"' + ema + '"}';
			$.ajax({
				type: "POST",
				url: url,
				data: json,
				contentType: "application/json",
				success: function(data) {
					alert(data.message);
				},
				error: function (jqXHR, exception) {
					var msg = '';
					if (jqXHR.status === 0) {
						msg = 'Not connect. Verify Network.';
					} else if (jqXHR.status == 404) {
						msg = 'Requested page not found. [404]';
					} else if (jqXHR.status == 500) {
						msg = 'Internal Server Error [500].';
					} else if (exception === 'parsererror') {
						msg = 'Requested JSON parse failed.';
					} else if (exception === 'timeout') {
						msg = 'Time out error.';
					} else if (exception === 'abort') {
						msg = 'Ajax request aborted.';
					} else {
						msg = 'Uncaught Error.\n' + jqXHR.responseText;
					}
					alert(msg);
				}
			});
			$("#registerWindow").modal('hide');
			return true;
		});
	</script>
	<style type="text/css">
		body { background: rgb(94,94,94) no-repeat center center fixed; -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; !important;}
	</style>
