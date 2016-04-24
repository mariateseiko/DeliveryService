/**
 * Created by Анастасия on 23.04.2016.
 */
var deviveryApp = angular.module('homeServices', ['ngRoute']);

deviveryApp.config(['$routeProvider', function($routeProvide){
    $routeProvide
    .when('/', {
        templateUrl: 'templates/home.html',
        controller: 'homeCtrl'
    })
    .otherwise({
        redirectTo :'/'
    })
}]);

deviveryApp.controller('homeCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {
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

