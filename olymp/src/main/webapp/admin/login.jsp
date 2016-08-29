<%
	if(request.getParameter("login").equals("admin") && request.getParameter("password").equals("adminko")) {
		Cookie c = new Cookie("admintoken", "FSDfhig84hwgrGDgh8we9ghdsfgdsFDS");
		response.addCookie(c);
		response.sendRedirect("/olymp/admin/index.jsp");
	} else {
		response.sendRedirect("/olymp/index.jsp");
	}
%>