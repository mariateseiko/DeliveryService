'use strict'

app.controller('accountsettingsCtrl', ['$scope', '$rootScope', '$location', 'sessionService', 'userService','loginService',
    function ($scope, $rootScope, $location, sessionService, userService, loginService) {
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            }
            $scope.errorMessage = "";
            $scope.successMessage = "";
            
            $scope.user = $rootScope.user;
            
            $scope.saveSettings = function() {
                $scope.errorMessage = "";
                $scope.successMessage = "";
                userService.saveAccSettings($scope, $scope.user);
            }
        }

        $rootScope.exit = function () {
            $scope.exit();
        }
    }]);