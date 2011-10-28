<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="common/jsp/commonhead.jsp"></jsp:include>
<title>Welcome to web chess</title>
<script>
var message = "";
var deskCount = 30;
function fixHref(num,type){
	$("#room_"+num).attr("href",config.webroot+"/HandleSelectBoard?room="+num+"&type="+type);
};
function fixHref2(num,type){
	$("#computer_"+num).attr("href",config.webroot+"/HandleSelectComputer?room=c"+num+"&type="+type);
};
function desk(num)
{
	this.number = num;
};
function getSeatType(num){
	return $(":radio:checked[name=qp"+num+"]").val();
};
function getSeatType2(num){
	return $(":radio:checked[name=computer"+num+"]").val();
};
function enterRoom(num){
	$.nmTop().close();
	$("#room_"+num).click();
};
function enterRoom2(num){
	$.nmTop().close();
	$("#computer_"+num).click();
};
function gameoverHandle(num,msg){
	$.nmTop().close();
	message=msg;
	$("#handleGameOver").attr("href","jsp/game.jsp?room="+num).click();
};
function gameoverHandle2(num,msg){
	$.nmTop().close();
	message=msg;
	$("#handleGameOver").attr("href","jsp/game.jsp?computer="+num).click();
};

function InitRoom(){
	var arrDesk = [];
	for(var i=1;i<=deskCount;i++){
		arrDesk.push(new desk(i));
	}
	var d = {arrDesk:arrDesk};
	var html = TrimPath.processDOMTemplate("model",d);
	$("#rooms").html(html);		
};

function InitComputer(){
	var arrDesk = [];
	for(var i=1;i<=deskCount;i++){
		arrDesk.push(new desk(i));
	}
	var d = {arrDesk:arrDesk};
	var html = TrimPath.processDOMTemplate("computermodel",d);
	$("#computers").html(html);	
};

$(function(){
	InitRoom();
	InitComputer();
	$(".nyroModal").each(function(){
		$(this).nm({
			titleFromIframe: true,
			resizable: false,
			autoSizable: false,
			width:700,
			height:620,			
			sizes:{
				initW: 600,    // Initial width
                initH: 600,    // Initial height
                w: 600,        // width
                h: 600,        // height
                minW: 800,    // minimum Width这个会生效
                minH: 800,    // minimum height这个会生效
                wMargin: 1,    // Horizontal margin
                hMargin: 1// Vertical margin
			},
			closeOnClick: true,
			modal:true
		});
	});
	$("#handleGameOver").nm({modal:true});
});
</script>
</head>
<body>
<div id="rContainer" style="border:1px solid green;background:green;text-align:center;margin-bottom:10px;">
<font size="6px">大众厅</font>
</div>
<div id="rooms">
</div>
<div style="clear:both;"></div>
<br/>
<div id="cContainer" style="border:1px solid green;background:green;text-align:center;margin-bottom:10px;">
<font size="6px">电脑厅</font>
</div>
<div id="computers">
</div>

<textarea id="model" style="display:none">
{for e in arrDesk}
	<div style="border:1px solid blue;width:80px;float:left;margin:0 0 10px 10px;">
	room:\${e.number}
	<br/>
	<input type="radio" id="qp\${e.number}1"  name="qp\${e.number}" value="1" checked onclick=";fixHref(\${e.number},1);" />red
	<br/>
	<input type="radio" id="qp\${e.number}2" name="qp\${e.number}" value="2" onclick=";fixHref(\${e.number},2);" />black
	<br/>
	<input type="radio" id="qp\${e.number}0" name="qp\${e.number}" value="0" onclick=";fixHref(\${e.number},0);" />look
	<br/>
	<a href="HandleSelectBoard?room=\${e.number}&type=1" id="room_\${e.number}" class="nyroModal"  target="_blank" >enter</a>
	</div>
{/for}
</textarea>
<textarea id="computermodel" style="display:none">
{for e in arrDesk}
	<div style="border:1px solid blue;width:80px;float:left;margin:0 0 10px 10px;">
	computer:\${e.number}
	<br/>
	<input type="radio" id="computer\${e.number}1"  name="computer\${e.number}" value="1" checked onclick=";fixHref2(\${e.number},1);" />red
	<br/>
	<input type="radio" id="computer\${e.number}2" name="computer\${e.number}" value="2" onclick=";fixHref2(\${e.number},2);" />black
	<br/>
	<input type="radio" id="computer\${e.number}0" name="computer\${e.number}" value="0" onclick=";fixHref2(\${e.number},0);" />look
	<br/>
	<a href="HandleSelectComputer?room=c\${e.number}&type=1" id="computer_\${e.number}" class="nyroModal"  target="_blank" >enter</a>
	</div>
{/for}
</textarea>
<a href="jsp/game.jsp" id="handleGameOver" rev="modal" target="_blank" style="display:none;" >game over</a>
</body>
</html>