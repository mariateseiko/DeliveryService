'use strict'

app.controller('priceListCtrl', ['$scope', 'orderService', '$rootScope' ,'$location', 'sessionService', 'userService',
    'managerService','loginService',
    function ($scope, orderService, $rootScope, $location, sessionService, userService, managerService, loginService){
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            $scope.errorMessage="";
            $scope.successMessage="";
            $rootScope.loginUser = sessionService.get('user');
            $scope.loginUser = sessionService.get('user');

            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            }
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

                    $scope.prices = [];
                    managerService.getPriceList($scope);
                        
                    $scope.savePriceList = function () {
                        if (managerService.checkPriceList($scope))
                            managerService.savePriceList();
                    }
                }
                $scope.exit = function () {
                    loginService.logout($scope, $rootScope);
                }
            
        }
    }]);
