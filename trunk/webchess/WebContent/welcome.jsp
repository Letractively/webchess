<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="java.util.*,com.whwqs.webchess.core.*,java.lang.*" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to web chess</title>
<script src="common/js/jquery-1.5.2.min.js"></script>
<script src="common/js/template.js"></script>
<script>
function SetDeskNumber(num)
{
	$.ajax({
		type:"POST",
		url:"HandleSelectBoard",
		data:{"":},
		success:function()
		{
			window.location.href = "desk.jsp";
		}
	});	
}
</script>
</head>
<body>
<div style="border:1px solid blue;">
desk:1
</div>

</body>
</html>