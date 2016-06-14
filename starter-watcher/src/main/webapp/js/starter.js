var APP = angular.module('APP', []);
APP.ApplicationCtrl = function ($scope) {
    $scope.name = '...';

    $scope.sayHelloWorld = function() {
        $scope.name = 'World';
    }
};

APP.factory('Data', function() {
    return {message:"I'm data from a service"}
})

function firstCtrl($scope, Data) {
	$scope.data = Data;
}

function secondCtrl($scope, Data) {
	$scope.data = Data;

	$scope.reversedMessage = function() {
		return $scope.data.message.split("").reverse().join("");
	}
}

APP.controller('usersCtrl', function($scope, $http) {
    $scope.loadUsers = function() {
        //$http.get() TODO

    }
});