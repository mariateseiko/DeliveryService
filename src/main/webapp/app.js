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
    $scope.errorMessage="";
    $scope.register = function (name, email, password, passwordRepeat) {
        $scope.errorMessage="";
        var pattern = /[\d|\w]{6,10}/;
       /* if (password != passwordRepeat) {
            console.log(password+' '+passwordRepeat);
            $scope.errorMessage="Passwords don't match";

        } else */if (!pattern.exec(password)){
            console.log(password);
            $scope.errorMessage="Password should only consist of letters and numbers, be at least 6 and no longer than 10 characters";
        }
        /*$http.post("register", user).success(function(data) {
            console.log('REGISTRATION');
        });*/
    };
}]);

app.controller('loginController', ['$scope','$http', '$location', function($scope, $http, $location) {
    $scope.login = function (email, password) {
        console.log(email+' '+password);
        $location.path('/profile');
        /*$http.post("register", user).success(function(data) {
         console.log('REGISTRATION');
         });*/
    };
}]);