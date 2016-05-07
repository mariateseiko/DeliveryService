'use strict'

app.controller('documentsCtrl', ['$scope', 'managerService', '$rootScope', 'documentService', 'sessionService', 'loginService','userService',
    function ($scope, managerService, $rootScope, documentService, sessionService, loginService, userService){
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

                $scope.user = $rootScope.user;
                $scope.apps = documentService.getAllApp($scope);

                $scope.exportAgreement = function (order) {
                    documentService.exportAgreement(order.id);
                };

                $scope.exportAct = function (order) {
                    documentService.exportAct(order.id);
                };

                $scope.exportPriceList = function (type) {
                    documentService.exportPriceList(type);
                };
                $scope.exportFinanceReport = function (type) {
                    documentService.exportFinanceReport(type);
                };
                $scope.exportOrderList = function (courierId) {
                  documentService.exportOrderList(courierId);
                };
                $scope.exit = function () {
                    loginService.logout($scope, $rootScope);
                }
            }
        
    }]);
