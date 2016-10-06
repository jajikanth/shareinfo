<!--
* INSPINIA - Responsive Admin Theme
* Version 2.0
*
-->

<!DOCTYPE html>
<html ng-app="inspinia">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Page title set in pageTitle directive -->
    <title page-title></title>

    <!-- Font awesome -->
    <link href="resources/aj/font-awesome/css/font-awesome.css" rel="stylesheet">

    <!-- Bootstrap and Fonts -->
    <link href="resources/aj/css/bootstrap.min.css" rel="stylesheet">

    <!-- Main Inspinia CSS files -->
    <link href="resources/aj/css/animate.css" rel="stylesheet">
    <link id="loadBefore" href="resources/aj/css/style.css" rel="stylesheet">


</head>

<!-- ControllerAs syntax -->
<!-- Main controller with serveral data used in Inspinia theme on diferent view -->
<body ng-controller="MainCtrl as main">

<!-- Main view  -->
<div ui-view></div>

<!-- jQuery and Bootstrap -->
<script src="resources/aj/js/jquery/jquery-2.1.1.min.js"></script>
<script src="resources/aj/js/plugins/jquery-ui/jquery-ui.js"></script>
<script src="resources/aj/js/bootstrap/bootstrap.min.js"></script>

<!-- MetsiMenu -->
<script src="resources/aj/js/plugins/metisMenu/jquery.metisMenu.js"></script>

<!-- SlimScroll -->
<script src="resources/aj/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- Peace JS -->
<script src="resources/aj/js/plugins/pace/pace.min.js"></script>

<!-- Custom and plugin javascript -->
<script src="resources/aj/js/inspinia.js"></script>

<!-- Main Angular scripts-->
<script src="resources/aj/js/angular/angular.min.js"></script>
<script src="resources/aj/js/plugins/oclazyload/dist/ocLazyLoad.min.js"></script>
<!-- <script src="resources/aj/js/plugins/jstree/jsTree.directive.js"></script> -->
<script src="resources/aj/js/ui-router/angular-ui-router.min.js"></script>
<script src="resources/aj/js/bootstrap/ui-bootstrap-tpls-0.12.0.min.js"></script>

<!-- Anglar App Script -->
<script src="resources/aj/js/app.js"></script>
<script src="resources/aj/js/config.js"></script>
<script src="resources/aj/js/directives.js"></script>
<script src="resources/aj/js/controllers.js"></script>

</body>
</html>
