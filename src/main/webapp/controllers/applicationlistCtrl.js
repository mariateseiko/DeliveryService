'use strict'

app.controller('applicationlistCtrl', ['$scope', '$rootScope', '$location', 'orderService', 'managerService', 'sessionService', 'loginService',
    function ($scope, $rootScope, $location, orderService, managerService, sessionService, loginService) {
        $rootScope.login = sessionService.get('user');
        $scope.login = sessionService.get('user');

        $scope.type = "Application";
        //console.log('2'+ $location.path());
        $scope.user = $rootScope.user;

        $scope.chooseApp = "";
        $scope.openApp = function(chooseApp) {
            $scope.chooseApp = chooseApp;
            $rootScope.chooseApp = chooseApp;
            $location.path('/order-info');
        }

        if ($scope.user.role == 'CLIENT') {
            $scope.applications = orderService.getApplications($scope, $rootScope);
            $scope.applications = $rootScope.applications;
        } else {
            $scope.applications = managerService.getApplications($scope, $rootScope);
        }
        $scope.exit = function () {
            loginService.logout($scope, $rootScope);
        }
    }]);