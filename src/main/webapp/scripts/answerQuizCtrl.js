angular.module('quizControllers')
.controller('AnswerQuizCtrl', ['$scope', '$http', '$routeParams',
    function($scope, $http, $routeParams) {
		$scope.quiz = null;
		$scope.questions = [];
		
		$scope.userName = "";
		$scope.userEmail = "";
		$scope.answers = {};
		
		$scope.errorMsg = "";
		
		var currentQuestion = -1;
		var isSubmitted = false;

		var testMode = $routeParams.testMode ? "&testMode=" + $routeParams.testMode : "";
		$http({method: "GET", url: "retrieveQuiz?mode=1&quizId=" + $routeParams.quizId + testMode}).
		success(function(data) {
			$scope.quiz = data;
			$scope.questions = data.questions;
			currentQuestion = 0;
		}).
		error(function(data,status) {
			console.log("Error: " + status + ": " + data);
			currentQuestion = -2;
			$scope.errorMsg = "Error: The Quiz is not available.";
		});
		
		$scope.nextQuestion = function(){
			currentQuestion++;
		};
		
		$scope.prevQuestion = function(){
			currentQuestion--;
		};
		
		$scope.isCurrent = function(q){
			return q === currentQuestion;
		};
		
		$scope.updateAnswers = function(qid, aid){
			var key = "q" + qid;
			$scope.answers[key] = aid;
		};
		
		$scope.isRadioBtnSelected = function(q, qa){
			if (parseInt(q.isChecked, 10) === qa.aid){
				return "quiz-input-choice-box-selected";
			}
			return "";
		};
		
		$scope.checkRadioButtons = function(qid){
			var radioButtonIsChecked = false;
			var buttons = document.getElementsByName("q" + qid);
			var i = 0;
			
			for (i = 0; i < buttons.length; i++){
				if (buttons[i].checked){
					radioButtonIsChecked = true;
					$scope.nextQuestion();
				}
			}
			
			if (!radioButtonIsChecked){
				buttons[0].required = "required";
			}
		};
		
		$scope.submitQuiz = function(){
			if (!isSubmitted){
				isSubmitted = true;
				var submitData = {"quizId": $scope.quiz.quizId, "name": $scope.userName, "email": $scope.userEmail, "answers": $scope.answers};
				
				$http({method: "POST", url: "submitQuiz", data: JSON.stringify(submitData) }).
				success(function(data) {
					$scope.nextQuestion();
				}).
				error(function(data,status) {
					console.log("Error:" + status);
				});
			}
		};

		$scope.init = function(){
			if($routeParams.testMode) {
				$scope.testMode = true;
			}
		};
		
}]);
