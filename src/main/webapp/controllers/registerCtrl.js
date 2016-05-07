"use strict"

app.controller('registerCtrl', ['$scope', 'registerService', 'sessionService', '$location',
    function($scope, registerService, sessionService, $location) {
        if (sessionService.get('user')) {
            $location.path('/profile');

        }
        else {
            $scope.errorMessage = "";
            $scope.successMessage = "";

            $scope.credentials = {
                name: null,
                password: null,
                login: null,
                passwordRepeat: null,
                phone: null,
                passport: null,
            };

            $scope.register = function () {
                registerService.register($scope.credentials, $scope);
            }
        }
    }]);