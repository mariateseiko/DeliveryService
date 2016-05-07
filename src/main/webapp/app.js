/**
 * Created by Анастасия on 23.04.2016.
 */
var app = angular.module('homeServices', ['ngRoute']);

app.config(['$routeProvider', function($routeProvide){
    $routeProvide
    .when('/', {
        templateUrl: 'templates/home.html',
        controller: 'homeCtrl'
    })
    .when('/register', {
        templateUrl: 'templates/register.html',
        controller:  'registerCtrl'
    })
    .when('/login', {
        templateUrl: 'templates/login.html',
        controller:  'loginCtrl'
    })
    .when('/profile', {
        templateUrl: 'templates/profile.html',
        controller: 'profileCtrl'
    })
    .when('/order', {
        templateUrl: 'templates/order.html',
        controller: 'orderCtrl'
    })
    .when('/order-list', {
        templateUrl: 'templates/listOrders.html',
        controller: 'orderlistCtrl'
    })
    .when('/courier-list', {
        templateUrl: 'templates/courierList.html',
        controller: 'couriersCtrl'
    })
    .when('/application-list', {
        templateUrl: 'templates/listOrders.html',
        controller: 'applicationlistCtrl'
    })
    .when('/order-info', {
        templateUrl: 'templates/orderinfo.html',
        controller: 'orderinfoCtrl'
    })
    .when('/accountsettings', {
        templateUrl: 'templates/accset.html'
    })
    .otherwise({
        redirectTo :'/'
    })
}]);



