<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>desk</title>
<script src="common/js/jquery-1.5.2.min.js"></script>
<script src="common/js/template.js"></script>
</head>
<body>
<% 

    if(session.getAttribute("deskNum")==null)
    {
    	response.sendRedirect("welcome.jsp");
    	return;
    }
%>
</body>
</html>