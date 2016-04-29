
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
        passwordRepeat: null
    };

    $scope.register = registerService.register();
}]);


app.controller('loginCtrl', ['$scope','$http', 'loginService', function($scope, $http, loginService) {
    $scope.credentials = {
        name: null,
        password: null
    };

    $scope.login = function () {
        loginService.login($scope.credentials, $scope);
    };
}]);
