<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
</head>
<script type="text/javascript">

function formSubmit(){
	var encodeStr=$("#str").val();
	var data='{str:"'+encodeStr+'"}';
	  $.ajax({
		  url:'<%=request.getContextPath()%>/home/encode',
		  type: "POST",
		  data:data,
		  contentType:"application/json; charset=utf-8",
		  success: function(html){
			  $("#encodeStr").val(html);
		  }
	  });
}
function decodeData(){
	//获取加密后的数据
	var encodeStr=$("#encodeStr").val();
	var data='{str:"'+encodeStr+'"}';
	  $.ajax({
		  url:'<%=request.getContextPath()%>/home/decode',
		  type: "POST",
		  data:data,
		  contentType:"application/json; charset=utf-8",
		  success: function(html){
			  $("#decodeStr").val(html);
		  }
	  });
}
</script>
<body>
原始数据：<br><textarea name="str" id="str" rows="5" cols="70" ></textarea><div></div><br>
<a href="javascript:formSubmit();">加 密</a><br/>
<font style="color:#C94028;">加密数据：</font><br>
<textarea  name="encodeStr" id="encodeStr" rows="10" cols="70"></textarea><div></div>
<a href="javascript:decodeData();">解 密</a><br/>
<font style="color:#50C167;">解密数据：</font><br>
<textarea  name="decodeStr" id="decodeStr" rows="5" cols="70"></textarea><div></div><br/>
</body>
</html>