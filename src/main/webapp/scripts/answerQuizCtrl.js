angular.module('quizControllers')
.controller('AnswerQuizCtrl', ['$scope', '$http', '$routeParams',
    function($scope, $http, $routeParams) {
		$scope.quiz = null;
		$scope.questions = [];
		
		$scope.userName = "";
		$scope.userEmail = "";
		$scope.answers = {};
		
		var currentQuestion = 0;
		
		$http({method: "GET", url: "quiz?quizId=" + $routeParams.quizid}).
		success(function(data) {
			$scope.quiz = data;
			$scope.questions = data.questions;
		}).
		error(function(data,status) {
			console.log("Error:" + status);
		});
		
		
		$scope.nextQuestion = function(){
			currentQuestion++;
		};
		
		$scope.prevQuestion = function(){
			currentQuestion--;
		};
		
		$scope.showBox = function(value){
			return value === currentQuestion;
		};
		
		$scope.isCurrent = function(q){
			return q === currentQuestion;
		};
		
		$scope.updateAnswers = function(qid, aid){
			var key = "q" + qid;
			$scope.answers[key] = aid;
		};
		
		$scope.highlightChoosenOption = function(qid){
			var radioButtons = document.getElementsByName("q" + qid);
			var labels = document.getElementsByClassName("radio quiz-input-choice-box ql" + qid);
			var i = 0;
			
			for (i = 0; i < radioButtons.length; i++) {
				labels[i].className = labels[i].className.replace( /(?:^|\s)quiz-input-choice-box-selected(?!\S)/g , '' );
				
				if (radioButtons[i].checked) {
					labels[i].className += " quiz-input-choice-box-selected";
				}
			}
		};
		
		$scope.radioButtonChanged = function(qid, aid){
			$scope.updateAnswers(qid, aid);
			$scope.highlightChoosenOption(qid);
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
			var submitData = {"quizId": $scope.quiz.quizId, "name": $scope.userName, "email": $scope.userEmail, "answers": $scope.answers};
			
			$http({method: "POST", url: "submit", data: JSON.stringify(submitData) }).
			success(function(data) {
				$scope.nextQuestion();
			}).
			error(function(data,status) {
				console.log("Error:" + status);
			});
		};
		
}]);
