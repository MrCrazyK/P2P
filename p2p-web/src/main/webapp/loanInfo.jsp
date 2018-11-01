<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>动力金融网-CFCA认证的互联网金融公司</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/trafficStatistics.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/share.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css"/>

</head>

<body>
<div id="header">
    <jsp:include page="commons/header.jsp"/>
</div>

<!--散标投资 begin-->
<div id="sbtz" class="invest-details">

    <!--页中begin-->
    <div class="mainBox pro-details-body">
        <div class="homeWap clearfix" id="huacengPar">
            <div class="pro-details-left">

                <!-- 产品详情start -->
                <div class="pro-info-details">
                    <div class="pro-name">
                        <h2><span>${loanInfo.productName}</span>(${loanInfo.productNo}期期)</h2>
                    </div>
                    <div class="pro-info">
                        <ul class="clearfix">
                            <li class="info-1">
                                <p>历史年化利率</p>
                                <h3>${loanInfo.rate}<span>%</span></h3>
                                <div class="info-bt">
                                    <span>本产品采用普通利率</span>
                                </div>
                            </li>
                            <li class="info-2">
                                <p>募集金额(元)</p>
                                <h3>${loanInfo.productMoney}</h3>
                                <div class="info-bt">
              	<span>
                    <c:choose>
                        <c:when test="${loanInfo.leftProductMoney == 0}">
                            已满标
                        </c:when>
                        <c:otherwise>
                            <!-- 只有状态为0时为募集，其它都为已满标 -->
                            募集中 | 剩余募集金额 ${loanInfo.leftProductMoney}元
                        </c:otherwise>
                    </c:choose>
              	</span>
                                </div>
                            </li>
                            <li class="info-3">
                                <p>投资周期</p>
                                <!-- 只有新手宝产品周期为天 -->
                                <c:choose>
                                    <c:when test="${loanInfo.productType eq 0}">
                                        <h3>${loanInfo.cycle}<span>天</span></h3>
                                    </c:when>
                                    <c:otherwise>
                                        <h3>${loanInfo.cycle}<span>个月</span></h3>
                                    </c:otherwise>
                                </c:choose>
                                <div class="info-bt"><span></span></div>
                            </li>
                        </ul>
                    </div>
                    <dl class="pro-syfs">
                        <dt><span>收益获取方式</span></dt>
                        <dd><span>收益返还：</span>到期还本付息</dd>
                    </dl>
                </div>
                <!-- 产品详情end -->

                <!-- 投资记录start -->
                <div class="pro-details-cnt">
                    <ul class="tabNav clearfix">
                        <li><a id="one3" href="javascript:void(0);" class="s">投资记录</a></li>
                    </ul>

                    <div class="invest-record" id="con_one_3" style="display:block">
                        <div class="vertical-side">投资列表</div>
                        <dl class="record-list">
                            <dt>
                                <span class="record-num">序号</span>
                                <span class="invest-user">投资人</span>
                                <span class="invest-money">投资金额(元)</span>
                                <span class="invest-time">投资时间</span>
                            </dt>
                            <c:choose>
                                <c:when test="${empty bidInfoList}">
                                    <!-- 如果投资记录为空，显示以下文字 -->
                                    <dd style="text-align:center;">该产品暂时还没有人投资，赶快去投资吧~</dd>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${bidInfoList}" var="bidInfo" varStatus="index">
                                        <!-- 如果有投资记录，循环遍历显示 -->
                                        <dd>
                                            <span class="record-num">${index.count}</span>
                                            <span class="invest-user">${fn:substring(bidInfo.user.phone,0,3)}******${fn:substring(bidInfo.user.phone,9,11)}</span>
                                            <span class="invest-money">${bidInfo.bidMoney}</span>
                                            <span class="invest-time"><fmt:formatDate value="${bidInfo.bidTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </dd>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </dl>
                    </div>
                </div>
                <!-- 投资记录end -->
            </div>

            <!--页面右侧begin-->
            <div class="pro-details-right">
                <div class="right-calculator" id="huaceng">
                    <div class="calculator-cnt">
                        <h2>立即投资</h2>
                        <dl class="profits-mode">
                            <dt>收益获取方式</dt>
                            <dd class="clearfix"><span id="fanhuan"><em>到期还本付息</em></span></dd>
                        </dl>
                        <dl class="usable">
                            <dt>我的账户可用</dt>
                            <dd>资金(元)：

                                <c:choose>
                                    <c:when test="${empty user}">
                                        <!-- 判断用户是否登录：未登录，显示登录连接 -->
                                        <span style="font-size:18px;color:#ff6161;vertical-align:bottom;"><a
                                                href="${pageContext.request.contextPath}/login.jsp">请登录</a></span>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- 判断用户是否登录：已登录，显示可用余额 -->
                                         <span style="font-size:18px;color:#ff6161;vertical-align:bottom;">${financeAccount.availableMoney}</span>
                                    </c:otherwise>
                                </c:choose>
                            </dd>
                        </dl>
                        <div class="expect-box">
                            <div class="expect-money">预计本息收入(元)：<span id="shouyi" class="money"></span><span
                                    class="prompt" style="display:block;">请在下方输入投资金额</span></div>
                            <input type="text" id="bidMoney" name="bidMoney" onblur="checkMoney();"
                                   placeholder="请输入投资金额，应为100元的整倍数" maxlength="9"/>
                            <div class="max-invest-money"></div>
                        </div>
                        <div class="invest-btn">
                            <a id="investNow" href="javascript:void(0)" class="btn-1" onclick="invest();">立即投资</a>
                        </div>
                        <input type="hidden" id="loanId" name="loanId" value="${loanInfo.id}"/>
                    </div>
                </div>
            </div>
            <!--页面右侧end-->
        </div>
    </div>
    <!--页中end-->

