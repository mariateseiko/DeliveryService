'use strict'


app.factory('documentService', ['$http', function ($http) {
    return {
        getAllApp: function ($scope) {
            $http.get('viewApplications').then(function (response) {
                response.data.forEach(function (element) {
                    element.date = parseDate(element.date);
                });
                $scope.apps = response.data;

                $http.get('viewOrders').then(function (response) {
                    response.data.forEach(function (element) {
                        element.date = parseDate(element.date);
                        $scope.apps.push(element);
                    });
                })
            })
        },
        exportAgreement: function (order) {
            var data = {
                orderId: order,
                docType: "PDF"
            };
            $http.post('exportAgreement', data).then(function (response) {
                console.log('Success exportAgreement' + response.data);
            });
            console.log('Error exportAgreement ');
        },
        exportAct: function (order) {
            var data = {
                orderId: order,
                docType: "PDF"
            };
            $http.post('actAgreement', data).then(function (response) {
                console.log('Success actAgreement '+ response.data);
            });
            console.log('Error actAgreement');
        },
        exportPriceList: function (order, type) {
            var data = {
                orderId: order,
                docType: type
            };
            $http.post('exportPriceList ', data).then(function (response) {
                console.log('Success exportPriceList  '+ response.data);
            });
            console.log('Error exportPriceList ');
        },
        exportFinanceReport: function (order, type) {
            var data = {
                orderId: order,
                docType: type
            };
            $http.post('exportFinanceReport ', data).then(function (response) {
                console.log('Success exportFinanceReport  '+ response.data);
            });
            console.log('Error exportFinanceReport ');
        },
        exportOrderList: function (courier) {
            var data = {
                courierId: courier,
                docType: 'PDF'
            };
            $http.post('exportOrderList  ', data).then(function (response) {
                console.log('Success exportOrderList   '+ response.data);
            });
            console.log('Error exportOrderList  ');
        }
    }
}]);


function parseDate(data) {
    return data.substring(0, data.indexOf('T'));
}
