<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
<style>
body {
	margin: 0px;
	background-image: none;
	position: static;
	left: auto;
	width: 603px;
	margin-left: 0;
	margin-right: 0;
	text-align: left;
}

#base {
	position: absolute;
	z-index: 0;
}

#u0_div {
	position: absolute;
	left: 0px;
	top: 0px;
	width: 475px;
	height: 298px;
	background: inherit;
	background-color: rgba(204, 255, 204, 1);
	border: none;
	border-radius: 0px;
	-moz-box-shadow: none;
	-webkit-box-shadow: none;
	box-shadow: none;
}

#u0 {
	position: absolute;
	left: 128px;
	top: 88px;
	width: 475px;
	height: 298px;
}

#u1 {
	position: absolute;
	left: 2px;
	top: 141px;
	width: 471px;
	visibility: hidden;
	word-wrap: break-word;
}

#u2_div {
	position: absolute;
	left: 0px;
	top: 0px;
	width: 81px;
	height: 21px;
	background: inherit;
	background-color: rgba(204, 255, 204, 1);
	border: none;
	border-radius: 0px;
	-moz-box-shadow: none;
	-webkit-box-shadow: none;
	box-shadow: none;
	font-family: 'Arial Negreta cursiva', 'Arial';
	font-weight: 700;
	font-style: italic;
	text-decoration: underline;
}

#u2 {
	position: absolute;
	left: 135px;
	top: 98px;
	width: 81px;
	height: 21px;
	font-family: 'Arial Negreta cursiva', 'Arial';
	font-weight: 700;
	font-style: italic;
	text-decoration: underline;
}

#u3 {
	position: absolute;
	left: 2px;
	top: 2px;
	width: 77px;
	word-wrap: break-word;
}

#u4_div {
	position: absolute;
	left: 0px;
	top: 0px;
	width: 68px;
	height: 22px;
	background: inherit;
	background-color: rgba(204, 255, 204, 1);
	border: none;
	border-radius: 0px;
	-moz-box-shadow: none;
	-webkit-box-shadow: none;
	box-shadow: none;
}

#u4 {
	position: absolute;
	left: 203px;
	top: 155px;
	width: 68px;
	height: 22px;
}

#u5 {
	position: absolute;
	left: 2px;
	top: 3px;
	width: 64px;
	word-wrap: break-word;
}

#u6_div {
	position: absolute;
	left: 0px;
	top: 0px;
	width: 68px;
	height: 23px;
	background: inherit;
	background-color: rgba(204, 255, 204, 1);
	border: none;
	border-radius: 0px;
	-moz-box-shadow: none;
	-webkit-box-shadow: none;
	box-shadow: none;
}

#u6 {
	position: absolute;
	left: 203px;
	top: 187px;
	width: 68px;
	height: 23px;
}

#u7 {
	position: absolute;
	left: 2px;
	top: 4px;
	width: 64px;
	word-wrap: break-word;
}

#u8 {
	position: absolute;
	left: 259px;
	top: 155px;
	width: 173px;
	height: 25px;
}

#u8_input {
	position: absolute;
	left: 0px;
	top: 0px;
	width: 173px;
	height: 25px;
	font-family: 'Arial Normal', 'Arial';
	font-weight: 400;
	font-style: normal;
	font-size: 13px;
	text-decoration: none;
	color: #000000;
	text-align: left;
}

#u9 {
	position: absolute;
	left: 259px;
	top: 190px;
	width: 173px;
	height: 25px;
}

#u9_input {
	position: absolute;
	left: 0px;
	top: 0px;
	width: 173px;
	height: 25px;
	font-family: 'Arial Normal', 'Arial';
	font-weight: 400;
	font-style: normal;
	font-size: 13px;
	text-decoration: none;
	color: #000000;
	text-align: left;
}

#u10_div {
	position: absolute;
	left: 0px;
	top: 0px;
	width: 150px;
	height: 25px;
	background: inherit;
	background-color: rgba(204, 255, 204, 1);
	border: none;
	border-radius: 0px;
	-moz-box-shadow: none;
	-webkit-box-shadow: none;
	box-shadow: none;
	color: #FF0000;
}

#u10 {
	position: absolute;
	left: 442px;
	top: 155px;
	width: 150px;
	height: 25px;
	color: #FF0000;
}

#u11 {
	position: absolute;
	left: 2px;
	top: 4px;
	width: 146px;
	word-wrap: break-word;
}

#u12_div {
	position: absolute;
	left: 0px;
	top: 0px;
	width: 150px;
	height: 25px;
	background: inherit;
	background-color: rgba(204, 255, 204, 1);
	border: none;
	border-radius: 0px;
	-moz-box-shadow: none;
	-webkit-box-shadow: none;
	box-shadow: none;
	color: #FF0000;
}

#u12 {
	position: absolute;
	left: 442px;
	top: 190px;
	width: 150px;
	height: 25px;
	color: #FF0000;
}

#u13 {
	position: absolute;
	left: 2px;
	top: 4px;
	width: 146px;
	word-wrap: break-word;
}

