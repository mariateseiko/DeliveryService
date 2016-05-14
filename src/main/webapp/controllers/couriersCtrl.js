'use strict'

app.controller('couriersCtrl', ['$scope', 'orderService', '$rootScope' ,'$location', 'managerService', 'sessionService', 'loginService','userService',
    function ($scope, orderService, $rootScope, $location, managerService, sessionService, loginService, userService){
        if (!sessionService.get('user') && sessionService.get('user').role != 'MANAGER')
            $location.path('/');
        else {
            $scope.errorMessage="";
            $scope.successMessage="";
            $rootScope.login = sessionService.get('user');
            $scope.login = sessionService.get('user');

            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            }
            $rootScope.login = sessionService.get('user');
            $scope.login = sessionService.get('user');

            $scope.user = $rootScope.user;

            $scope.couriers = managerService.getCourierList($scope);

            $scope.exit = function () {
                loginService.logout($scope, $rootScope);
            }
            
        }
    }]);