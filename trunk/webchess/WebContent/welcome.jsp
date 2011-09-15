
<jsp:include page="commonpage.jsp"></jsp:include>
<html>
<head>
<jsp:include page="commonhead.jsp"></jsp:include>
<title>Welcome to web chess</title>
<script>
function SetDeskNumber(num)
{
	var isRed = $("input:radio:checked[name='qp"+num+"']").val();
	$.ajax({
		type:"POST",
		url:"HandleSelectBoard",
		data:{"number":num,"isRed":isRed},
		success:function(d)
		{
			if(d==1)
			{
				window.location.href = "desk.jsp";
			}
			
		}
	});	
}
function desk(num)
{
	this.number = num;
}
var deskCount = 10;
$(function(){
	var arrDesk = [];
	for(var i=1;i<=deskCount;i++){
		arrDesk.push(new desk(i));
	}
	var d = {arrDesk:arrDesk};
	var html = Trimpath.processDOMTemplate("model",d);
	alert(html);
});
</script>
</head>
<body>
<div style="border:1px solid blue;width:120px;float:left;margin-left:10px;">
desk:1
<br/>
<input type="radio"  name="qp1" value="1" checked />red
&nbsp;
<input type="radio"  name="qp1" value="0" />black
<br/>
<input type="button" onclick="SetDeskNumber(1)" value="select" />
</div>

<div style="border:1px solid blue;width:120px;float:left;margin-left:10px;">
desk:2
<br/>
<input type="radio"  name="qp2" value="1" checked />red
&nbsp;
<input type="radio"  name="qp2" value="0" />black
<br/>
<input type="button" onclick="SetDeskNumber(2)" value="select" />
</div>
</body>
<div id="model" style="display:none">
{for e in  d.arrDesk}
	<div style="border:1px solid blue;width:120px;float:left;margin-left:10px;">
	desk:1
	<br/>
	<input type="radio"  name="qp1" value="1" checked />red
	&nbsp;
	<input type="radio"  name="qp1" value="0" />black
	<br/>
	<input type="button" onclick="SetDeskNumber(1)" value="select" />
	</div>
	{/for}
</div>
</html>