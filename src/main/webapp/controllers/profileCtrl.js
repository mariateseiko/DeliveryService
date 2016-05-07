'use strict'

app.controller('profileCtrl', ['$scope', 'sessionService', '$rootScope', '$location', 'orderService', 'userService', 'loginService',
    function ($scope, sessionService, $rootScope, $location, orderService, userService,loginService) {
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            $rootScope.login = sessionService.get('user');
            $scope.login = sessionService.get('user');
            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            } else {
                orderService.getCountOrders($scope, $rootScope);
                orderService.getCountApplications($scope, $rootScope);
            }
            $scope.user = $rootScope.user;
            $scope.applications = [];
            $scope.type = "";

            $scope.exit = function () {
                loginService.logout($scope, $rootScope);
            }
        }
    }]);
