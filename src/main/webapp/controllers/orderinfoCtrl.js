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
            var prevDataOrder = {
                status: $scope.order.status, 
                courier: {
                    id: $scope.order.courier? $scope.order.courier.id : null,
                }
            };
            
            $scope.isAssignedCourier = $scope.order.courier? true: false;
            $scope.userData = managerService.getUserData($scope, $rootScope, $rootScope.chooseApp.partner.id);
            
            $scope.saveStatus = function () {
                if (managerService.checkPermissionChangeStatus($scope, $scope.order, prevDataOrder.status))
                managerService.updateOrderStatus($scope, $rootScope, $scope.order);
            };
            $scope.couriers = managerService.getCourierList($scope);
            
            $scope.saveCourier = function() {
                var data = {
                    id_courier: $scope.order.courier.id,
                    id_order: $scope.order.id 
                }
                managerService.assignCourier($scope, data, prevDataOrder.courier.id);
            };
            
            $scope.exit = function () {
                loginService.logout($scope, $rootScope);
            }
            
        }
    }]);
