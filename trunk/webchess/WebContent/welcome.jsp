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
$(function(){
	$("#btnTest").click(function(){
		$.ajax({
			type:"POST",
			url:"HandleClickBoard",
			data:{},
			success:function(){alert(1);},
			error:function(){alert(2);}
		});
	});
});
</script>
</head>
<body>
<input type="button" value="test" id="btnTest"/>
</body>
</html>