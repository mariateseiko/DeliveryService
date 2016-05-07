'use strict'

app.controller('couriersCtrl', ['$scope', 'orderService', '$rootScope' ,'$location', 'managerService', 'sessionService',
    function ($scope, orderService, $rootScope, $location, managerService, sessionService){
        $rootScope.login = sessionService.get('user');
        $scope.login = sessionService.get('user');

        $scope.user = $rootScope.user;

        $scope.couriers = managerService.getCourierList($scope);
    }]);