app.factory('loginService', function($http, $location, sessionService) {
    return{
        login: function(data, scope){
            scope.errorMessage="";
            
            $http.post('login', data).then(function(msg){
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

app.factory('sessionService', function(){
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
});

app.factory('registerService', function($http){
    return{
        register: function (credentials, $scope) {
            console.log('why i here?');
            $scope.errorMessage="";
            $scope.successMessage="";

            var pattern = /[\d|\w]{6,10}/;
            if (credentials.password != credentials.passwordRepeat) {
                $scope.errorMessage="Passwords don't match";

            } else if (!pattern.exec(credentials.password)){
                $scope.errorMessage="Password should only consist of letters and numbers, be at least 6 and no longer than 10 characters";
            }
            var dataToSend = {
                name: credentials.name,
                password: credentials.password,
                login: credentials.login,
                phone: credentials.phone,
                passport: credentials.passport
            };
            $http.post("register", dataToSend).then(function(response) {
                if (response.status == 'ok')
                    $scope.successMessage = "You are registered";
                else if (response.data == 0)
                    $scope.errorMessage = "This user is already exist.";
            });
        }
    }
});

app.factory('orderService', ['$http', function ($http) {
   return{
       sendApplication: function (data, $scope) {
           $http.post('sendOrder', data).then(function (response) {
                if (response.status == 'ok') {
                    $scope.successMessage = "Your application is sent."
                } else $scope.errorMessage = "Error";
           })
       },
       getApplications: function () {
           $http.get('getApplications').then(function (response){
                if (response.status == 'ok') {
                    $scope.applications = response.data;
                } else $scope.errorMessage = "Error";
           })
       },
       getOrders: function () {
           $http.get('getOrders').then(function (response) {
               if (response.status == 'ok') {
                   $scope.orders = response.data;
               }else $scope.errorMessage = "Error";
           })
       },
       getCountApplications: function () {
           return this.getApplications().length();
       },
       getCountOrders: function () {
           return this.getOrders().length();
       }
   } 
}]);
app.factory('userService', ['$http', 'sessionService', function($http, sessionService){
    return {
        saveAccSettings: function (data, $scope) {
            $http.post('changeAccSettings').then(function(response){
                if (response.status == 'ok'){
                    $scope.successMessage = "Settings have changed"
                } else $scope.errorMessage = "Error";
            })
        }
    }
}]);