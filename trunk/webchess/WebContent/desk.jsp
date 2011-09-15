<jsp:include page="commonpage.jsp"></jsp:include>
<html>
<head>
<jsp:include page="commonhead.jsp"></jsp:include>
<title>desk</title>
</head>
<body>
<% 

    if(session.getAttribute("number")!=null
    &&session.getAttribute("isRed")!=null)
    {
    	session.removeAttribute("number");
    }
    else
    {
    	//response.sendRedirect("welcome.jsp");
    }
%>
</body>
</html>