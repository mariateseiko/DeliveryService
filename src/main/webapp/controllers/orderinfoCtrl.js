'use strict'

app.controller('orderinfoCtrl', ['$scope', '$rootScope', '$location', 'orderService', 'managerService', 'sessionService', 'loginService', 'userService',
    function ($scope, $rootScope, $location, orderService, managerService, sessionService, loginService, userService) {
        if (!sessionService.get('user'))
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
            $scope.order = $rootScope.chooseApp;

            $scope.userData = managerService.getUserData($scope, $rootScope, $rootScope.chooseApp.partner.id);
            
            $scope.saveOrder = function () {
                managerService.updateOrderStatus($scope, $rootScope, $scope.order);
            };
            $scope.couriers = managerService.getCourierList($scope);
            
            $scope.assignCourier = function() {
                var data = {
                    id_courier: $scope.selected,
                    id_order: $scope.order.id
                }
                managerService.assignCourier($scope, data);
            };
            
            $scope.exit = function () {
                loginService.logout($scope, $rootScope);
            }
            
        }
    }]);