</div>
<!--散标投资 end-->

<!--遮罩层-->
<div class="dialog-overlay" id="dialog-overlay1" style="display:none;"></div>

<!--投资成功浮层start-->
<div class="layer-body failureSuccess failurePayment" id="failurePayment"
     style="display:none;width:500px;height:100px;top:75%;">
    <a class="layer-close" href="javascript:closeit();"></a>
    <div style="background:#f2f2f2; line-height:105px;text-align:center;"><font style="font-size:25px;">投资成功</font>
    </div>
</div>
<!--投资成功浮层end-->

<!--页脚start-->
<jsp:include page="commons/footer.jsp"/>
<!--页脚end-->

<script type="text/javascript">
    var contextPath = "${pageContext.request.contextPath}";

    function closeit() {
        $("#failurePayment").hide();
        $("#dialog-overlay1").hide();
        window.location.href = "${pageContext.request.contextPath}/loan/myCenter";
    }

    //验证投资金额
    function checkMoney() {
        var bidMoney = $("#bidMoney").val();
        var rate = ${loanInfo.rate};
        var productType = "${loanInfo.productType}";
        var cycle = "${loanInfo.cycle}";

        //投资金额不能为空
        if("" == bidMoney) {
            $(".max-invest-money").html("");
            $(".max-invest-money").html("投资金额不能为空");
            return false;
        } else if(isNaN(bidMoney)) {
            //投资金额只支持数字
            $(".max-invest-money").html("");
            $(".max-invest-money").html("投资金额只支持数字");
            return false;
        } else if(bidMoney <= 0) {
            //投资金额必须大于0
            $(".max-invest-money").html("");
            $(".max-invest-money").html("投资金额必须大于0");
            return false;
        } else if(bidMoney % 100 != 0) {
            //投资金额必须为100的整数倍
            $(".max-invest-money").html("");
            $(".max-invest-money").html("投资金额必须为100的整数倍");
            return false;
        } else {
            $(".max-invest-money").html("");
            //计算收益 = 投资金额 * 利率
            //收益 = 投资金额 * 天利率 * 天数
            //产品根据投资时间单位划分为：    天：新手宝，月：优选和散标
            var incomeMoney = "";
            if(0 == productType) {
                //新手宝
                incomeMoney = bidMoney * (rate / 100 / 365) * cycle;
                incomeMoney = Math.round(incomeMoney * Math.pow(10,2)) / Math.pow(10,2);
            } else {
                //优选和散标
                incomeMoney = bidMoney * rate / 100 / 265 * cycle * 30;
                incomeMoney = Math.round(incomeMoney * Math.pow(10,2)) / Math.pow(10,2);
            }
            $("#shouyi").html(incomeMoney);
        }
        return true;
    }

    //用户投资
    function invest() {
        if(checkMoney()) {
            //判断用户是否登录
            var user = "${user}";
            var name = "${user.name}";

            var bidMoney = $("#bidMoney").val();
            var bidMinLimit = "${loanInfo.bidMinLimit}";
            var bidMaxLimit = "${loanInfo.bidMaxLimit}";
            var leftProductMoney = "${loanInfo.leftProductMoney}";

            var availableMoney = "${financeAccount.availableMoney}";
            var loanId = "${loanInfo.id}";

            if(user == "") {
                if (confirm("您尚未登录，请登录")) {
                    window.location.href = contextPath + "/login.jsp";
                }
                return false;
            } else if("" == name) {
                //判断用户是否实名认证
                if(confirm("您尚未实名认证，请认证")) {
                    window.location.href = contextPath + "/realName.jsp";
                }
                return false;
            } else if(parseFloat(bidMoney) < parseFloat(bidMinLimit)) {
                //判断投资金额是否小于最小投资金额
                $(".max-invest-money").html("");
                $(".max-invest-money").html("投资金额过低，请投高于" + bidMinLimit);
                return false;
            } else if(parseFloat(bidMoney) > parseFloat(bidMaxLimit)) {
                //判断投资金额是否超过单笔投资金额
                $(".max-invest-money").html("");
                $(".max-invest-money").html("投资金额过高，请投低于" + bidMaxLimit);
                return false;
            } else if(parseFloat(bidMoney) > parseFloat(leftProductMoney)) {
                //判断投资金额是否超过剩余可投金额
                //我认为此处应该发送ajax查询可投金额
                $(".max-invest-money").html("");
                $(".max-invest-money").html("剩余可投金额没有这么多喽");
                return false;
            } else if(parseFloat(availableMoney) < parseFloat(bidMoney)) {
                //判断投资金额是否超过账户余额
                $(".max-invest-money").html("");
                $(".max-invest-money").html("您的账户没有这么多钱喽，请充值");
                return false;
            } else {
                $(".max-invest-money").html("");

                $.ajax({
                    url :  contextPath + "/loan/invest",
                    type : "post",
                    data : "loanId=" + loanId + "&bidMoney=" + bidMoney,
                    success : function(jsonObject) {
                        if(jsonObject.errorMessage == "OK") {
                            $("#failurePayment").show();
                            $("#dialog-overlay1").show();
                        } else {
                            $(".max-invest-money").html("");
                            $(".max-invest-money").html("投资人数太多了，请稍后重试...");
                        }
                    },
                    error : function() {
                        $(".max-invest-money").html("");
                        $(".max-invest-money").html("投资人数太多了，请稍后重试...");
                    }
                });
            }
        }
    }
</script>
</body>
</html>