
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
			<form id="fileform" enctype="multipart/form-data">
				<label for="comment"><font color="black">Task Title:</font></label>
				<input type="text" class="form-control" id="tasktitle"/>
				<br>
				<label for="comment"><font color="black">Description:</font></label>
				<code class="language-c"> 
					<textarea class="form-control" rows="25" id="description"></textarea>
				</code>
				<br>
				<label for="comment"><font color="black">Tests file:</font></label>
				<input type="file" name="tests" id="tests" accept=".zip">
			</form>
		</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" id="submit">Add Task</button>
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
				<li><a href="#" data-toggle="modal" data-target="#submitWindow">Add Task</a></li>
			  </ul>
			</li>
			<li><a href="#" data-toggle="modal" data-target="#statusWindow">Status</a></li>
			<li><a href="stats.php">Statistics</a></li>
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


    <script src="../js/jquery.js"></script>
    <script src="../js/bootstrap.min.js"></script>
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
						$("#submissions tr:last").after("<tr><td><center>" + data[i].id + "</center></td><td><center>" + data[i].auth + "</center></td><td><center>"  + data[i].task + "</center></td><td><center>" + data[i].verd + "</center></td><td><center>" + data[i].testid + "</center></td></tr>");
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
			var desc = $("#description").val();
			var titl = $("#tasktitle").val();
			var url = "/olymp/api/rest/addtask";
			var json = '{"title":"' + titl + '", "description":"' + desc + '"}';
			var taskid = "1";
			$.ajax({
				type: "POST",
				url: url,
				data: json,
				contentType: "application/json",
				
				success: function(response) {
					taskid = response.id;
					console.log(response);
					var formData = new FormData();
					formData.append('tests', $('input[name="tests"').get(0).files[0]);
					
					$.ajax({
					  url : '/olymp/api/rest/addtasktest?taskId=' + taskid,
					  type : 'POST',
					  data : formData,
					  cache : false,
					  contentType : false,
					  processData : false,
					  success : function(data, textStatus, jqXHR) {
							var message = jqXHR.responseText;
					  },
					  error : function(jqXHR, textStatus, errorThrown) {
					  }
					});
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
	
	</script>
	<style type="text/css">
		body { background: url('images/bk.jpg') no-repeat center center fixed; -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; !important;}
	</style>
