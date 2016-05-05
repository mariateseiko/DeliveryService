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
    .when('/orderinfo', {
        templateUrl: 'templates/orderinfo.html',
        controller: 'orderCtrl'
    })
    .when('/accountsettings', {
        templateUrl: 'templates/accset.html'
    })
    .otherwise({
        redirectTo :'/'
    })
}]);



