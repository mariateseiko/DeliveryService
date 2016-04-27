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

app.controller('homeCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {
    $scope.go = function (path) {
        $location.path(path);
    };

    $scope.users = [
        {'name' : 'Grivachevsky Andrey',
            'img' : 'assets/images/team1.jpg'},
        {'name' : 'Kostyukova Anastasia',
            'img' : 'assets/images/team2.jpg'},
        {'name' : 'Teseiko Maria',
            'img' : 'assets/images/team2.jpg'
        }
    ];

    $http.get('price.json').then(function(data) {
        $scope.prices = data;
    });
}]);
app.controller('registerCtrl', ['$scope','$http', function($scope, $http) {
    $scope.errorMessage="";
    $scope.successMessage="";
    $scope.credentials = {
        name: null,
        surname: null,
        email: null,
        phone: null,
        password: null,
        passwordRepeat: null
    };

    $scope.register = function () {
        $scope.errorMessage="";
        $scope.successMessage="";

        var pattern = /[\d|\w]{6,10}/;
        if ($scope.credentials.password != $scope.credentials.passwordRepeat) {
            $scope.errorMessage="Passwords don't match";
        } else if (!pattern.exec($scope.credentials.password)){
            console.log($scope.credentials.password);
            $scope.errorMessage="Password should only consist of letters and numbers, be at least 6 and no longer than 10 characters";
        }

        var dataToSend = {
            name: $scope.credentials.name,
            surname: $scope.credentials.surname,
            email: $scope.credentials.email,
            password: $scope.credentials.password,
            phone: $scope.credentials.phone
        };
        $http.post("register", dataToSend).then(function(response) {
            if (response.data == true) {
                $scope.successMessage = "You are registered!";
            } else {
                $scope.errorMessage = "This user is already exist :(";
            }
        });
    };
}]);

app.controller('loginCtrl', ['$scope','$http', '$location', function($scope, $http, $location) {
    $scope.errorMessage="";
    $scope.credentials = {
        email: null,
        password: null
    };

    $scope.login = function () {
        $http.post("login", $scope.credentials).then(function(response) {
            if (response.data == true) {
                $location.path('/profile');
            } else {
                $scope.errorMessage = "Wrong email or password";
            }
        });
    };
}]);