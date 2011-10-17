<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="common/jsp/commonhead.jsp"></jsp:include>
<title>Welcome to web chess</title>
<script>
function fixHref(num,type){
	$("#room_"+num).attr("href",config.webroot+"/HandleSelectBoard?room="+num+"&type="+type);
}
function desk(num)
{
	this.number = num;
}
function getSeatType(num){
	return $(":radio:checked[name=qp"+num+"]").val();
}
function enterRoom(num){
	$("#room_"+num).click();
}
function gameoverHandle(num,msg){
	
}
var deskCount = 20;
$(function(){
	var arrDesk = [];
	for(var i=1;i<=deskCount;i++){
		arrDesk.push(new desk(i));
	}
	var d = {arrDesk:arrDesk};
	var html = TrimPath.processDOMTemplate("model",d);
	$("#rooms").html(html);	
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
			showCloseButton: true,
			closeButton: '<a href="#" class="nyroModalClose nyroModalCloseButton nmReposition" title="close">Close</a>'				
		});
	});
	$("#handleGameOver").nm({modal:true});
});
</script>
</head>
<body>
<div id="rooms">
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
<a href="jsp/game.jsp" id="handleGameOver" rev="modal" target="_blank" style="display:none;" ></a>
</body>
</html>