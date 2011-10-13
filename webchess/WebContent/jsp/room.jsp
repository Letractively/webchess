<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@page import="com.whwqs.webchess.*, com.whwqs.webchess.core.*"  %>
<%-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>

<%! 
	ChessBoard board = null;
	String roomNumber = null;
	String manType = null;
	String[] typeString = new String[]{"look","red","black"};
%>
<% 
Object n = request.getSession().getAttribute("room"); 
Object t = request.getSession().getAttribute("type"); 
if(n==null || t==null)
{
	response.sendRedirect("../welcome.jsp");
	return;
}
else
{
	roomNumber = n.toString();
	manType = t.toString();
	board = ChessBoardManager.GetChessBoard(roomNumber);
}
%>

<html>
<head>
<jsp:include page="../common/jsp/commonhead.jsp"></jsp:include>
<title>desk<%=roomNumber %>_<%=typeString[Integer.valueOf(manType)] %></title>
<script type="text/javascript" src="../js/chessboard.js"></script>
<script type="text/javascript">
config.room = <%=roomNumber %>;
config.type = <%=manType %>;
var chessBoard = {};
function SetBoardData(data)
{
	chessBoard.data = data;
	chessBoard.SetBoardByData();
}
$(function(){
	chessBoard = new ChessBoard($("#qp"));
	chessBoard.DrawBoard();
	SetBoardData( '<%=board.ToString()%>');
	chessBoard.setTimer();
	parent.$(".nyroModalCont").width(800).height(700);
	parent.$("iframe[id*='nyromodal-iframe-']").width(600).height(600).css({left:"50%",top:"50%"});
});
</script>
</head>
<body style="width:800px;height:800px;">
<div id="qp" ></div>
</body>
</html>