<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/top.jsp"%>
</head>
<body class="no-skin">
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
								<form action="deliverer/${msg }.do" name="delivererForm" id="delivererForm" method="post">
								    <input type="hidden" name="DELIVERER_ID" id="deliverer_id" value="${pd.DELIVERER_ID }"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">账号:</td>
											<td><input type="text" name="DELIVERER_ACCOUNT" id="deliverer_account" value="${pd.DELIVERER_ACCOUNT }" maxlength="32" placeholder="这里输入账号" title="账号"  style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">姓名:</td>
											<td><input type="text" name="DELIVERER_CNNAME" id="deliverer_cnname" value="${pd.DELIVERER_CNNAME }" maxlength="32" placeholder="这里输入姓名" title="姓名"  style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">身份证后六位:</td>
											<td><input type="text" name="DELIVERER_ID_CARD" id="deliverer_id_card" value="${pd.DELIVERER_ID_CARD }" maxlength="32" placeholder="这里输入身份证后六位" title="身份证后六位"  style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">保证金(￥):</td>
											<td><input type="NUMBER" name="DELIVERER_CREDIT" id="deliverer_credit" value="${pd.DELIVERER_CREDIT }" maxlength="32" placeholder="这里输入保证金" title="保证金"  style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">年龄:</td>
											<td><input type="NUMBER" name="DELIVERER_AGE" id="deliverer_age" value="${pd.DELIVERER_AGE }" maxlength="32" placeholder="这里输入年龄" title="年龄"  style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">性别:</td>
											<td>
                                               <select name="DELIVERER_GENDER" id="deliverer_gender" style="width:98%;" maxlength="32">
												 <option value="0">男</option>
												  <option value="1">女</option>
											   </select> 											
											</td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">账户状态:</td>
											<td>
											<select name="DELIVERER_STATUS" id="deliverer_status" style="width:98%;">
											<option value="0">正常</option>
											<option value ="1">冻结</option>
											<option value ="2">关闭</option>
											</select>
											</td>
										</tr>
										
										<tr>
											<td style="text-align: center;" colspan="10">
												<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
												<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
											</td>
										</tr>
									</table>
									</div>
									<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
								</form>
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->
	</div>
	
	
	
	
	
	<!-- /.main-container -->
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/js/ACalendar.js"></script>
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- inline scripts related to this page -->
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript">
$(top.hangge());
	//保存
	function save(){
	   if($("#deliverer_account").val()=="" ||$("#deliverer_account").val()=="此账号已存在!"){
			$("#deliverer_account").tips({
				side:3,
	            msg:'输入配送员账号',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#deliverer_account").focus();
			return false;
		}
	   var myreg = /^(((13[0-9]{1})|159)+\d{8})$/;
		if($("#deliverer_account").val()==""){
			
			$("#deliverer_account").tips({
				side:3,
	         msg:'填写商户账号手机号',
	         bg:'#AE81FF',
	         time:3
	     });
			$("#deliverer_account").focus();
			return false;
		}else if($("#deliverer_account").val().length != 11 && !myreg.test($("#deliverer_account").val())){
			$("#deliverer_account").tips({
				side:3,
	         msg:'手机号格式不正确',
	         bg:'#AE81FF',
	         time:3
	     });
			$("#deliverer_account").focus();
			return false;
		}
	   
		if($("#deliverer_cnname").val()==""){
			$("#deliverer_cnname").tips({
				side:3,
	            msg:'输入配送员姓名',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#deliverer_cnname").focus();
			return false;
		}
		
		if($("#deliverer_id_card").val()==""){
			$("#deliverer_id_card").tips({
				side:3,
	            msg:'输入身份证后六位',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#deliverer_id_card").focus();
			return false;
		}
		if($("#deliverer_id_card").val().length != 6){
			$("#deliverer_id_card").tips({
				side:3,
	            msg:'输入正确的身份证后六位',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#deliverer_id_card").focus();
			return false;
		}
		if($("#deliverer_credit").val()==""){
			$("#deliverer_credit").tips({
				side:3,
	            msg:'输入保证金',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#deliverer_credit").focus();
			return false;
		}
		if($("#deliverer_credit").val() < 0){
			$("#deliverer_credit").tips({
				side:3,
	            msg:'保证金非负数',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#deliverer_credit").focus();
			return false;
		}
		if($("#deliverer_age").val()==""){
			$("#deliverer_age").tips({
				side:3,
	            msg:'输入年龄',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#deliverer_age").focus();
			return false;
		}
		if($("#deliverer_age").val() < 0){
			$("#deliverer_age").tips({
				side:3,
	            msg:'年龄非负数',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#deliverer_age").focus();
			return false;
		}
		hasAccount();
		/*$("#delivererForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();*/
		
	} 
	
	//判断IMEI号是否存在
	function hasAccount(){
		var account = $.trim($("#deliverer_account").val());
		$.ajax({
			type: "POST",
			url: '<%=basePath%>deliverer/findByAccount.do',
	    	data: {DELIVERER_ACCOUNT:account,tm:new Date().getTime()},
			dataType:'json',
			cache: false,
			success: function(data){
				 if("success" == data.result){
					$("#delivererForm").submit();
					$("#zhongxin").hide();
					$("#zhongxin2").show();
				 }else{
					$("#deliverer_account").css("background-color","#D16E6C");
					setTimeout("$('#deliverer_account').val('此账号已存在!')",500);
				 }
			}
		});
	}

	
</script>
</html>