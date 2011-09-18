<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="common/jsp/commonhead.jsp"></jsp:include>
<title>Welcome to web chess</title>
<script>
function fixHref(a,num){
	var type = $("input:radio:checked[name='qp"+num+"']").val();
	a.href = "HandleSelectBoard?room="+num+"&type="+type;
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
	<input type="radio"  name="qp\${e.number}" value="1" checked />red
	<br/>
	<input type="radio"  name="qp\${e.number}" value="2" />black
	<br/>
	<input type="radio"  name="qp\${e.number}" value="0" />look
	<br/>
	<a href=""  target="room_\${e.number}" onclick=";fixHref(this,\${e.number});">enter</a>
	</div>
{/for}
</textarea>
</html>