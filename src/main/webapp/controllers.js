
app.controller('homeCtrl', ['$scope', '$http', '$location', 'sessionService', 'userService',
    function($scope, $http, $location, sessionService, userService) {
    $scope.go = function (path) {
        $location.path(path);
    };
   if (sessionService.get('user')) {
       $location.path('/profile');
       return;
   }

    $scope.users = [
        {'name' : 'Grivachevsky Andrey',
            'img' : 'assets/images/team1.jpg'},
        {'name' : 'Kostyukova Anastasia',
            'img' : 'assets/images/team2.jpg'},
        {'name' : 'Teseiko Maria',
            'img' : 'assets/images/team2.jpg'
        }
    ];

    $http.get('price.json').success(function(data) {
        $scope.prices = data;
    });
    
    if (!sessionService.get('user')) {
        $scope.user = {
            name: null,
            login: null,
            phone: null,
            passport: null,
            role: null,
            countOrders: null,
            countApplications: null,
        };
    }
}]);

app.controller('registerCtrl', ['$scope', 'registerService', 'sessionService', '$location',
    function($scope, registerService, sessionService, $location) {
        if (sessionService.get('user')) {
            $location.path('/profile');
        }
        else {
            $scope.errorMessage = "";
            $scope.successMessage = "";
    
            $scope.credentials = {
                name: null,
                password: null,
                login: null,
                passwordRepeat: null,
                phone: null,
                passport: null,
            };
    
            $scope.register = function () {
                registerService.register($scope.credentials, $scope);
            }
        }
}]);


app.controller('loginCtrl', ['$scope', 'loginService', '$rootScope', 'orderService', '$location', 'sessionService',
    function($scope, loginService, $rootScope, orderService, $location, sessionService) {
        $scope.credentials = {
            login: null,
            password: null
        };

        if (sessionService.get('user')) {
            $location.path('/profile');
        } else {
            $scope.errorMessage = "";

            $scope.login = function () {
                loginService.login($scope.credentials, $scope, $rootScope);
            };
        }
}]);

app.controller('profileCtrl', ['$scope', 'sessionService', '$rootScope', '$location', 'orderService', 'userService',
    function ($scope, sessionService, $rootScope, $location, orderService, userService) {
        if (!sessionService.get('user'))
           $location.path('/');
        else {
            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            } else {
                orderService.getCountOrders($scope, $rootScope);
                orderService.getCountApplications($scope, $rootScope);
            }
            $scope.user = $rootScope.user;
        }
}]);
app.controller('orderCtrl', ['$scope', 'orderService', '$rootScope' ,'$location', 'sessionService', 'userService',
    function ($scope, orderService, $rootScope, $location, sessionService, userService){
        $scope.errorMessage = "";
        $scope.successMessage = "";
        
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            }
            //CHANGE FIELDS FOR SENDING APPLICATION
            $scope.application = {
                from: null,
                to: null,
                distance: null,
                weight: null,
                shipping: null,
                data: null
            };

            $scope.order = {
                ord_id: null,
                ord_date: null,
                usr_login: null,
                usr_name: null,
                usr_passport: null,
                usr_phone: null,
                ord_status: null,
                ord_from: null,
                ord_to: null,
                ord_distance: null,
                ord_weight: null,
                shp_name: null,
                shp_pricePerKG: null,
                shp_pricePerKM: null,
                ord_total: null
            }
            $scope.sendApplication = function () {
                if (orderService.checkApplication($scope.application, $scope)) {
                    orderService.sendApplication($scope.application, $scope);
                }
            }
        }
}]);

app.controller('accSettingsCtrl', ['$scope', '$rootScope', '$location', 'sessionService', 'userService',
    function ($scope, $rootScope, $location, sessionService, userService) {
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            }
            $scope.errorMessage = "";
            $scope.successMessage = "";
         }
}]);