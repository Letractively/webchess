<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="java.util.*,com.whwqs.webchess.core.*,java.lang.*" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to web chess</title>
</head>
<body>
<% 
ChessBoard b = new ChessBoard();
b.SetBoard(b.ToString().replace('空', '相'));
out.print(b.ToString()+"<br/>");
ChessType t = b.getBoardData()[0][0];
b.getBoardData()[0][0] = ChessType.Get("将");
out.print(t.getName()+" "+b.getBoardData()[0][0].getName());
%>
</body>
</html>