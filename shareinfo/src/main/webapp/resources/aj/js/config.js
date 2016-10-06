/**
 * INSPINIA - Responsive Admin Theme
 * Copyright 2015 Webapplayers.com
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */
function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {
    $urlRouterProvider.otherwise("/index/main");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: true
    });

    $stateProvider

        .state('index', {
            abstract: true,
            url: "/index",
            templateUrl: "resources/aj/views/common/content.html",
        })
        .state('index.main', {
            url: "/main",
            templateUrl: "resources/aj/views/main.html",
            data: { pageTitle: 'Example view' }
        })
        .state('index.minor', {
            url: "/minor",
            templateUrl: "resources/aj/views/minor.html",
            data: { pageTitle: 'File Manager' },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            name: 'jsTree.directive',
                         	files: ['resources/aj/js/plugins/jstree/jsTree.directive.js'],
                        },
                        {
                        	
                            files: ['resources/aj/css/plugins/jsTree/style.min.css','resources/aj/js/plugins/jstree/jstree.min.js'],
                            //'resources/aj/css/plugins/jsTree/style.min.css'
                        }
                    ]);
                }
            }
        })
}
angular
    .module('inspinia')
    .config(config)
    .run(function($rootScope, $state) {
        $rootScope.$state = $state;
    });
