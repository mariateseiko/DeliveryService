
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
    };

    $scope.register = function() {
        registerService.register($scope.credentials, $scope);
    }
}]);


app.controller('loginCtrl', ['$scope', 'loginService', 'sessionService', 'orderService',function($scope, loginService, sessionService, orderService) {
    $scope.credentials = {
        name: null,
        password: null
    };
    $scope.user = null;

    $scope.login = function () {
        loginService.login($scope.credentials, $scope);
    };
    $scope.user = {
        name:  sessionService.get('user').name,
        login:  sessionService.get('user').login,
        phone:  sessionService.get('user').phone,
        passport:  sessionService.get('user').passport,
        countOrders: orderService.getCountOrders(),
        countApplications: orderService.getCountOrders()
    }
   
    
}]);

app.controller('orderCtrl', ['$scope', 'orderService', 'sessionService',function ($scope, orderService, sessionService){
    $scope.errorMessage="";
    $scope.successMessage="";
    
    //CHANGE FIELDS FOR SENDING APPLICATION
    $scope.application = {
        id: sessionService.get('user').id,
        from: null,
        to: null,
        distance: null,
        weight: null,
        typeShipping: null
    };
    $scope.sendApplication = orderService.sendApplication($scope.application, $scope);
    
}]);
//SESSION STORE ONLY ID AND LOGIN OF USER
app.controller('accSettingsCtrl', ['$scope', 'sessionService', function ($scope, sessionService) {
    $scope.errorMessage = "";
    $scope.successMessage = "";
    
}]);