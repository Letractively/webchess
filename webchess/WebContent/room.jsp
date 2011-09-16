<%@page import="com.whwqs.webchess.*, com.whwqs.webchess.core.*"  %>
<%! 
	ChessBoard board = null;
	int roomNumber = -1;
	int manType = -1;
%>
<% 
Object num = request.getSession().getAttribute("room"); 
Object type = request.getSession().getAttribute("type"); 
if(num==null || type==null)
{
	response.sendRedirect("welcome.jsp");
}
else
{
	
}
%>
<jsp:include page="commonpage.jsp"></jsp:include>
<html>
<head>
<jsp:include page="commonhead.jsp"></jsp:include>
<title>desk</title>
</head>
<body>
</body>
</html>