'use strict'

app.factory('managerService', ['$http', function ($http) {
    return {
        getApplications: function ($scope, $rootScope) {
            $http.get('viewApplications').then(function (response) {
                response.data.forEach(function (element) {
                    element.date = parseDate(element.date);
                });
                $scope.applications = response.data;
                $rootScope.applications = response.data;
            })
        },
        getOrders: function ($scope, $rootScope) {
            $http.get('viewOrders').then(function (response) {
                response.data.forEach(function (element) {
                    element.date = parseDate(element.date);
                });
                $scope.orders = response.data;
                $rootScope.orders = response.data;
            })
        },
        getUserData: function($scope, $rootScope, id) {
            $http.get('viewProfile', {
                params: {userId: id}
            }).then(function (responce) {
                $scope.userData = responce.data;
            });
        },
        updateOrderStatus: function ($scope, $rootScope, $order) {
            var data = {
                orderId: $order.id,
                status: $order.status
            }
            $http.post('updateStatus', data).then(function (response) {
                if (response.status == 200 && response.data){
                    $scope.successMessage = "Order was updated."
                } else $scope.errorMessage = "Error";
            })
        },
        getCourierList: function ($scope) {
            $http.get('viewCouriers').then(function (response) {
                $scope.couriers = response.data;
                //$rootScope.couriers = response.data;
            })
        },
        checkPermissionChangeStatus: function ($scope, order, prevStatus) {
            $scope.errorMessage = "";
            if (!order.courier) $scope.errorMessage = "You need to assign courier!";
            if (prevStatus != 'AWAITING' && order.status == 'AWAITING')   {
               $scope.errorMessage = "You can't change status to this value";
            }
            if (order.status == prevStatus) {
                $scope.errorMessage = "New status and current status are the same.";
            }
            return $scope.errorMessage?false:true;
            
        },
        assignCourier: function ($scope, data, prevCourier) {
            $scope.errorMessage = "";
            var dataToSend = {
                cid: data.id_courier,
                oid: data.id_order
            };
            if (prevCourier == data.id_courier) {
                $scope.errorMessage = "Previous courier and new courier are the same"
            } else
                $http.post('assignCourier', dataToSend).then(function (response) {
                    if (response.status == 200 && response.data){
                        $scope.successMessage = "Order was updated."
                    } else $scope.errorMessage = "Error";
                });
        },
        getPricelist : function ($scope) {
            $http.get('viewPricelist').then(function (response) {
                if (response.status == 200 && response.data){
                    $scope.prices = response.data;
                    
                } else $scope.errorMessage = "Error";
            });
        }
    }
}]);