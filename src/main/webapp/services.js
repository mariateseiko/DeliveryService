app.factory('loginService', function($http, $location, sessionService, orderService) {
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
                        role: response.data.role,
                        countOrders: 0,
                        countApplications: 0,
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
app.factory('userService', ['$http', 'orderService', function($http, orderService){
    return {
        saveAccSettings: function (data, $scope) {
            $http.post('changeAccSettings').then(function(response){
                if (response.status == 200 && response.data){
                    $scope.successMessage = "Settings have changed"
                } else $scope.errorMessage = "Error";
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
        }
    }
}]);

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
                    });
                    $scope.apps.push(response.data);

                })
            })
        },
        exportAgreement: function (order) {
            var data = {
                orderId: order,
                docType: "PDF"
            };
            $http.post('exportAgreement', data).then(function (response) {
                console.log('Success exportAgreement');
            });
            console.log('Error exportAgreement '+ response.data);
        },
        exportAct: function (order) {
            var data = {
                orderId: order,
                docType: "PDF"
            };
            $http.post('actAgreement', data).then(function (response) {
                console.log('Success exportAgreement '+ response.data);
            });
            console.log('Error exportAgreement');
        }
    }
}]);
function parseDate(data) {
    return data.substring(0, data.indexOf('T'));
}