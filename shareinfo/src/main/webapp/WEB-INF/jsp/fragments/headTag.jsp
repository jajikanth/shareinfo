<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">

<title>shareinfo</title>
<spring:url value="/webjars/bootstrap/2.3.0/css/bootstrap.min.css" var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />
<spring:url value="/webjars/bootstrap/2.3.0/css/bootstrap-responsive.min.css" var="bootstrapResponsiveCss" />
<link href="${bootstrapResponsiveCss}" rel="stylesheet" />
<spring:url value="/resources/css/common.css" var="commonCss" />
<link href="${commonCss}" rel="stylesheet" />

<spring:url value="/webjars/jquery/1.9.0/jquery.min.js" var="jQuery" />
<script src="${jQuery}"></script>

<spring:url value="/webjars/json2/20110223/json2.min.js" var="json2Min" />
<script src="${json2Min}"></script>

<spring:url value="/webjars/bootstrap/2.3.0/js/bootstrap.min.js" var="bootstrapJs" />
<script src="${bootstrapJs}"></script>


<spring:url value="/resources/js/bootbox.min.js" var="bootBoxJs" />
<script src="${bootBoxJs}"></script>

<spring:url value="/resources/js/bootstrap-maxlength.min.js" var="bootstrapMaxLengthJs" />
<script src="${bootstrapMaxLengthJs}"></script> 

<spring:url value="/resources/js/placeholder.js" var="placeHolderJs" />
<script src="${placeHolderJs}"></script>


 <spring:url value="/webjars/jquery-ui/1.9.2/css/smoothness/jquery-ui-1.9.2.custom.css" var="jQueryUiCss" />
<link href="${jQueryUiCss}" rel="stylesheet"></link> 

<%-- <spring:url value="/webjars/tinymce-jquery/4.0.16/tinymce.min.js" var="editorJs" />
<script src="${editorJs}"></script>
 --%>

 <spring:url value="/webjars/jquery-ui/1.9.2/js/jquery-ui-1.9.2.custom.js" var="jQueryUi" />
<script src="${jQueryUi}"></script> 

<%--
<spring:url value="/resources/components/webcomponentsjs/webcomponents.js" var="webComponents" />
<script src="${webComponents}"></script>

 <spring:url value="/resources/components/paper-button/paper-button.html" var="paperButton" />
<link href="${paperButton}" rel="import" />

<spring:url value="/resources/components/paper-input/paper-input-decorator.html" var="paperButton" />
<link href="${paperInputDecorator}" rel="import" />
 --%>
   
<!-- <script src="http://www.polymer-project.org/components/webcomponentsjs/webcomponents.js"></script>
  <link rel="import" href="http://www.polymer-project.org/components/paper-input/paper-input.html">
 <link href="http://www.polymer-project.org/components/paper-button/paper-button.html" rel="import">
 <link href="http://www.polymer-project.org/components/paper-input/paper-input-decorator.html" rel="import">
  -->
<!-- 
 <link href="http://www.polymer-project.org/components/paper-input/paper-input-decorator.html" rel="import">
 -->
 <!-- Optional -->
<!--  
   <link rel="import" href="//www.polymer-project.org/components/font-roboto/roboto.html">
  <link rel="import" href="//www.polymer-project.org/components/polymer/polymer.html">
  -->
 </head>


