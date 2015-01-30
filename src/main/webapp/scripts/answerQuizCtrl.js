angular.module('quizControllers')
.controller('AnswerQuizCtrl', ['$scope', '$http', '$routeParams',
    function($scope, $http, $routeParams) {
		$scope.quiz = null;
		$scope.questions = [];
		
		$scope.userName = "";
		$scope.userEmail = "";
		$scope.userCompany = "";
		$scope.userPhoneNumber = "";
		$scope.answers = {};

		$scope.errorMsg = "";

		$scope.i18nMap = {
			0 : {
				startQuiz : "Start quiz",
				loading : "Loading...",
				previousQuestion : "Previous question",
				nextQuestion : "Next question",
				fillUserInfo : "Please fill out the following fields in order to enter to win",
				required : "Required",
				invalidEmail : "Not a valid email address",
				submitAnswers : "Submit answers",
				submitted : "The quiz is now submitted! Thank you for taking the quiz!",
				name : "Name",
				email : "Email address",
				company : "Company name",
				phone : "Phone number",
				questionCap : "Question",
				questionOf : "of",
				restartQuiz : "Restart quiz",
				checkAnswer : "Check answer"
			},
			1 : {
				startQuiz : "Starta quizzen",
				loading : "Laddar...",
				previousQuestion : "Föregående fråga",
				nextQuestion : "Nästa fråga",
				fillUserInfo : "Vänligen fyll ut följande information om dig själv",
				required : "Obligatoriskt fält",
				invalidEmail : "Ogiltig emailadress",
				submitAnswers : "Skicka in svar",
				submitted : "Tack för ditt deltagande!",
				name : "Namn",
				email : "Emailadress",
				company : "Företagsnamn",
				phone : "Telefonnummer",
				questionCap : "Fråga",
				questionOf : "av",
				restartQuiz : "Ta quizzen igen",
				checkAnswer : "Kontrollera svar"
			}
		};
		
		$scope.currentQuestion = -1;
		$scope.shownAnswer = false;
		$scope.correctAnswer = false;
		var isSubmitted = false;

		var testMode = $routeParams.testMode ? "&testMode=" + $routeParams.testMode : "";
		$http({method: "GET", url: "retrieveQuiz?mode=1&quizId=" + $routeParams.quizId + testMode}).
		success(function(data) {
			$scope.quiz = data;
			$scope.questions = data.questions;
			$scope.currentQuestion = 0;
		}).
		error(function(data,status) {
			console.log("Error: " + status + ": " + data);
			$scope.currentQuestion = -2;
			$scope.errorMsg = "Error: The Quiz is not available.";
		});
		
		$scope.nextQuestion = function(){
			$scope.currentQuestion++;
			$scope.shownAnswer = false;
			$scope.correctAnswer = false;
		};
		
		$scope.prevQuestion = function(){
			$scope.currentQuestion--;
		};
		
		$scope.isCurrent = function(q){
			return q === $scope.currentQuestion;
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
		
		$scope.checkInput = function(qid){
			var radioButtonIsChecked = false;
			var buttons = document.getElementsByName("q" + qid);
			var i = 0;
			
			for (i = 0; i < buttons.length; i++){
				if (buttons[i].checked){
					radioButtonIsChecked = true;
					$scope.nextQuestion();
				}
			}

			if(buttons.length === 0) {
				var text = document.getElementById("textAnswer_q" + qid);

				if(text.value.length > 0) {
					$scope.textAnswer = text.value;
					$scope.nextQuestion();
				}
				else {
					text.required = "required";
				}
			}
			else if (!radioButtonIsChecked){
				buttons[0].required = "required";
			}
		};

		$scope.checkAnswer = function(qid){
			var radioButtonIsChecked = false;
			var buttons = document.getElementsByName("q" + qid);
			var i;

			for (i = 0; i < buttons.length; i++){
				if (buttons[i].checked){
					radioButtonIsChecked = true;

					//Show correct/wrong question msg
					$scope.correctAnswer = ($scope.questions[$scope.currentQuestion - 1].answer === (i + 1));
					$scope.correctAnswerNumber = $scope.questions[$scope.currentQuestion - 1].answer;

					//Show "next question" button, hide the show question button
					$scope.shownAnswer = true;
				}
			}

			if (!radioButtonIsChecked){
				buttons[0].required = "required";
			}
		};

		$scope.getAlternativeClass = function(qid, aid) {

			if($scope.shownAnswer && $scope.isCorrectAnswer(aid)) {
				return "correct-answer";
			}

			if($scope.shownAnswer && !$scope.correctAnswer && $scope.isSelected(qid, aid)) {
				return "incorrect-selected-answer";
			}

			return "";
		};

		$scope.isSelected = function(qid, aid){
			var buttons = document.getElementsByName("q" + qid);

			var i;
			for (i = 0; i < buttons.length; i++){
				if (buttons[i].checked){
					return aid === (i + 1);
				}
			}
			return false;
		};

		$scope.isCorrectAnswer = function(aid){
			return ($scope.questions[$scope.currentQuestion - 1].answer === aid);
		};
		
		$scope.submitQuiz = function(){
			if (!isSubmitted){
				isSubmitted = true;
				var submitData = {"quizId": $scope.quiz.quizId, "name": $scope.userName, "email": $scope.userEmail,
					"company": $scope.userCompany, "phoneNumber": $scope.userPhoneNumber, "answers": $scope.answers,
					"textAnswer": $scope.textAnswer};
				
				$http({method: "POST", url: "submitQuiz", data: JSON.stringify(submitData) }).
				success(function(data) {
					$scope.nextQuestion();
				}).
				error(function(data,status) {
					console.log("Error:" + status);
				});
			}
		};

		$scope.i18n = function(key){

			if(!$scope.quiz) {
				return "";
			}

			var lang = $scope.quiz.language;
			return $scope.i18nMap[lang][key];
		};

		$scope.init = function(){
			if($routeParams.testMode) {
				$scope.testMode = true;
			}
		};
		
}]);
