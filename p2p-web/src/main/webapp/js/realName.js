
//验证真实姓名
function checkRealName() {
	//获取用户真实姓名
	var realName = $.trim($("#realName").val());

	//真实姓名不能为空
	if(realName == "") {
		showError("realName","请输入真实姓名");
		return false;
	} else if(!/^[\u4e00-\u9fa5]{0,}$/.test(realName)) {
        //真实姓名只支持中文
		showError("realName","真实姓名只支持中文");
		return false;
	} else {
		showSuccess("realName");
	}
	return true;
}

//验证身份证号码
function checkIdCard() {
	//获取用户身份证号码
	var idCard = $.trim($("#idCard").val());
	var replayIdCard = $.trim($("#replayIdCard").val());

	if(idCard == "") {
		showError("idCard","请输入身份证号码");
		return false;
	} else if(!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)) {
		showError("idCard","请输入正确的身份证号码");
		return false;
	} else {
		showSuccess("idCard");
	}

	if(replayIdCard != idCard) {
		showError("replayIdCard","请在此输入确认身份证号码");
		return false;
	}

	return true;

}

//验证确认身份证号
function checkIdCardEqu() {
	//获取确认身份号
	var replayIdCard = $.trim($("#replayIdCard").val());
	var idCard = $.trim($("#idCard").val());

	//不能为空
	if("" == replayIdCard) {
		showError("replayIdCard","请输入确认身份照号");
		return false;
	} else if(replayIdCard != idCard){
        //符合规则//和身份证号相等
		showError("replayIdCard","两次输入身份证号码不一致");
		return false;
	} else {
		showSuccess("replayIdCard");
	}
    return true;
}

//图形验证码
function checkCaptcha() {
	var captcha = $.trim($("#captcha").val());
	var flag = true;

	if(captcha == "") {
		showError("captcha","请输入图形验证码");
		return false;
	} else {
		$.ajax({
            url : "loan/checkCaptcha",
            type : "post",
            data : "captcha=" + captcha,
			async : false,
            success: function (jsonObject) {
					if (jsonObject.errorMessage == "OK") {
						showSuccess("captcha");
						flag = true;
						} else {
						showError("captcha",jsonObject.errorMessage);
						flag = false;
					}
				},
            error: function () {
				showError("captcha","系统繁忙，请稍后重试...");
				flag = false;
            }
        });
	}
	if(!flag) {
		return false;
	}
	return true;
}

//实名认证
function verifyRealName() {
	//获取用户实名认证参数
	var realName = $.trim($("#realName").val());
	var idCard = $.trim($("#idCard").val());
	var replarIdCard = $.trim($("#replayIdCard"));

	if(checkRealName() && checkIdCard() && checkIdCardEqu()) {
		$.ajax({
			url : "loan/verifyRealName",
			type : "post",
			data : {
				realName : realName,
				idCard : idCard,
				replayIdCard : replarIdCard
			},
			success : function(jsonObject) {
				if(jsonObject.errorMessage == "OK") {
					//重定向至用户中心
					window.location.href = "index";
				} else {
					showError("captcha",jsonObject.errorMessage);
				}
			},
			error : function() {
				showError("captcha","实名认证人数过多，请稍后重试...");
			}
		});
	}
}

//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}

//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}

//成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}

//实名认证提交
function realName () {
	
	var idCard = $.trim($("#idCard").val());
	var replayIdCard = $.trim($("#replayIdCard").val());//确认身份证号
	var realName = $.trim($("#realName").val());
	var captcha = $.trim($("#captcha").val());
	
	if(userRealName(0) && idCardEequ() && checkCaptcha() && idCardCheck()) {
		$.ajax({
			type:"POST",
			url:"loan/checkRealName",
			dataType: "json",
			async: false,
			data:"realName="+realName+"&idCard="+idCard+"&replayIdCard="+replayIdCard+"&captcha="+captcha,
			success: function(retMap) {
				if (retMap.errorMessage == "ok") {
					window.location.href = "loan/showMyCenter";
				} else {
					showError('captcha', retMap.errorMessage);
				}
			},
		    error:function() {
				 showError('captcha','网络错误');
				 rtn = false;
			}
		});
	}
}
//同意实名认证协议
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
});
//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}