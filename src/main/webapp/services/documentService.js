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

            window.open('exportAgreement?docType=' + data.docType +
                '&orderId=' + data.orderId);

            console.log('Error exportAgreement ');
        },
        exportAct: function (order) {
            var data = {
                orderId: order,
                docType: "PDF"
            };
            $http.get('actAgreement', {params: data}).then(function (response) {
                console.log('Success actAgreement '+ response.data);
            });
            console.log('Error actAgreement');
        },
        //------------- -order
        exportPriceList: function (type) {
            var data = {
                docType: type
            };
            $http.get('exportPriceList ', {params: data}).then(function (response) {
                console.log('Success exportPriceList  '+ response.data);
            });
            console.log('Error exportPriceList ');
        },
        //--------------- -order, + sinnceData 
        exportFinanceReport: function (type) {
            var now = new Date();
            var strDateTime = [now.getFullYear(),AddZero(now.getMonth() + 1), AddZero(now.getDate())].join("-");
            function AddZero(num) {
                return (num >= 0 && num < 10) ? "0" + num : num + "";
            }
            var data = {
                sinceData: strDateTime,
                docType: type
            };
            $http.get('exportFinanceReport ', {params: data}).then(function (response) {
                console.log('Success exportFinanceReport  '+ response.data);
            });
            console.log('Error exportFinanceReport ');
        },
        //-------
        exportOrderList: function (courier) {
            var data = {
                courierId: courier,
                docType: 'PDF'
            };
            $http.get('exportOrderList  ', {params: data}).then(function (response) {
                console.log('Success exportOrderList   '+ response.data);
            });
            console.log('Error exportOrderList  ');
        }
    }
}]);


function parseDate(data) {
    return data.substring(0, data.indexOf('T'));
}
