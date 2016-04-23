/**
 * Created by Анастасия on 23.04.2016.
 */
var controllers = angular.module('homeServices', []);

controllers.controller('developerListCtrl', function ($scope) {
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
controllers.controller('priceListCtrl', function ($scope, $http) {
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
controllers.controller('addressesListCtrl', function ($scope) {
    $scope.adresses = [

    ]
})