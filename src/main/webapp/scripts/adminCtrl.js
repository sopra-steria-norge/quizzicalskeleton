angular.module('quizControllers')
.controller('AdminCtrl', ['$scope', '$http', '$route', '$routeParams',
    function($scope, $http, $route, $routeParams) {
		$scope.quizzes = [];
		$scope.newquiz = {
				quizName: "",
				quizDesc: "",
				submitMsg: "",
				questions: [{id: 1, text: "", alternatives: [{aid:1, atext: ""}], answer: undefined}]
		};
		
		$http({method: "GET", url: "adminQuiz?mode=2&userId=1"}).
		success(function(data) {
			$scope.quizzes = data;
			fillForm();
		}).
		error(function(data,status) {
			console.log("Error: " + status + ": " + data);
		});
		
		function fillForm(){
			if ($route.current.templateUrl === "templates/adminAddQuiz.html"){
				if ($routeParams.quizId !== ""){
					var i;
					for (i = 0; i < $scope.quizzes.length; i++){
						var quiz = $scope.quizzes[i];
						if (quiz.quizId === parseInt($routeParams.quizId, 10)){
							$scope.newquiz = quiz;
						}
					}
				}
			}
		}
		
		$scope.addAlternative = function(question){
			var numberOfAlternatives = question.alternatives.length;
			question.alternatives.push({aid: (numberOfAlternatives + 1), atext: ""});
		};
		
		$scope.addQuestion = function(){
			var numberOfQuestions = $scope.newquiz.questions.length;
			$scope.newquiz.questions.push({id: (numberOfQuestions + 1), text: "", alternatives: [{aid:1, atext: ""}], answer: 0});
		};
		
		$scope.submitQuiz = function(){
			var submitData = $scope.newquiz;
			alert(JSON.stringify(submitData));
			
			$http({method: "POST", url: "adminQuiz", data: JSON.stringify(submitData) }).
			success(function(data) {
				alert("Submitted!");
			}).
			error(function(data,status) {
				console.log("Error:" + status);
			});			
		};
		
}]);
