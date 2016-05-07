'use strict'

app.controller('orderCtrl', ['$scope', 'orderService', '$rootScope' ,'$location', 'sessionService', 'userService', 'loginService',
    function ($scope, orderService, $rootScope, $location, sessionService, userService, loginService){
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            $rootScope.loginUser = sessionService.get('user');
            $scope.loginUser = sessionService.get('user');

            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
                $rootScope.login = sessionService.get('user');
                $scope.login = sessionService.get('user');

                $scope.errorMessage = "";
                $scope.successMessage = "";

                if (!sessionService.get('user'))
                    $location.path('/');
                else {
                    if (!$rootScope.user) {
                        userService.viewProfile($scope, $rootScope);
                    }
                    $scope.user = $rootScope.user;

                    $scope.sendApplication = function () {
                        if (orderService.checkApplication($scope.application, $scope)) {
                            orderService.sendApplication($scope.application, $scope);
                        }
                    }
                }
                $scope.exit = function () {
                    loginService.logout($scope, $rootScope);
                }
            }
        }
    }]);
