"use strict"

app.controller('homeCtrl', ['$scope', '$http', '$location', 'sessionService', 'userService',
    function($scope, $http, $location, sessionService, userService) {
        $scope.go = function (path) {
            $location.path(path);
        };
        if (sessionService.get('user')) {
            $location.path('/profile');
            return;
        }
        $scope.loginUser = sessionService.get('user');
        
        
        $scope.users = [
            {'name' : 'Grivachevsky Andrey',
                'img' : 'assets/images/team1.jpg'},
            {'name' : 'Kostyukova Anastasia',
                'img' : 'assets/images/team2.jpg'},
            {'name' : 'Teseiko Maria',
                'img' : 'assets/images/team2.jpg'
            }
        ];

        $http.get('viewPricelist').then(function(response) {
            $scope.prices = response.data;
            
        });
        
        $scope.applications = [];
        if (!sessionService.get('user')) {
            $scope.user = {
                name: null,
                login: null,
                phone: null,
                passport: null,
                role: null,
                countOrders: null,
                countApplications: null,
            };
        }
    }]);