'use strict'

app.controller('documentsCtrl', ['$scope', 'managerService', '$rootScope', 'documentService', 'sessionService', 'loginService','userService',
    function ($scope, managerService, $rootScope, documentService, sessionService, loginService, userService){
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            $rootScope.loginUser = sessionService.get('user');
            $scope.loginUser = sessionService.get('user');

            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            }
                $rootScope.login = sessionService.get('user');
                $scope.login = sessionService.get('user');

                $scope.user = $rootScope.user;
                $scope.apps = documentService.getAllApp($scope);

                $scope.exportAgreement = function (order) {
                    documentService.exportAgreement(order.id);
                };

                $scope.exportAct = function (order) {
                    documentService.exportAct(order.id);
                };

                $scope.exportPriceList = function (order, type) {
                    documentService.exportPriceList(order.id, type);
                };
                $scope.exportFinanceReport = function (order, type) {
                    documentService.exportFinanceReport(order.id, type);
                };
                $scope.exportOrderList = function () {

                }
                $scope.exit = function () {
                    loginService.logout($scope, $rootScope);
                }
            }
        
    }]);