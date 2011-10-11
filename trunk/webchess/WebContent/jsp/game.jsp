<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../common/jsp/commonhead.jsp"></jsp:include>
<title>new game</title>
<script type="text/javascript">
$(function(){
	var ajaxing = false;
	function newgame(save){
		if(ajaxing)return;
		ajaxing = true;
		$.ajax({
			type:"POST",
			cache:false,
			url:config.webroot+"/HandleCommon",
			data:(function(){
				return {"type":save,
						"room":top.config.room
					};
			})(),
			success:function(data, textStatus){			
				//eval("var json="+data);
				//if(json.ok)
				{
					
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("try again");
			},
			complete:function(xhr,status){
				ajaxing =false;
				$.nmTop().close();
			}
		});
	}
	$("#save").click(function(){
		newgame(true);
	});
	$("#notsave").click(function(){
		newgame(false);
	});
});
</script>
</head>
<body>
<input type="button" value="new game with saveing old" id="save"/>
<input type="button" value="new game without saveing old" id="notsave"/>
</body>
</html>