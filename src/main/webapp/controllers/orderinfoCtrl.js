'use strict'

app.controller('orderinfoCtrl', ['$scope', '$rootScope', '$location', 'orderService', 'managerService', 'sessionService', 'loginService', 'userService',
    function ($scope, $rootScope, $location, orderService, managerService, sessionService, loginService, userService) {
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            $rootScope.login = sessionService.get('user');
            $scope.login = sessionService.get('user');

            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            }
            $rootScope.login = sessionService.get('user');
            $scope.login = sessionService.get('user');

            $scope.user = $rootScope.user;
            $scope.order = $rootScope.chooseApp;

            $scope.errorMessage = "";
            $scope.successMessage = "";

            $scope.updates = [
                {status: 'AWAITING'},
                {status: 'DELIVERY'},
                {status: 'DELIVERED'},
                {status: 'CANCELED'},
                {status: 'DECLINED'}
            ];

            $scope.userData = managerService.getUserData($scope, $rootScope, $rootScope.chooseApp.partner.id);
            $scope.saveOrder = function () {
                managerService.updateOrderStatus($scope, $rootScope, $scope.order);
            }
            $scope.exit = function () {
                loginService.logout($scope, $rootScope);
            }
        
        }
    }]);