#u14 {
	position: absolute;
	left: 354px;
	top: 251px;
	width: 78px;
	height: 25px;
}

#u14_input {
	position: absolute;
	left: 0px;
	top: 0px;
	width: 78px;
	height: 25px;
	background-color: transparent;
	font-family: 'Arial Normal', 'Arial';
	font-weight: 400;
	font-style: normal;
	font-size: 13px;
	text-decoration: none;
	color: #333333;
	text-align: center;
}

/* so the window resize fires within a frame in IE7 */
html, body {
	height: 100%;
}

a {
	color: inherit;
}

p {
	margin: 0px;
	text-rendering: optimizeLegibility;
	font-feature-settings: "kern" 1;
	-webkit-font-feature-settings: "kern";
	-moz-font-feature-settings: "kern";
	-moz-font-feature-settings: "kern=1";
	font-kerning: normal;
}

iframe {
	background: #FFFFFF;
}

/* to match IE with C, FF */
input {
	padding: 1px 0px 1px 0px;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
}

textarea {
	margin: 0px;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
}

div.intcases {
	font-family: arial;
	font-size: 12px;
	text-align: left;
	border: 1px solid #AAA;
	background: #FFF none repeat scroll 0% 0%;
	z-index: 9999;
	visibility: hidden;
	position: absolute;
	padding: 0px;
	border-radius: 3px;
	white-space: nowrap;
}

div.intcaselink {
	cursor: pointer;
	padding: 3px 8px 3px 8px;
	margin: 5px;
	background: #EEE none repeat scroll 0% 0%;
	border: 1px solid #AAA;
	border-radius: 3px;
}

div.refpageimage {
	position: absolute;
	left: 0px;
	top: 0px;
	font-size: 0px;
	width: 16px;
	height: 16px;
	cursor: pointer;
	background-image: url(images/newwindow.gif);
	background-repeat: no-repeat;
}

div.annnoteimage {
	position: absolute;
	left: 0px;
	top: 0px;
	font-size: 0px;
	/*width: 16px;
    height: 12px;*/
	cursor: help;
	/*background-image: url(images/note.gif);*/
	/*background-repeat: no-repeat;*/
	width: 13px;
	height: 12px;
	padding-top: 1px;
	text-align: center;
	background-color: #138CDD;
	-moz-box-shadow: 1px 1px 3px #aaa;
	-webkit-box-shadow: 1px 1px 3px #aaa;
	box-shadow: 1px 1px 3px #aaa;
}

div.annnoteline {
	display: inline-block;
	width: 9px;
	height: 1px;
	border-bottom: 1px solid white;
	margin-top: 1px;
}

div.annnotelabel {
	position: absolute;
	left: 0px;
	top: 0px;
	font-family: Helvetica, Arial;
	font-size: 10px;
	/*border: 1px solid rgb(166,221,242);*/
	cursor: help;
	/*background:rgb(0,157,217) none repeat scroll 0% 0%;*/
	padding: 1px 3px 1px 3px;
	white-space: nowrap;
	color: white;
	background-color: #138CDD;
	-moz-box-shadow: 1px 1px 3px #aaa;
	-webkit-box-shadow: 1px 1px 3px #aaa;
	box-shadow: 1px 1px 3px #aaa;
}

.annotation {
	font-size: 12px;
	padding-left: 2px;
	margin-bottom: 5px;
}

.annotationName {
	/*font-size: 13px;
    font-weight: bold;
    margin-bottom: 3px;
    white-space: nowrap;*/
	font-family: 'Trebuchet MS';
	font-size: 14px;
	font-weight: bold;
	margin-bottom: 5px;
	white-space: nowrap;
}

.annotationValue {
	font-family: Arial, Helvetica, Sans-Serif;
	font-size: 12px;
	color: #4a4a4a;
	line-height: 21px;
	margin-bottom: 20px;
}

.noteLink {
	text-decoration: inherit;
	color: inherit;
}

.noteLink:hover {
	background-color: white;
}

/* this is a fix for the issue where dialogs jump around and takes the text-align from the body */
.dialogFix {
	position: absolute;
	text-align: left;
	border: 1px solid #8f949a;
}

@
keyframes pulsate {from { box-shadow:0010px#15d6ba;
	
}

to {
	box-shadow: 0 0 20px #15d6ba;
}

}
@
-webkit-keyframes pulsate {from { -webkit-box-shadow:0010px#15d6ba;
	box-shadow: 0 0 10px #15d6ba;
}

to {
	-webkit-box-shadow: 0 0 20px #15d6ba;
	box-shadow: 0 0 20px #15d6ba;
}

}
@
-moz-keyframes pulsate {from { -moz-box-shadow:0010px#15d6ba;
	box-shadow: 0 0 10px #15d6ba;
}

to {
	-moz-box-shadow: 0 0 20px #15d6ba;
	box-shadow: 0 0 20px #15d6ba;
}

}
.legacyPulsateBorder {
	/*border: 5px solid #15d6ba;
    margin: -5px;*/
	-moz-box-shadow: 0 0 10px 3px #15d6ba;
	box-shadow: 0 0 10px 3px #15d6ba;
}

