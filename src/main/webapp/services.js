app.factory('loginService', function($http, $location, sessionService) {
    return{
        login: function(data, $scope, $rootScope){
            $scope.errorMessage="";
            
            $http.post('login', data).then(function(response){
                if(response.data !== null){
                    sessionService.set('user', response.data.login);
                    if (!sessionService.get('user'))
                        console.log('Error set session');
                    $location.path('/profile');

                    $rootScope.user = {
                        name: response.data.fullName,
                        login: sessionService.get('user'),
                        phone: response.data.phone,
                        passport: response.data.passport,
                        role: response.data.role
                        //  countOrders: orderService.getCountOrders(),
                        // countApplications: orderService.getCountOrders()
                    }
                } else {
                    $scope.errorMessage = "Wrong login or password";
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
            $http.post("register", dataToSend).then(function(response) {
                if (response.status == 200) {
                    $scope.successMessage = "You are registered";
                }
                else if (response.data == 0)
                    $scope.errorMessage = "This user is already exist.";
            });
        }
    }
});

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
       getApplications: function () {
           $http.get('viewApplications').then(function (response){
                if (response.status == 200 && response.data) {
                    $scope.applications = response.data;
                } else $scope.errorMessage = "Error";
           })
       },
       getOrders: function () {
           $http.get('viewOrders').then(function (response) {
               if (response.status == 200 && response.data) {
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