angular.module('quizControllers')
.controller('SubmitQuizCtrl', ['$scope', '$http',
    function($scope, $http) {
		
		$http({method: "GET",url: "submit/"}).
		success(function(data) {
			
		}).
		error(function(data,status) {
			console.log("Error:" + status);
		});
				
}]);
