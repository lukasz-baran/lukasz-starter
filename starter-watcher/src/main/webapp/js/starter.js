var APP = angular.module('APP', []);
anotherController = function ($scope) {
    $scope.name = '...';
    $scope.sayHelloWorld = function() {
        $scope.name = 'World';
    }
	$scope.ludzie = ["Andrzej", "Sebastian", "Kamil"];
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

APP.controller('monitoringCtrl',
	function($scope, $http) {
		$http.get("rest/monitoring/getAll")
		.then(function(response) {
			$scope.myData = response.data;
		});

		$scope.control = function(parameter) {
			$http.get("rest/monitoring/" + parameter).then(function(response) {
				//$scope.userData = response.data;
			})
		};
	}
);

