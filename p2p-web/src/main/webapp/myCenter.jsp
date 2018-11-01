<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<title>动力金融网 - 专业的互联网金融信息服务平台</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/trafficStatistics.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/share.css"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css"/>
</head>

<body>
<!-- 页头start -->
<div id="header">
<jsp:include page="commons/header.jsp"/>
</div>
<!--页头end-->

<!-- 二级导航栏start -->
<jsp:include page="commons/subNav.jsp"/>
<!-- 二级导航栏end -->

<div class="mainBox">
    <div class="homeWap">
    	<!--账户信息start-->
        <div class="credenMain clearfix">
            <div class="dog-ear"></div>
            <div class="credenBasic">
                <div class="credenName">
                    <div class="head">
                    <span><img id="user_photo" src="${pageContext.request.contextPath}/images/header.png" width="90" height="90"/></span>
                    <a href="${pageContext.request.contextPath}/loan/uploadHeader">上传头像</a>
                    </div>
                    <h3 class="name">
                        <c:choose>
                            <c:when test="${empty user.name}">
                                <a href="${pageContext.request.contextPath}/realName.jsp">请实名认证</a>
                            </c:when>
                            <c:otherwise>
                                ${user.name}
                            </c:otherwise>
                        </c:choose>
                   		<%-- <a href="${pageContext.request.contextPath}/realName.jsp">请认证姓名</a> --%>
                    </h3>
                    <h4></h4>
                    <h4>${user.phone}</h4>
                    <h4>最近登录：2<fmt:formatDate value="${user.lastLoginTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </h4>
                </div>
                <i class="borderRight"></i>
                 <ul class="credenBalance">
                  <li class="availMoney">
	                  <h3>可用余额：</h3>
	                  <span class="moneyIcon"><i>¥ <c:choose>
                          <c:when test="${empty financeAccount.availableMoney}">0 元</c:when>
                          <c:otherwise>${financeAccount.availableMoney} 元</c:otherwise>
                      </c:choose></i></span>
	                  <a class="recharge" href="${pageContext.request.contextPath}/toRecharge.jsp">充值</a>
	                  <a class="inves" href="${pageContext.request.contextPath}/loan/loan">投资</a>
                  </li>
                 </ul>
            </div>
        </div>
        <!--账户信息end-->
        
        <!--最新操作数据start-->
        <div class="allInvest clearfix">
            <div class="eachInvest invest-tab">
                <h3>最近投资</h3>
                <div class="invest-tab-box">
                 <dl>
                     <dt>
                     	<span class="number" style="text-align:left;">序号</span>
                         <span class="number">投资产品</span>
                         <span class="money">投资金额</span>
                         <span class="profit">投资时间</span>
                     </dt>
                     <c:choose>
                         <c:when test="${not empty bidInfoList}">
                             <c:forEach items="${bidInfoList}" varStatus="seq" var="bidInfo">
                                 <dd>
                                     <span class="name">${seq.count}</span>
                                     <span id="preparedLoanTotalLoanCount" class="number">${bidInfo.loanInfo.productName}</span>
                                     <span id="preparedLoanTotalBidMoney" class="money">${bidInfo.bidMoney}</span>
                                     <span id="preparedLoanTotalIncome" class="profit"><fmt:formatDate value="${bidInfo.bidTime}" pattern="yyyy-MM-dd"/> </span>
                                 </dd>
                             </c:forEach>
                         </c:when>
                     </c:choose>
                 </dl>
                 <div class="more-allinvest">
                     <a href="${pageContext.request.contextPath}/loan/myInvest"><span>查看全部投资</span><i></i></a>
                 </div>
             </div>
            </div>
            <div class="eachInvest invest-tab invest-tab-box">
                <h3>最近充值</h3>
                <dl>
                    <dt>
                        <span class="number" style="text-align:left;">序号</span>
                        <span class="number">充值描述</span>
                        <span class="money">充值金额</span>
                        <span class="money">充值时间</span>
                    </dt>
                    <c:choose>
                        <c:when test="${not empty rechargeRecordList}">
                            <c:forEach items="${rechargeRecordList}" varStatus="index" var="rechargeRecord">
                                <dd>
                                    <span class="name">${index.count}</span>
                                    <span id="preparedLoanStandardTotalLoanCount" class="number">${rechargeRecord.rechargeDesc}</span>
                                    <span id="preparedLoanStandardTotalBidMoney" class="money">${rechargeRecord.rechargeMoney}</span>
                                    <span id="preparedLoanStandardTotalIncome" class="profit"><fmt:formatDate value="${rechargeRecord.rechargeTime}" pattern="yyyy-MM-dd"/> </span>
                                </dd>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                </dl>
                 <div class="more-allinvest">
                    <a href="${pageContext.request.contextPath}/loan/myRecharge"><span>查看全部充值</span><i></i></a>
                </div>
            </div>
            
            <div class="eachInvest recent-earnings">
                <h3>最近收益</h3>
                <dl id="recentearning">
                    <dt>
                    	<span class="number" style="text-align:left;">序号</span>
                        <span class="number">项目名称</span>
                        <span class="number name" style="text-align:center;">收益日期</span>
                        <span class="profit">收益金额</span>
                    </dt>
                    <c:choose>
                        <c:when test="${not empty incomeRecordList}">
                            <c:forEach items="${incomeRecordList}" var="incomeRecord" varStatus="seri">
                                <dd>
                                    <span class="number" style="text-align:left;">${seri.count}</span>
                                    <span id="preparedLoanStandardTotalLoanCount" class="number">${incomeRecord.loanInfo.productName}</span>
                                    <span id="preparedLoanStandardTotalIncome" class="number name" style="text-align:center;"><fmt:formatDate value="${incomeRecord.incomeDate}" pattern="yyyy-MM-dd"/> </span>
                                    <span id="preparedLoanStandardTotalBidMoney" class="profit">${incomeRecord.incomeMoney}</span>

                                </dd>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                </dl>
                <div id="moreRecentIncome" class="more-allinvest">
                    <a href="${pageContext.request.contextPath}/loan/myIncome"><span>查看全部收益计划</span><i></i></a>
                </div>
            </div>
        </div>
        <!--最新操作数据end-->
    </div>
</div>

<!--页脚start-->
<jsp:include page="commons/footer.jsp"/>
<!--页脚end-->
</body>
</html>