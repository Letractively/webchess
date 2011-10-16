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
config.roomNum = <%=roomNumber %>;
config.seatType = <%=manType %>;
config.qpBackgroundColor = "rgb(100,200,200)";
config.qpLineColor = "#003663";
config.qpColor = "rgb(201,199,224)";
var deskCount = parent.deskCount;
var chessBoard = {};

$(function(){	
	chessBoard = new ChessBoard($("#qp"));
	chessBoard.DrawBoard();	
	chessBoard.data = '<%=board.ToString()%>';
	chessBoard.SetBoardByData();
	chessBoard.setTimer();
	
	$(window).unload(function(){
		chessBoard.KillTimer();
	});	
	
	$("#preRoom").click(function(e){
		e.preventDefault();
		var room = (config.roomNum+deskCount-1)%deskCount;
		if(room==0)room=deskCount;
		config.roomNum =room;
		config.seatType = parent.getSeatType(config.roomNum);
		chessBoard.AfterChangeRoom = function(){
			SetIframe();
			chessBoard.roomNum.text("room: "+config.roomNum+" ");
		};
		chessBoard.ChangeRoom();
	});
	$("#nextRoom").click(function(e){
		e.preventDefault();
		var room = (config.roomNum+1)%deskCount;
		if(room==0)room=deskCount;
		config.roomNum =room;
		config.seatType = parent.getSeatType(config.roomNum);
		chessBoard.AfterChangeRoom = function(){
			SetIframe();
			chessBoard.roomNum.text("room: "+config.roomNum+" ");
		};
		chessBoard.ChangeRoom();
	});
	$("#changeSeat").click(function(e){
		e.preventDefault();
		config.seatType = (config.seatType+1)%3;
		chessBoard.AfterChangeRoom = function(){
			parent.$("#qp"+config.roomNum+""+config.seatType).attr("checked",true);
			parent.fixHref(config.roomNum,config.seatType);
			SetIframe();
		};
		chessBoard.ChangeRoom();
	});
	
	var nyroIframe = parent.$("iframe[id*='nyromodal-iframe-']");
	var nyroIframe_father = nyroIframe.parent();
	var nyroIframe_grandfather = nyroIframe.parent().parent();
	
	function SetIframe(){
		var qpW = 500;
		var qpH = 550;
		if(config.seatType==0){
			qpW=550;
			qpH=500;
		}
		
		nyroIframe.width(qpW).height(qpH)
		.css({left:"50%",top:"50%","margin-left":"-"+qpW/2+"px","margin-top":"-"+qpH/2+"px",position:"absolute"});
		
		nyroIframe_father.width(600).height(600)
		.css({left:"50%",top:"50%","margin-left":"-300px","margin-top":"-300px",position:"absolute"});
	}
	
	SetIframe();
	
	$("#funcList1").appendTo(nyroIframe_grandfather);	
	$("#funcList2").appendTo(nyroIframe_grandfather);	
	chessBoard.roomNum=$("#roomnum").text("room: "+config.roomNum+" ").prependTo(nyroIframe_grandfather).end();
	chessBoard.msg = $("#msg").prependTo(nyroIframe_grandfather).end();
	
	nyroIframe_grandfather.css({"background-color":config.qpBackgroundColor});
});
</script>
</head>
<body>
<div id="qp" style="position:relative;"></div>
<div id="msg" style="position:absolute;bottom:5px;left:10px;font-size:large;"></div>
<div id="roomnum" style="position:absolute;bottom:5px;right:10px;font-size:large;"></div>
<div id="funcList1" style="position:absolute;top:5px;left:10px;font-size:large;">
	&nbsp;<a href="#" id="preRoom">preRoom</a>
	&nbsp;<a href="#" id="nextRoom">nextRoom</a>
</div>
<div id="funcList2" style="position:absolute;top:5px;right:10px;font-size:large;">
	<a href="#" id="changeSeat">changeSeat</a>&nbsp;
</div>
<div style="display:none">
	<a href="demoSent.php" id="handleGameOver" rev="modal">handle win</a>
</div>
</body>
</html>