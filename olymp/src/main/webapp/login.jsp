<%@page import="ru.sibint.olymp.dbsync.QueryType"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="ru.sibint.olymp.dbsync.DBProxy"%>
<%
	List<Object> list = DBProxy.evaluateQuery("SELECT Id, Name FROM UserApp WHERE email = '" + request.getParameter("login") + "' AND token = '" + request.getParameter("password") + "'", QueryType.SELECT);
	if(list != null) {
		String userId = ((HashMap<String, String>)list.get(0)).get("Id");
		String usname = ((HashMap<String, String>)list.get(0)).get("Name");
		Cookie c = new Cookie("user", userId);
		response.addCookie(c);
		c = new Cookie("username", usname);
		response.addCookie(c);
	}
	response.sendRedirect("./index.jsp");
%>