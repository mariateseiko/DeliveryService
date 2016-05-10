'use strict'

app.controller('loginCtrl', ['$scope', 'loginService', '$rootScope', 'orderService', '$location', 'sessionService',
    function($scope, loginService, $rootScope, orderService, $location, sessionService) {
        
        $scope.credentials = {
            login: null,
            password: null
        };

        if (sessionService.get('user')) {
            $location.path('/profile');
        } else {
            $scope.errorMessage = "";

            $scope.login = function () {
                loginService.login($scope.credentials, $scope, $rootScope);
                
            };
        }
    }]);