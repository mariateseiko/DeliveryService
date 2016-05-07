'use strict'

app.controller('accSettingsCtrl', ['$scope', '$rootScope', '$location', 'sessionService', 'userService','loginService',
    function ($scope, $rootScope, $location, sessionService, userService, loginService) {
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            }
            $scope.errorMessage = "";
            $scope.successMessage = "";
        }

        $scope.exit = function () {
            loginService.logout($scope, $rootScope);
        }
    }]);