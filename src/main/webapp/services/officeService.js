'use strict'

app.factory('officeService', ['$http', function ($http) {
    return {

        getPrimaryOffice: function ($scope) {
            $http.get('viewPrimaryOffice').then(function (response) {
                $scope.primaryOffice = response.data;
            });
        },

        updatePrimaryOffice: function ($scope, $rootScope, $office) {

            var data = {
                name: $office.name,
                credentials: $office.credentials
            };

            $http.post('updatePrimaryOffice', data).then(function (response) {
                if (response.status == 200 && response.data) {
                    $scope.successMessage = "The primary office has been" +
                        " updated.";
                } else {
                    $scope.errorMessage = "Failed to update the primary office.";
                }
            });

        }

    }
}]);
