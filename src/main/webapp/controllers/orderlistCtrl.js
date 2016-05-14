'use strict'

app.controller('orderlistCtrl', ['$scope', '$rootScope', '$location', 'orderService', 'managerService', 'sessionService', 'loginService', 'userService',
    function ($scope, $rootScope, $location, orderService, managerService, sessionService, loginService, userService) {
        if (!sessionService.get('user') )
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

            $scope.type = "Order";
            $scope.user = $rootScope.user;

            $scope.chooseApp = "";
            $scope.openApp = function (chooseApp) {
                $scope.chooseApp = chooseApp;
                $rootScope.chooseApp = chooseApp;
                $location.path('/order-info');
            };

            if ($scope.user.role == 'CLIENT') {
                $scope.orders = orderService.getOrders($scope, $rootScope);
                $scope.orders = $rootScope.orders;
            } else {
                $scope.orders = managerService.getOrders($scope, $rootScope);
            }
            $scope.exit = function () {
                loginService.logout($scope, $rootScope);
            }
        }
        
    }]);