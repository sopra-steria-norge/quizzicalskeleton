angular.module('quizControllers')
.controller('AnswerQuizCtrl', ['$scope', '$http', '$routeParams',
    function($scope, $http, $routeParams) {
		var currentQuestion = 0;
		
		$scope.quiz = null;
		$scope.questions = []; 
		
		$http({method: "GET", url: "quiz/?quizId=" + $routeParams.quizid}).
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
			return value == currentQuestion;
		};
		
		$scope.isCurrent = function(q){
			return q == currentQuestion;
		};
		
		
		$scope.chooseOption = function(id){
			var radioButtons = document.getElementsByName("q" + id);
			var labels = document.getElementsByClassName("radio quiz-input-choice-box ql" + id);
			
			for (var i = 0; i < radioButtons.length; i++) {
				labels[i].className = labels[i].className.replace( /(?:^|\s)quiz-input-choice-box-selected(?!\S)/g , '' );
				
				if (radioButtons[i].checked == true) {
					labels[i].className += " quiz-input-choice-box-selected";
				}
			}
		};
		
		$scope.checkRadioButtons = function(id){
			var radioButtonIsChecked = false;
			var buttons = document.getElementsByName("q" + id);
			
			for (var i = 0; i < buttons.length; i++){
				if (buttons[i].checked){
					radioButtonIsChecked = true;
					$scope.nextQuestion();
				}
			}
			
			if (!radioButtonIsChecked){
				buttons[0].required = "required";
			}
		};
		
}]);
