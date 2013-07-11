angular.module('quizControllers')
.controller('QuizCtrl', ['$scope', '$http',
    function($scope, $http) {

	$scope.numberOne = 3;
	$scope.numberTwo = 2;

	$scope.addNumbers = function() {
		$scope.sum = $scope.numberOne + $scope.numberTwo;
		$http({method: "POST",url: "quiz/sum", data: {one: $scope.numberOne, two: $scope.numberTwo}}).
		success(function(data) {
			$scope.serversum = data.sum;
		}).
		error(function(data,status) {
			console.log("Error:" + status);
		});
	};
}]);