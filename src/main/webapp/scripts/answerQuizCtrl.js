angular.module('quizControllers')
.controller('AnswerQuizCtrl', ['$scope', '$http',
    function($scope, $http) {
		$scope.questions = []; 
	
		$http({method: "GET",url: "quiz/"}).
		success(function(data) {
			$scope.questions = data;
		}).
		error(function(data,status) {
			console.log("Error:" + status);
		});
}]);