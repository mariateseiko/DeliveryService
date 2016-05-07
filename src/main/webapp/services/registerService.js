'use strict'

app.factory('registerService', function($http){
    return{
        register: function (credentials, $scope) {
            $scope.errorMessage="";
            $scope.successMessage="";

            var pattern = /[\d|\w]{6,10}/;
            var regPhone = /\+[0-9|-]{1,4}[\(|0-9|\)]{2,5}[0-9| |\(\)|-]{5,}/;
            if (credentials.password != credentials.passwordRepeat) {
                $scope.errorMessage="Passwords don't match";

            } else if (!pattern.exec(credentials.password)){
                $scope.errorMessage="Password should only consist of letters and numbers, be at least 6 and no longer than 10 characters";
            } else if (!regPhone.exec(credentials.phone)) {
                $scope.errorMessage= "Phone number must contain country code, may contain the symbols -()/."
            }

            var dataToSend = {
                name: credentials.name,
                password: credentials.password,
                login: credentials.login,
                phone: credentials.phone,
                passport: credentials.passport
            };
            if (!$scope.errorMessage) {
                $http.post("register", dataToSend).then(function (response) {
                    if (response.status == 200) {
                        $scope.successMessage = "You are registered";
                    }
                    else if (response.data == 0)
                        $scope.errorMessage = "This user is already exist.";
                });
            }
        }
    }
});
