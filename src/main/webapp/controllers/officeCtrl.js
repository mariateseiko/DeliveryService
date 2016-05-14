'use strict'

app.controller('officeCtrl', ['$scope', 'officeService', '$rootScope' ,'$location', 'sessionService', 'loginService','userService',
    function ($scope, officeService, $rootScope, $location, sessionService, loginService, userService){
        if (!sessionService.get('user') && sessionService.get('user').role != 'MANAGER')
            $location.path('/');
        else {
            $scope.errorMessage="";
            $scope.successMessage="";
            
            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            }

            $scope.user = $rootScope.user;

            officeService.getPrimaryOffice($scope);

            $scope.exit = function () {
                $rootScope.exit();
            }

        }
    }]);