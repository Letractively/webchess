<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="common/jsp/commonhead.jsp"></jsp:include>
<title>Welcome to web chess</title>
<script>
function fixHref(num){
	var type = $("input:radio:checked[name='qp"+num+"']").val();
	$("#room_"+num).attr("href",config.webroot+"/HandleSelectBoard?room="+num+"&type="+type);
}
function desk(num)
{
	this.number = num;
}
var deskCount = 20;
$(function(){
	var arrDesk = [];
	for(var i=1;i<=deskCount;i++){
		arrDesk.push(new desk(i));
	}
	var d = {arrDesk:arrDesk};
	var html = TrimPath.processDOMTemplate("model",d);
	$("body").html(html);	
	$(".nyroModal").nm({type:'iframe'});
});
</script>
</head>
<body>

</body>
<textarea id="model" style="display:none">
{for e in arrDesk}
	<div style="border:1px solid blue;width:80px;float:left;margin:0 0 10px 10px;">
	room:\${e.number}
	<br/>
	<input type="radio"  name="qp\${e.number}" value="1" checked onclick=";fixHref(\${e.number});" />red
	<br/>
	<input type="radio"  name="qp\${e.number}" value="2" onclick=";fixHref(\${e.number});" />black
	<br/>
	<input type="radio"  name="qp\${e.number}" value="0" onclick=";fixHref(\${e.number});" />look
	<br/>
	<a rel="room" href="HandleSelectBoard?room=\${e.number}&type=1" id="room_\${e.number}" class="nyroModal"  target="room_\${e.number}" >enter</a>
	</div>
{/for}
</textarea>
</html>