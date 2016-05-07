
app.controller('homeCtrl', ['$scope', '$http', '$location', 'sessionService', 'userService',
    function($scope, $http, $location, sessionService, userService) {
    $scope.go = function (path) {
        $location.path(path);
    };
   if (sessionService.get('user')) {
       $location.path('/profile');
       return;
   }
    $scope.login = sessionService.get('user');
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
    $scope.applications = [];
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
                $rootScope.login = $scope.user.login;
            };
        }
}]);

app.controller('profileCtrl', ['$scope', 'sessionService', '$rootScope', '$location', 'orderService', 'userService', 'loginService',
    function ($scope, sessionService, $rootScope, $location, orderService, userService,loginService) {
        if (!sessionService.get('user'))
           $location.path('/');
        else {
            $rootScope.login = sessionService.get('user');
            $scope.login = sessionService.get('user');
            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            } else {
                orderService.getCountOrders($scope, $rootScope);
                orderService.getCountApplications($scope, $rootScope);
            }
            $scope.user = $rootScope.user;
            $scope.applications = [];
            $scope.type = "";

            $scope.exit = function () {
                loginService.logout($scope, $rootScope);
            }
        }
}]);
app.controller('applicationlistCtrl', ['$scope', '$rootScope', '$location', 'orderService', 'managerService',
    function ($scope, $rootScope, $location, orderService, managerService) {
        $rootScope.login = sessionService.get('user');
        $scope.login = sessionService.get('user');
        
        $scope.type = "Application";
        //console.log('2'+ $location.path());
        $scope.user = $rootScope.user;

        $scope.chooseApp = "";
        $scope.openApp = function(chooseApp) {
            $scope.chooseApp = chooseApp;
            $rootScope.chooseApp = chooseApp;
            $location.path('/order-info');
        }
        
        if ($scope.user.role == 'CLIENT') {
            $scope.applications = orderService.getApplications($scope, $rootScope);
            $scope.applications = $rootScope.applications;
        } else {
            $scope.applications = managerService.getApplications($scope, $rootScope);
        }
    
}]);

app.controller('orderlistCtrl', ['$scope', '$rootScope', '$location', 'orderService', 'managerService',
    function ($scope, $rootScope, $location, orderService, managerService) {
    $rootScope.login = sessionService.get('user');
    $scope.login = sessionService.get('user');
    
    $scope.type = "Order";
    $scope.user = $rootScope.user;
        
        $scope.chooseApp = "";
        $scope.openApp = function(chooseApp) {
            $scope.chooseApp = chooseApp;
            $rootScope.chooseApp = chooseApp;
            $location.path('/order-info');
        };

    if ($scope.user.role == 'CLIENT') {
        $scope.orders = orderService.getOrders($scope, $rootScope);
        $scope.orders = $rootScope.orders;
    } else {
        $scope.orders = managerService.getOrders($scope, $rootScope);
    }

}]);

app.controller('orderinfoCtrl', ['$scope', '$rootScope', '$location', 'orderService', 'managerService', 'sessionService',
    function ($scope, $rootScope, $location, orderService, managerService, sessionService) {
        $rootScope.login = sessionService.get('user');
        $scope.login = sessionService.get('user');
        
        $scope.user = $rootScope.user;
        $scope.order = $rootScope.chooseApp;

        $scope.errorMessage = "";
        $scope.successMessage = "";
        
        $scope.updates = [
            {status: 'AWAITING'},
            {status: 'DELIVERY'},
            {status: 'DELIVERED'},
            {status: 'CANCELED'},
            {status: 'DECLINED'}
        ];

        $scope.userData = managerService.getUserData($scope, $rootScope, $rootScope.chooseApp.partner.id);
        $scope.saveOrder = function () {
            managerService.updateOrderStatus($scope, $rootScope, $scope.order);
        }
    }]);

app.controller('orderCtrl', ['$scope', 'orderService', '$rootScope' ,'$location', 'sessionService', 'userService',
    function ($scope, orderService, $rootScope, $location, sessionService, userService){
        $rootScope.login = sessionService.get('user');
        $scope.login = sessionService.get('user');
        
        $scope.errorMessage = "";
        $scope.successMessage = "";
        
        if (!sessionService.get('user'))
            $location.path('/');
        else {
            if (!$rootScope.user) {
                userService.viewProfile($scope, $rootScope);
            }
            $scope.user = $rootScope.user;
            
            $scope.sendApplication = function () {
                if (orderService.checkApplication($scope.application, $scope)) {
                    orderService.sendApplication($scope.application, $scope);
                }
            }
        }
}]);

app.controller('couriersCtrl', ['$scope', 'orderService', '$rootScope' ,'$location', 'managerService', 'sessionService',
    function ($scope, orderService, $rootScope, $location, managerService, sessionService){
        $rootScope.login = sessionService.get('user');
        $scope.login = sessionService.get('user');
        
        $scope.user = $rootScope.user;

        $scope.couriers = managerService.getCourierList($scope);
    }]);

app.controller('documentsCtrl', ['$scope', 'managerService', '$rootScope', 'documentService', 'sessionService',
    function ($scope, managerService, $rootScope, documentService, sessionService){
        $rootScope.login = sessionService.get('user');
        $scope.login = sessionService.get('user');
        
        $scope.user = $rootScope.user;
        $scope.apps = documentService.getAllApp($scope);
        
        $scope.exportAgreement = function (order) {
            documentService.exportAgreement(order.id);
        };

        $scope.exportAct = function (order) {
            documentService.exportAct(order.id);
        };

        $scope.exportPriceList = function (order, type) {
            documentService.exportPriceList(order.id, type);
        };
        $scope.exportFinanceReport = function (order, type) {
            documentService.exportFinanceReport(order.id, type);
        };
        $scope.exportOrderList = function () {
            
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