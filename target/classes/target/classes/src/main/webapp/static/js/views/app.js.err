require.config({
    paths: {
        // angular
        "angular": "/static/js/angular.min.1.4.6",

        // angular-ui
        "angular-ui-router": "/static/js/angular-ui-router/angular-ui-router.min",

        // angularAMD
        "angularAMD": "/static/js/angularAMD/angularAMD.min",
        "ngload": "/static/js/angularAMD/ngload.min"
    },
    shim: {
        // angular
        "angular": { exports: "angular" },

        // angular-ui
        "angular-ui-router": ["angular"],

        // angularAMD
        "angularAMD": ["angular"],
        "ngload": ["angularAMD"]
    }
});

// var myApp = angular.module("myApp", ["ui.router"]);

// myApp.config(function ($stateProvider, $urlRouterProvider) {

//      $urlRouterProvider.when("", "/PageTab");

//      $stateProvider
//         .state("PageTab", {
//             url: "/PageTab",
//             templateUrl: "PageTab.tpl"
//         })
//         .state("PageTab.Page1", {
//             url:"/Page1",
//             templateUrl: "Page-1.tpl"
//         })
//         .state("PageTab.Page2", {
//             url:"/Page2",
//             templateUrl: "Page-2.tpl"
//         })
//         .state("PageTab.Page3", {
//             url:"/Page3",
//             templateUrl: "Page3.tpl"
//         });
// });

angular.module('myApp',['ngRoute'])
            .config(['$routeProvider', function($routeProvider){
                $routeProvider
                .when('/',{template:'这是首页页面'})
                .when('/computers',{template:'这是电脑分类页面'})
                .when('/printers',{template:'这是打印机页面'})
                .otherwise({redirectTo:'/'});
            }]);