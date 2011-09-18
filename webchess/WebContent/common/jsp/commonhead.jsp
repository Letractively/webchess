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
<script type="text/javascript" src="<%=webRoot%>/common/js/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="<%=webRoot%>/common/js/template.js"></script>
<script type="text/javascript" src="<%=webRoot%>/common/js/drawing.js"></script>
<script type="text/javascript">
<!--
window.config = {};
config.webroot = "<%=webRoot%>";
config.imgroot = "<%=webRoot%>"+"/images";
//-->
</script>
