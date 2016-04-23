/**
 * Created by Анастасия on 23.04.2016.
 */
<<<<<<< Updated upstream
var deviveryApp = angular.module('homeServices', ['ngRoute']);

deviveryApp.config(['$routeProvider', function($routeProvide){
    $routeProvide

=======
var app = angular.module('homeServices', ['ngRoute']);

app.config(['$routeProvider', function($routeProvide){
    $routeProvide
    .when('/', {
        templateUrl: 'templates/home.html',
        controller: 'registerCtrl'
    })
>>>>>>> Stashed changes
    .when('/reg', {
        templateUrl: 'templates/registration.html',
        controller: 'registerCtrl'
    })
    .when('/login', {
        templateUrl: 'templates/login.html',
        controller: 'loginCtrl'
    })
<<<<<<< Updated upstream
}]);

deviveryApp.controller('registerCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {
}]);

deviveryApp.controller('loginCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {
}]);

deviveryApp.controller('developerListCtrl', function ($scope) {
=======
    .otherwise({
        reditectTo :'/'
    })
}]);

app.controller('registerCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {
}]);

app.controller('loginCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {
}]);

app.controller('developerListCtrl', function ($scope) {
>>>>>>> Stashed changes
    $scope.users = [
        {'name' : 'Grivachevsky Andrey',
            'img' : 'assets/images/team1.jpg'},
        {'name' : 'Kostyukova Anastasia',
            'img' : 'assets/images/team2.jpg'},
        {'name' : 'Teseiko Maria',
            'img' : 'assets/images/team2.jpg'
        }
    ];
});
<<<<<<< Updated upstream
deviveryApp.controller('priceListCtrl', function ($scope, $http) {
=======
app.controller('priceListCtrl', function ($scope, $http) {
>>>>>>> Stashed changes
    $http.get('price.json').success(function(data) {
        $scope.prices = data;
    });
    /*$scope.prices = [
     {'name' : 'Advanced',
     'price' : 14,
     'storage' : '30GB Storage',
     'ram' : '5GB RAM'},
     {'name' : 'Standart',
     'price' : 99,
     'storage' : '20GB Storage',
     'ram' : '3GB RAM'},
     {'name' : 'Basic',
     'price' : 23,
     'storage' : '5GB Storage',
     'ram' : '1GB RAM'
     }
     ];*/
});
