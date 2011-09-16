
<jsp:include page="commonpage.jsp"></jsp:include>
<html>
<head>
<jsp:include page="commonhead.jsp"></jsp:include>
<title>Welcome to web chess</title>
<script>
function SetDeskNumber(num)
{
	var type = $("input:radio:checked[name='qp"+num+"']").val();
	$.ajax({
		type:"POST",
		url:"HandleSelectBoard",
		data:{"room":num,"type":type},
		success:function(d)
		{
			eval(d);			
		},
		error:function(ex)
		{alert(ex);}
	});	
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
<div id="model" style="display:none">
{for e in  arrDesk}
	<div style="border:1px solid blue;width:80px;float:left;margin:0 0 10px 10px;">
	room:\${e.number}
	<br/>
	<input type="radio"  name="qp\${e.number}" value="1" checked />red
	<br/>
	<input type="radio"  name="qp\${e.number}" value="2" />black
	<br/>
	<input type="radio"  name="qp\${e.number}" value="0" />look
	<br/>
	<input type="button" onclick="SetDeskNumber(\${e.number})" value="enter" />
	</div>
{/for}
</div>
</html>