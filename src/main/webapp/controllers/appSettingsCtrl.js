'use strict'

app.controller('accSettingsCtrl', ['$scope', '$rootScope', '$location', 'sessionService', 'userService',
    function ($scope, $rootScope, $location, sessionService, userService) {
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            }
            $scope.errorMessage = "";
            $scope.successMessage = "";
        }
    }]);