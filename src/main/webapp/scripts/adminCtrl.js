angular.module('quizControllers')
.controller('AdminCtrl', ['$scope', '$http', '$route', '$routeParams', '$location',
    function($scope, $http, $route, $routeParams, $location) {
		$scope.quizzes = [];

		$scope.languages = [
			{id: 0, name:'English'},
			{id: 1, name:'Swedish'}
		];

		$scope.newquiz = {
				quizName: "",
				quizDesc: "",
				submitMsg: "",
				questions: [{id: 1, text: "", alternatives: [{aid:1, atext: ""}], answer: undefined, answerInText: false}],
				userId: "",
				language: "",
				showAnswer: false
		};

		$scope.changepw = {
			oldPassword: "",
			newPassword1: "",
			newPassword2: ""
		};

		$scope.createUser = {
			username: "",
			newPassword1: "",
			newPassword2: ""
		};

		$scope.newquizInitialCopy = {};
		$scope.isEditing = false;
		$scope.duplicate = false;

		var isSubmitting = false;
		var updateRespondentsInterval;
		
		$scope.winner = [];
		
		$scope.user = null;
			
		$http({method: "GET", url: "adminQuiz?mode=8"}).
		success(function(data){
			$scope.user = data;
		}).
		error(function(data,status){
			console.log("Error:" + status);
		});

		$http({method: "GET", url: "adminQuiz?mode=12"}).
		success(function(data) {
			$scope.quizzes = data;
			initAddQuiz();
			$scope.newquizInitialCopy = angular.copy($scope.newquiz);
		}).
		error(function(data,status) {
			console.log("Error: " + status + ": " + data);
		});
				
		$scope.$on("$locationChangeStart", function(event){
			if (!isSubmitting){
				if ($route.current.templateUrl === "templates/adminAddQuiz.html") {
					var copyOfNewquiz =  angular.copy($scope.newquiz);
					
					delete copyOfNewquiz.questions[0].$$hashKey;
					delete copyOfNewquiz.questions[0].alternatives[0].$$hashKey;
					
					if (JSON.stringify(copyOfNewquiz) === JSON.stringify($scope.newquizInitialCopy)){
						window.onbeforeunload = null;
					} else if(!confirm("Your quiz is not submitted, are you sure you want to leave this page?")){
						event.preventDefault();
					}
				}
			} 
			if ($route.current.templateUrl === "templates/adminResults.html"){
				updateRespondentsInterval = clearInterval(updateRespondentsInterval);
			}
		});
		
		function initAddQuiz(){
			if ($route.current.templateUrl === "templates/adminAddQuiz.html"){
				if ($routeParams.quizId !== undefined){

					var quizId = parseInt($routeParams.quizId, 10);

					var i;
					for (i = 0; i < $scope.quizzes.length; i++){
						var quiz = $scope.quizzes[i];
						if (quiz.quizId === quizId){
							$scope.newquiz = quiz;
							$scope.newquiz.duplicate = $routeParams.duplicate !== undefined;
							$scope.newquiz.userId = $scope.user.userId;

							//Fy helvete..
							var i2;
							for (i2 = 0; i2 < $scope.languages.length; i2++) {
								var lang = $scope.languages[i2];
								if (lang.id === $scope.newquiz.language) {
									$scope.newquiz.language = lang;
								}

							}
						}
					}
					$scope.isEditing = true;
				}
				window.onbeforeunload = function (event) {
					var message = "Your quiz is not submitted, are you sure you want to exit?";
					if (typeof event === 'undefined') {
						event = window.event;
					}
					if (event) {
						if($route.current.templateUrl === "templates/adminAddQuiz.html"){
							event.returnValue = message;
						}
					}
					return message;
				};
			}
		}
		
		$scope.addAlternative = function(question){
			var numberOfAlternatives = question.alternatives.length;
			question.alternatives.push({aid: (numberOfAlternatives + 1), atext: ""});
		};

		$scope.setAnswerInText = function($index, $event){
			var q = $scope.newquiz.questions[$index];

			if($event.currentTarget.checked) {
				//If set, disable add alternative.
				document.getElementById("addAlternative-" + $index).disabled = true;

				//Remove existing alternatives.
				q.alternatives = [];
				//Remove correct answer
				q.answer = undefined;
				//Uncheck other checkboxes for text answers (only one supported)

				var boxes = document.getElementsByClassName("answerInText");
				var i;
				for (i = 0; i < boxes.length; i++){
					if($event.currentTarget !== boxes[i]) {
						boxes[i].checked = false;
					}
				}



			}
			else {
				//If unset, enable add alternative.
				document.getElementById("addAlternative-" + $index).disabled = false;
			}
		};

		
		$scope.removeAlternative = function(question, index){
			if (question.alternatives[index].aid === parseInt(question.answer, 10)){
				question.answer = undefined;
			} else if (question.alternatives[index].aid < question.answer){
				question.answer--;
			}
			question.alternatives.splice(index, 1);
			
			var i;
			for (i = 0; i < question.alternatives.length; i++){
				question.alternatives[i].aid = 1 + i;
			}
		};
		
		$scope.addQuestion = function(){
			var numberOfQuestions = $scope.newquiz.questions.length;
			$scope.newquiz.questions.push({id: (numberOfQuestions + 1), text: "", alternatives: [{aid:1, atext: ""}], answer: 0});
		};
		
		$scope.removeQuestion = function(questions, index){
			questions.splice(index, 1);
			for (i = 0; i < questions.length; i++){
				questions[i].id = 1 + i;
			}
		};

		$scope.submitQuiz = function(){
			var submitData = $scope.newquiz;

			$http({method: "POST", url: "adminQuiz", data: JSON.stringify(submitData)}).
			success(function(data) {

				if(data.errorMsg) {
					alert(data.errorMsg);
				}
				else {
					isSubmitting = true;
					$location.path("/admin/overview/");
					window.onbeforeunload = null;
				}

			}).
			error(function(data,status) {
				console.log("Error:" + status);
			});
		};

		$scope.submitChangePassword = function(){
			var submitData = $scope.changepw;

			$http({method: "POST", url: "adminPassword", data: JSON.stringify(submitData)}).
				success(function(data) {
					if(data.errorMsg) {
						alert(data.errorMsg);
					}
					else {
						alert('Password changed.');
						isSubmitting = true;
						$location.path("/admin/overview/");
						window.onbeforeunload = null;
					}
				}).
				error(function(data,status) {
					console.log("Error:" + status);
				});
		};

		$scope.submitCreateUser = function(){
			var submitData = $scope.createUser;

			$http({method: "POST", url: "createUser", data: JSON.stringify(submitData)}).
				success(function(data) {
					if(data.errorMsg) {
						alert(data.errorMsg);
					}
					else {
						alert('User added.');
						isSubmitting = true;
						$location.path("/admin/overview/");
						window.onbeforeunload = null;
					}
				}).
				error(function(data,status) {
					console.log("Error:" + status);
				});
		};


		$scope.drawRandomWinner = function(quizId){
			$http({method: "GET", url: "adminQuiz?mode=4&quizId=" + quizId}).
			success(function(data) {
				for(i = 0; i < $scope.quizzes.length; i++){
					if($scope.quizzes[i].quizId === quizId){
						$scope.quizzes[i].winner = data;
					}
				}

			}).
			error(function(data,status){
				console.log("Error:" + status);
			});	
		};
		
		$scope.removeQuiz = function(quiz){
			var confirmDelete = confirm("Are you sure you want to delete \""+quiz.quizName+"\"?");
			if(confirmDelete){
				removeQuizFromDB(quiz.quizId);
			}
		};
		
		function removeQuizFromDB(quizId){
			$http({method: "GET", url: "adminQuiz?mode=5&quizId=" + quizId}).
			success(function(data){
				window.location.assign("#/admin/overview");
				window.location.reload(true);
			}).
			error(function(data,status){
				console.log("Error:" + status);
			});
		}
		
		function updateRespondentsFromDB(quizId){
			$http({method: "GET", url: "adminQuiz?mode=7&quizId=" + quizId}).
			success(function(data){
				for(i = 0; i < $scope.quizzes.length; i++){
					if($scope.quizzes[i].quizId === quizId){
						$scope.quizzes[i].respondentsList = data;
						$scope.quizzes[i].responses = $scope.quizzes[i].respondentsList.length;
					}
				}
			}).
			error(function(data,status){
				console.log("Error:" + status);
			});
		}
		
		$scope.$on('$viewContentLoaded', function() {
			if ($route.current.templateUrl === "templates/adminResults.html"){
				updateRespondentsInterval = window.setInterval(function updateAllRespondentsFromDB(){
					var i;
					for (i = 0; i < $scope.quizzes.length; i++){
						updateRespondentsFromDB($scope.quizzes[i].quizId);
					}
				}, 1000);
			}
		});
		
		$scope.showRespondentState = {};
		
		$scope.showRespondents = function(quizId){
			$scope.showRespondentState["q" + quizId] = true;
			updateRespondentsFromDB(quizId);
		};
		
		$scope.hideRespondents = function(quizId){
			$scope.showRespondentState["q" + quizId] = false;
		};

		$scope.changeActiveStatusTo = function(active, quizId){
			$http({method: "GET", url: "adminQuiz?mode=6&quizId=" + quizId + "&active=" + active}).
			success(function(data){
				window.location.reload(true);
			}).
			error(function(data,status){
				console.log("Error:" + status);
			});	
		};
		
		$scope.logout = function(){
			$http({method: "GET", url: "adminQuiz?mode=9"}).
			success(function(){
				window.location.assign("#/");
				window.location.reload(true);
			}).
			error(function(status){
				console.log("Error:" + status);
			});
		};

}]);