<%! String webRoot=null; %>
<% 
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", 0); 
webRoot = request.getRequestURL().toString().replace(request.getServletPath(), "");
%> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<META HTTP-EQUIV="Expires" CONTENT="0"> 
<link rel="stylesheet" href="<%=webRoot%>/common/css/nyroModal.css" type="text/css" media="screen" />
<script type="text/javascript" src="<%=webRoot%>/common/js/jquery-1.6.3.min.js"></script>
<script type="text/javascript" src="<%=webRoot%>/common/js/jquery.spritely-0.6.js"></script>
<script type="text/javascript" src="<%=webRoot%>/common/js/jquery.nyroModal.custom.js"></script>
<script type="text/javascript" src="<%=webRoot%>/common/js/jquery.nyroModal-ie6.min.js"></script>
<script type="text/javascript" src="<%=webRoot%>/common/js/template.js"></script>
<script type="text/javascript" src="<%=webRoot%>/common/js/drawing.js"></script>
<script type="text/javascript" src="<%=webRoot%>/common/js/config.js"></script>
<script type="text/javascript">
<!--
config.webroot = "<%=webRoot%>";
config.imgroot = "<%=webRoot%>"+"/images";
config.isVsComputer = false;
config.isComputerAuto = false;
//-->
</script>
