'use strict'

app.factory('userService', ['$http', 'orderService', 'sessionService', '$location', function($http, orderService, sessionService, $location){
    return {
        saveAccSettings: function ($scope, user) {
            $http.post('updateProfile', user).then(function(response){
                if (response.status == 200 && response.data){
                    $scope.successMessage = "Settings have changed"
                } else $scope.errorMessage = "Error of saving settings";
            })
        },
        viewProfile: function($scope, $rootScope) {
            $http.get('viewProfile').then(function (response) {
                if (response.status == 200 && response.data){
                    $scope.user = {
                        name: response.data.fullName,
                        login: response.data.login,
                        phone: response.data.phone,
                        role: response.data.role,
                        passport: response.data.passport,
                        countOrders: 0,
                        countApplications: 0
                    };

                    $rootScope.user = $scope.user;

                    $scope.user.countOrders = orderService.getCountOrders($scope, $rootScope);
                    $scope.user.countApplications = orderService.getCountApplications($scope, $rootScope);
                } else $scope.errorMessage = "Error";
            })
        }
    }
}]);
