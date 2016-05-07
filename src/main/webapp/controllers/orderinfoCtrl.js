'use strict'

app.controller('orderinfoCtrl', ['$scope', '$rootScope', '$location', 'orderService', 'managerService', 'sessionService',
    function ($scope, $rootScope, $location, orderService, managerService, sessionService) {
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
    }]);
