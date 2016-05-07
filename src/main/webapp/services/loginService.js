'use strict'

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
        logout:function ($scope, $rootScope) {
            sessionService.destroy('user');
            $http.get('logout').then(function () {
                $rootScope.user = null;
                $scope.user = null;
                $location.path('/');
            });
        }
    }
});