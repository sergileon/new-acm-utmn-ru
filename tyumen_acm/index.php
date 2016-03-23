<!DOCTYPE html>
<html>
  <head>
    <title>Association of Tyumen Coders</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
	<link href="css/dopstyle.css" rel="stylesheet" media="screen">	
  </head>
  <body>

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

		</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Update</button>
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
	<nav class="navbar navbar-default  navbar-fixed" role="navigation">
	  <div class="container-fluid">
		<div class="navbar-header">
		  <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		  </button>
		  <a class="navbar-brand" href="index.html">Tyumen ACM</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

		  <ul class="nav navbar-nav">
			<li class="dropdown">
			  <a href="#" class="dropdown-toggle" data-toggle="dropdown">Problem Set<b class="caret"></b></a>
			  <ul class="dropdown-menu">
				<li><a href="volume.php?volume=1">Volume 1</a></li>
				<li><a href="volume.php?volume=2">Volume 2</a></li>
				<li><a href="volume.php?volume=3">Volume 3</a></li>
				<li class="divider"></li>
				<li><a href="contest.php">Contest</a></li>
				<li class="divider"></li>
				<li><a href="#" data-toggle="modal" data-target="#submitWindow">Submit</a></li>
				<li class="divider"></li>
				<li><a href="forum.html">Forum</a></li>
			  </ul>
			</li>
			<li><a href="#" data-toggle="modal" data-target="#statusWindow">Status</a></li>
			<li><a href="stats.php">Statistics</a></li>
		  </ul>
		  <ul class="nav navbar-nav navbar-right">
			<li class="dropdown">
			  <a href="#" class="dropdown-toggle" data-toggle="dropdown">User<b class="caret"></b></a>
			  <ul class="dropdown-menu">
				<li><a href="#">Log In/Sign In</a></li>
				<li class="divider"></li>
				<li><a href="#">My profile</a></li>
			  </ul>
			</li>
		  </ul>
		  <form class="navbar-form navbar-right" role="search">
			<div class="form-group">
			  <input type="text" class="form-control" placeholder="Search">
			</div>
			<button type="submit" class="btn btn-default">Search</button>
		  </form>
		</div>
	  </div>
	</nav>


    <script src="https://code.jquery.com/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<script>
		$("#submit").click(function () {
			var text = $("#sourcecode").val();
			
			$.ajax({
			  type: "POST",
			  url: "http://localhost:8080/olymp/api/rest/submit/cpp/1/1",
			  data: text,
			});			
						
			$("#submitWindow").modal('hide');
		});
	</script>
  </body>
</html>
