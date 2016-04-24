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
    .otherwise({
        redirectTo :'/'
    })
}]);

app.controller('homeCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {
    $scope.users = [
        {'name' : 'Grivachevsky Andrey',
            'img' : 'assets/images/team1.jpg'},
        {'name' : 'Kostyukova Anastasia',
            'img' : 'assets/images/team2.jpg'},
        {'name' : 'Teseiko Maria',
            'img' : 'assets/images/team2.jpg'
        }
    ];

    $http.get('price.json').success(function(data) {
        $scope.prices = data;
    });
}]);
app.controller('registerController', ['$scope','$http', function($scope, $http) {
    $scope.register = function (name, email, password, passwordRepeat) {
        console.log(email);
        /*$http.post("register", user).success(function(data) {
            console.log('REGISTRATION');
        });*/
    };
}]);
app.controller('loginController', ['$scope','$http', function($scope, $http) {
    $scope.login = function (email, password) {
        console.log(email+' '+password);
        /*$http.post("register", user).success(function(data) {
         console.log('REGISTRATION');
         });*/
    };
}]);