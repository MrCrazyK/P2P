var referrer = "";//登录后返回页面

//跳转至当前页面之前页面的url
referrer = document.referrer;


if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
}
//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		login();
	}
});

$(function() {
	loadStat();

});

function  loadStat() {
	$.ajax({
		url : "loan/loanStat",
		type : "get",
		success : function(jsonObject) {
			$(".historicalAverageRate").html(jsonObject.historicalAverageRate);
			$("#userCount").html(jsonObject.userCount);
			$("#bidMoneySum").html(jsonObject.bidMoneySum);
		}

	});

}

function checkPhone() {
    var phone = $.trim($("#phone").val());
    if("" == phone) {
        $("#showId").html("");
        $("#showId").html("请输入手机号码");
    } else if(/^1[1-9]\\d{9}$/.test(phone)){
        $("#showId").html("");
        $("#showId").html("请输入正确的手机号码");
    } else {
        $("#showId").html("");
    }
    return true;
}

function checkLoginPassword() {
	var loginPassword = $.trim($("#loginPassword").val());
	if("" == loginPassword) {
		$("#showId").html("");
		$("#showId").html("请输入密码");
	} else {
		$("#showId").html("");
	}
	return true;
}

function checkCaptcha() {
	var captcha = $.trim($("#captcha").val());
	var flag = true;

	if("" == captcha) {
        $("#showId").html("");
        $("#showId").html("请输入图形验证码");
		return false;
	} else {
		$.ajax({
			url : "loan/checkCaptcha",
			type : "post",
			data : "captcha=" + captcha,
			async : false,
			success : function(jsonObject) {
				if(jsonObject.errorMessage == "OK") {
					$("#showId").html("");
					$("#showId").html(jsonObject.errorMessage);
					flag = true;
				} else {
                    $("#showId").html("");
                    $("#showId").html(jsonObject.errorMessage);
					flag = false;
				}
			},
			error : function() {
				$("#showId").html("");
				$("#showId").html("系统繁忙，请稍后重试...");
				flag = false;
			}
		});
	}
	if(!flag) {
		return false;
	}
	return true;
}

function checkLoginPassword() {
	var loginPassword = $.trim($("#loginPassword").val());
	if("" == loginPassword) {
		$("#showId").html("");
		$("#showId").html("请输入登录密码");
		return false;
	} else {
		$("#showId").html("");
		return true;
	}
}

function login() {
	var phone = $.trim($("#phone").val());
	var loginPassword = $.trim($("#loginPassword").val());

	if(checkPhone() && checkLoginPassword() && checkCaptcha() && checkLoginPassword()) {
		$("#loginPassword").val($.md5(loginPassword));
		$.ajax({
			url : "loan/login",
			type : "post",
			data : "phone=" + phone + "&loginPassword=" + $("#loginPassword").val(),
			success : function(jsonObject) {
				if(jsonObject.errorMessage == "OK") {
					//登录成功
					if(!referrer) {
						window.location.href = "index";
					} else {
						window.location.href = referrer;
					}
				}else {
                    //登录失败
                    $("#showId").html("");
					$("#showId").html(jsonObject.errorMessage);
                }
			},
			error : function() {
				$("#showId").html("");
				$("#showId").html("登录人数过多，请稍后重试...");
			}
		});
	}
}
