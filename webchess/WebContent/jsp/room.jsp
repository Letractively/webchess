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
	$("#funcList").appendTo(chessBoard.container);
	var qpW = 500;
	var qpH = 550;
	if(config.type==0){
		qpW=550;
		qpH=500;
	}
	
	$(window).unload(function(){
		chessBoard.KillTimer();
	});
	
	var deskCount = parent.deskCount;
	var curDesk = config.room;
	var curType = config.type;
	$("#preRoom").click(function(e){
		e.preventDefault();
		var room = (curDesk+deskCount-1)%deskCount;
		parent.$("#room_"+room).click();
	});
	$("#nextRoom").click(function(e){
		e.preventDefault();
		var room = (curDesk+1)%deskCount;
		parent.$("#room_"+room).click();
	});
	$("#changeSeat").click(function(e){
		e.preventDefault();
		var type = (curType+1)%3;
		alert(parent.$("#qp"+curDesk+""+type).attr("id"));
		parent.$("#room_"+curDesk).click();
	});
	
	parent.$("iframe[id*='nyromodal-iframe-']")
	.width(qpW).height(qpH)
	.css({left:"50%",top:"50%","margin-left":"-"+qpW/2+"px","margin-top":"-"+qpH/2+"px",position:"absolute"})
	.parent().width(600).height(600)
	.css({left:"50%",top:"50%","margin-left":"-300px","margin-top":"-300px",position:"absolute"});
});
</script>
</head>
<body>
<div id="qp" style="position:relative;"></div>
<div id="funcList" style="position:absolute;bottom:5px;left:10px;">
	&nbsp;<a href="#" id="preRoom">preRoom</a>
	&nbsp;<a href="#" id="changeSeat">changeSeat</a>
	&nbsp;<a href="#" id="nextRoom">nextRoom</a>
</div>
</body>
</html>