.pulsateBorder {
	animation-name: pulsate;
	animation-timing-function: ease-in-out;
	animation-duration: 0.9s;
	animation-iteration-count: infinite;
	animation-direction: alternate;
	-webkit-animation-name: pulsate;
	-webkit-animation-timing-function: ease-in-out;
	-webkit-animation-duration: 0.9s;
	-webkit-animation-iteration-count: infinite;
	-webkit-animation-direction: alternate;
	-moz-animation-name: pulsate;
	-moz-animation-timing-function: ease-in-out;
	-moz-animation-duration: 0.9s;
	-moz-animation-iteration-count: infinite;
	-moz-animation-direction: alternate;
}

.ax_default_hidden, .ax_default_unplaced {
	display: none;
	visibility: hidden;
}

.widgetNoteSelected {
	-moz-box-shadow: 0 0 10px 3px #138CDD;
	box-shadow: 0 0 10px 3px #138CDD;
	/*-moz-box-shadow: 0 0 20px #3915d6;
    box-shadow: 0 0 20px #3915d6;*/
	/*border: 3px solid #3915d6;*/
	/*margin: -3px;*/
}

.singleImg {
	display: none;
	visibility: hidden;
}

.uu {
	visibility: visible;
}
</style>
</head>
<body>
	<div id="base" class="">

		<!-- Unnamed (矩形) -->
		<div id="u0" class="ax_default box_1">
			<div id="u0_div" class=""></div>
			<!-- Unnamed () -->
			<div id="u1" class="text" style="display: none; visibility: hidden">
				<p>
					<span></span>
				</p>
			</div>
		</div>

		<!-- Unnamed (矩形) -->
		<div id="u2" class="ax_default box_1">
			<div id="u2_div" class=""></div>
			<!-- Unnamed () -->
			<div id="u3" class="text" style="visibility: visible;">
				<p>
					<span style="text-decoration: underline;">用户登录</span>
				</p>
			</div>
		</div>

		<!-- Unnamed (矩形) -->
		<div id="u4" class="ax_default box_1">
			<div id="u4_div" class=""></div>
			<!-- Unnamed () -->
			<div id="u5" class="text" style="visibility: visible;">
				<p>
					<span>账号：</span>
				</p>
			</div>
		</div>

		<!-- Unnamed (矩形) -->
		<div id="u6" class="ax_default box_1">
			<div id="u6_div" class=""></div>
			<!-- Unnamed () -->
			<div id="u7" class="text" style="visibility: visible;">
				<p>
					<span>密码：</span>
				</p>
			</div>
		</div>

		<!-- Unnamed (文本框) -->
		<div id="u8" class="ax_default text_field">
			<input id="account" type="text" value="" />
		</div>

		<!-- Unnamed (文本框) -->
		<div id="u9" class="ax_default text_field">
			<input id="password" type="password" value="" />
		</div>

		<!-- Unnamed (矩形) -->
		<div id="u10" class="ax_default box_1">
			<div id="u10_div" class=""></div>
			<!-- Unnamed () -->
			<div id="u11" class="text" style="visibility: visible;">
				<p>
					<span id="errorAccount"></span>
				</p>
			</div>
		</div>

		<!-- Unnamed (矩形) -->
		<div id="u12" class="ax_default box_1">
			<div id="u12_div" class=""></div>
			<!-- Unnamed () -->
			<div id="u13" class="text" style="visibility: visible;">
				<p>
					<span></span>
				</p>
			</div>
		</div>

		<!-- Unnamed (提交按钮) -->
		<div id="u14" class="ax_default html_button">
			<input id="u14_input" type="submit" value="提交" />
		</div>
	</div>
</body>
<script type="text/javascript">

$(function(){

	function text(){
		var isTrue=false;
		var account=$("#account").val();
		  $.ajax({
			  url:"<%=request.getContextPath()%>/manager/user/checkAccount",
				type : "POST",
				data:"{\"account\":\""+account+"\"}",
				contentType:"application/json; charset=utf-8",
				async: false,
				success : function(html) {
					var data = eval(html);
					console.log(html);
					console.log(data);
					if (data.retCode == "0") {
						isTrue=true;
					}
				}
			});
		return isTrue;
		}
		$("#account").change(function() {
			if (!text()) {
				$("#errorAccount").text("*该账号不存在");
			}else{
				$("#errorAccount").text("");
			}
		});
		$("#u14_input").click(function() {
			if(text()){
				$("#errorAccount").text("");
				var account=$("#account").val();
				var password=$("#password").val();
				 $.ajax({
						url:"<%=request.getContextPath()%>/manager/logining/login",
						type : "POST",
						data:"{\"account\":\""+account+"\",\"password\":\""+password+"\"}",
						contentType:"application/json; charset=utf-8",
						success : function(html) {
							if(html.retCode=="1"){
								alert(html.data.roleName+html.data.userName+"登录成功！");
							}else{
								alert("密码错误！");
							}
					}
				});
			}else{
				$("#errorAccount").text("*该账号不存在");
			}
		})

	})
</script>
</html>