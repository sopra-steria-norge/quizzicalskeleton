function QuizCtrl($scope,$http) {
	$scope.numberOne = 3;
	$scope.numberTwo = 2;

	$scope.addNumbers = function() {
		$scope.sum = $scope.numberOne + $scope.numberTwo;
	};
}