'use strict'

app.controller('profileCtrl', ['$scope', 'sessionService', '$rootScope', '$location', 'orderService', 'userService', 'loginService',
    function ($scope, sessionService, $rootScope, $location, orderService, userService,loginService) {
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            $scope.errorMessage="";
            $scope.successMessage="";
            $scope.user = $rootScope.user;
            $rootScope.loginUser = sessionService.get('user');
            $scope.loginUser = sessionService.get('user');
            
            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            } else {
                orderService.getCountOrders($scope, $rootScope);
                orderService.getCountApplications($scope, $rootScope);
            }
            $scope.user = $rootScope.user;

            $scope.exit = function () {
                loginService.logout($scope, $rootScope);
            };
            
            $scope.viewOrders = function () {
                $location.path('/order-list');
            };

            $scope.viewApplications = function () {
                $location.path('/application-list');
            }
        }
    }]);
