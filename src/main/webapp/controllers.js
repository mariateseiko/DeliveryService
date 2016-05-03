
app.controller('homeCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {
    $scope.go = function (path) {
        $location.path(path);
    };

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

    $scope.user = {
        name: null,
        login: null,
        phone: null,
        passport: null,
        role: null
    };
}]);

app.controller('registerCtrl', ['$scope', 'registerService',function($scope, registerService) {
    $scope.errorMessage="";
    $scope.successMessage="";
    
    $scope.credentials = {
        name: null,
        password: null,
        login: null,
        passwordRepeat: null,
        phone: null,
        passport: null,
    };

    $scope.register = function() {
        registerService.register($scope.credentials, $scope);
    }
}]);


app.controller('loginCtrl', ['$scope', 'loginService', '$rootScope',function($scope, loginService, $rootScope) {
    $scope.credentials = {
        login: null,
        password: null
    };

    $scope.errorMessage="";

    $scope.login = function () {
        loginService.login($scope.credentials, $scope, $rootScope);
    };
}]);

app.controller('profileCtrl', ['$scope', 'sessionService', '$rootScope', '$location', 
    function ($scope, sessionService, $rootScope, $location) {
        if ($rootScope.user == null)
            $location.path('/');
        else    
            $scope.user = $rootScope.user;
}]);
app.controller('orderCtrl', ['$scope', 'orderService', '$rootScope' ,'$location',
    function ($scope, orderService, $rootScope, $location){
        if ($rootScope.user == null)
            $location.path('/');
        else {
            $scope.errorMessage = "";
            $scope.successMessage = "";

            //CHANGE FIELDS FOR SENDING APPLICATION
            $scope.application = {
                from: null,
                to: null,
                distance: null,
                weight: null,
                shipping: null
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
                orderService.sendApplication($scope.application, $scope);
            }
        }
}]);

app.controller('accSettingsCtrl', ['$scope', '$rootScope', '$location', function ($scope, $rootScope, $location) {
    if ($rootScope.user == null)
        $location.path('/');
    else {
        $scope.errorMessage = "";
        $scope.successMessage = "";
    }
}]);