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
        templateUrl: 'templates/profile.html'
    })
    .when('/order', {
        templateUrl: 'templates/order.html'
    })
    .when('/accountsettings', {
        templateUrl: 'templates/accset.html'
    })
    .otherwise({
        redirectTo :'/'
    })
}]);


