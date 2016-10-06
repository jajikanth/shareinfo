/**
 * INSPINIA - Responsive Admin Theme
 * Copyright 2015 Webapplayers.com
 *
 */

/**
 * MainCtrl - controller
 */
function MainCtrl() {

    this.userName = 'Jajikanth';
    this.helloText = 'Welcome to File Manager App';
    this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

};

/**
 * nestableCtrl - Controller for nestable list
 */
function nestableCtrl($scope, $http) {
	//select 1st user and load folders by default.
	$scope.showFolderHelp = true;   
	$scope.tree = []
	   $scope.changeTree = function() {
		   console.log("on change tree");
		   $scope.tree = [{"id": 1,"parent": "#","text": "Simple root"},
		                  {"id": 2,"parent": "#","text": "Root 2"},
		                  {"id": 3,"parent": 2,"text": "Child 1"},
		                  {"id": 4,"parent": 2,"text": "Child 2"}];
		   }
	   $http.get('consultants').success(function(data) {
		    $scope.users = data;
		  });//need to redirect on error
	    angular.element(document).ready(function () {
	       console.log("On document ready");
	       
	    });
	$scope.loadFolders = function(user_id) {
		$scope.showFolderHelp = false;
		var URL = 'fileManager/' + user_id + '/folders';
		console.log("load folders url : " + URL);
		$http.get(URL).success(function(data) {
			console.log("folders length : " + data.length);
			if(data.length > 0){
				$scope.tree = data;	
			} else {
				alert("invalid request");
				$scope.showFolderHelp = true;   
				$scope.tree = [];
			}
			
			});//handle error also to empty the tree when improper request is triggered.
	}	    // Set scope tree to null and load it on click of
}

angular
    .module('inspinia')
    .controller('MainCtrl', MainCtrl)
    .controller('nestableCtrl', nestableCtrl)