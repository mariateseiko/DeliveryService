'use strict'

app.controller('documentsCtrl', ['$scope', 'managerService', '$rootScope', 'documentService', 'sessionService',
    function ($scope, managerService, $rootScope, documentService, sessionService){
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
    }]);