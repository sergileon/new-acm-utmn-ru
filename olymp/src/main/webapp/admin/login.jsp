<%
	if(request.getParameter("login").equals("admin") && request.getParameter("password").equals("adminko")) {
		Cookie c = new Cookie("admintoken", "FSDfhig84hwgrGDgh8we9ghdsfgdsFDS");
		response.addCookie(c);
		response.sendRedirect("./index.jsp");
	} else {
		response.sendRedirect("../index.jsp");
	}
%>