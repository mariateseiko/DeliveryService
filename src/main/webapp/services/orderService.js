'use strict'

app.factory('orderService', ['$http', function ($http) {
    return{
        checkApplication: function(data, $scope) {
            $scope.errorMessage="";
            $scope.successMessage="";
            var regNumber = /^(\-|\+)?([0-9]+)$/;

            if (!regNumber.exec(data.weight)) {
                $scope.errorMessage = "Weight must be specified as a number";
                return false;
            }
            if(!regNumber.exec(data.distance)) {
                $scope.errorMessage = "Distance must be specified as a number";
                return false;
            }
            return true;
        },
        sendApplication: function (data, $scope) {
            $scope.successMessage = "";
            $scope.errorMessage = "";
            var now = new Date();
            var strDateTime = [[now.getFullYear(),AddZero(now.getMonth() + 1), AddZero(now.getDate())].join("-"), [AddZero(now.getHours()), AddZero(now.getMinutes()), AddZero(now.getSeconds())].join(":")].join(" ");
            function AddZero(num) {
                return (num >= 0 && num < 10) ? "0" + num : num + "";
            }

            console.log(strDateTime);
            data.data = strDateTime;
            $http.post('sendOrder', data).then(function (response) {
                if (response.status == 200 && response.data) {
                    $scope.successMessage = "Your application is sent."
                } else $scope.errorMessage = "Error";
            })
        },
        getApplications: function ($scope, $rootScope) {
            $http.get('viewUserApplications').then(function (response){
                response.data.forEach(function (element) {
                    element.date = parseDate(element.date);
                });

                if (response.status == 200 && response.data) {
                    $scope.applications = response.data;
                    $rootScope.applications = response.data;
                } else $scope.errorMessage = "Error";
            })
        },
        getOrders: function ($scope) {
            $http.get('viewUserOrders').then(function (response) {
                response.data.forEach(function (element) {
                    element.date = parseDate(element.date);
                });

                if (response.status == 200 && response.data) {
                    $scope.orders = response.data;
                }else $scope.errorMessage = "Error";
            })
        },
        getCountApplications: function ($scope, $rootScope) {
            $http.get('viewUserApplications').then(function (response){
                if (response.status == 200 && response.data) {
                    $rootScope.user.countApplications = response.data.length;
                    $scope.user.countApplications = response.data.length;
                    return response.data.length;
                } else $scope.errorMessage = "Error";
            });
        },
        getCountOrders: function ($scope, $rootScope) {
            $http.get('viewUserOrders').then(function (response){
                if (response.status == 200 && response.data) {
                    $rootScope.user.countOrders = response.data.length;
                    $scope.user.countOrders = response.data.length;
                    return response.data.length;
                } else $scope.errorMessage = "Error";
            });
        }
    }
}]);
