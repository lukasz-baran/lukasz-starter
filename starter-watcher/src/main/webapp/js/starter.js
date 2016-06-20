var APP = angular.module('APP', []);
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

