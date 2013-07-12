angular.module('quizControllers')
.controller('AnswerQuizCtrl', ['$scope', '$http',
    function($scope, $http) {
		var currentQuestion = 0;
		$scope.questions = []; 
	
		/*$http({method: "GET",url: "quiz/"}).
		success(function(data) {
			$scope.questions = data;
		}).
		error(function(data,status) {
			console.log("Error:" + status);
		});
		*/
		$scope.questions = [{id:1, text: "Hva er hovedstaden i Norge?", qAlt: [{aid:1, aText:"Oslo"}, {aid:2, aText:"Bergen"}] },
		                    {id:2, text: "Hva er norges største innsjoe?", qAlt: [{aid: 1, aText:"Mjosa"}] }
		];
		
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
			return q.id == currentQuestion;
		};
		
		
		$scope.chooseOption = function(){
			var element = document.getElementsByName("quiz-choice");
			var labels = document.getElementsByClassName("radio quiz-input-choice-box");
			
			for (var i = 0; i < element.length; i++) {
				labels[i].className = labels[i].className.replace( /(?:^|\s)quiz-input-choice-box-selected(?!\S)/g , '' );
				
				if (element[i].checked == true) {
					labels[i].className += " quiz-input-choice-box-selected";
				}
			}
		};
		
}]);
