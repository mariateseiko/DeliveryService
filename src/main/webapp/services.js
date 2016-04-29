app.factory('loginService', function($http, $location, sessionService) {
    return{
        login: function(data, scope){
            $scope.errorMessage="";
            var $promise=$http.post('login', data);
            $promise.then(function(msg){
                var uid = msg.data;
                if(uid){
                    sessionService.set('user', uid);
                    $location.path('/profile');
                } else {
                    scope.errorMessage = "Wrong login or password";
                }
            });
        },
        logout:function () {
            sessionService.destroy('user');
            $location.path('/login');
        }
    }
});

app.factory('sessionService', ['$http', function($http){
    return{
        set: function(key, value){
            return sessionStorage.setItem(key, value);
        },
        get: function (key) {
            return sessionStorage.getItem(key);
        },
        destroy: function(key){
            return sessionStorage.removeItem(key);
        }
    }
}]);

app.factory('registerService', function($http){
    return{
        register: function (credentials, $scope) {
            $scope.errorMessage="";
            $scope.successMessage="";

            var pattern = /[\d|\w]{6,10}/;
            if (credentials.password != credentials.passwordRepeat) {
                $scope.errorMessage="Passwords don't match";

            } else if (!pattern.exec($scope.credentials.password)){
                $scope.errorMessage="Password should only consist of letters and numbers, be at least 6 and no longer than 10 characters";
            }
            var dataToSend = {
                name: credentials.name,
                password: credentials.password,
                login: credentials.login,
                phone: credentials.phone
            };
            $http.post("register", dataToSend).then(function(response) {
                console.log(response.data);
                $scope.successMessage = "You are registered";
            });
        }
    }
});