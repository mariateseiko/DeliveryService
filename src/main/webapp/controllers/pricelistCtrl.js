'use strict'

app.controller('priceListCtrl', ['$scope', 'orderService', '$rootScope' ,'$location', 'sessionService', 'userService', 'managerService',
    function ($scope, orderService, $rootScope, $location, sessionService, userService, managerService){
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            $scope.errorMessage="";
            $scope.successMessage="";
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

                    $scope.prices = managerService.getPricelist($scope);
                    $scope.savePrices = function () {

                    }
                }
                $scope.exit = function () {
                    loginService.logout($scope, $rootScope);
                }
            }
        }
    }]);
