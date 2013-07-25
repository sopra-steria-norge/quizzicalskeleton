angular.module('quizControllers')
.controller('AdminCtrl', ['$scope', '$http', '$route', '$routeParams', '$location',
    function($scope, $http, $route, $routeParams, $location) {
		$scope.quizzes = [];
		$scope.newquiz = {
				quizName: "",
				quizDesc: "",
				submitMsg: "",
				questions: [{id: 1, text: "", alternatives: [{aid:1, atext: ""}], answer: undefined}],
				userId: "1"
		};
		
		$scope.newquizInitialCopy = {};
		$scope.isEditing = false;
		var isSubmitting = false;
		
		$http({method: "GET", url: "adminQuiz?mode=2&userId=1"}).
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
		});
		
		function initAddQuiz(){
			if ($route.current.templateUrl === "templates/adminAddQuiz.html"){
				if ($routeParams.quizId !== undefined){
					var i;
					for (i = 0; i < $scope.quizzes.length; i++){
						var quiz = $scope.quizzes[i];
						if (quiz.quizId === parseInt($routeParams.quizId, 10)){
							$scope.newquiz = quiz;
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
		
		$scope.removeAlternative = function(question, index){
			question.alternatives.splice(index, 1);
		};
		
		$scope.addQuestion = function(){
			var numberOfQuestions = $scope.newquiz.questions.length;
			$scope.newquiz.questions.push({id: (numberOfQuestions + 1), text: "", alternatives: [{aid:1, atext: ""}], answer: 0});
		};
		
		$scope.removeQuestion = function(questions, index){
			questions.splice(index, 1);
		};
		
		$scope.submitQuiz = function(){
			var submitData = $scope.newquiz;
			
			$http({method: "POST", url: "adminQuiz", data: JSON.stringify(submitData)}).
			success(function(data) {
				isSubmitting = true;
				$location.path("/admin/overview/");
				window.onbeforeunload = null;
			}).
			error(function(data,status) {
				console.log("Error:" + status);
			});			
		};

		$scope.winner = [];
		
		$scope.drawRandomWinner = function(quizId){
			$http({method: "GET", url: "adminQuiz?mode=4&quizId=" + quizId}).
			success(function(data) {
				for(i = 0; i < $scope.quizzes.length; i++){
					if($scope.quizzes[i].quizId === parseInt(quizId, 10)){
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
	
		$scope.respondentsList = [];
		
		function getRespondentsFromDB(quizId){
			$http({method: "GET", url: "adminQuiz?mode=7&quizId=" + quizId}).
			success(function(data){
				$scope.respondentsList = data;
			}).
			error(function(data,status){
				console.log("Error:" + status);
			});
		}
		
		$scope.showRespondents = function(quizId){
			getRespondentsFromDB(quizId);
		};

		$scope.changeActiveStatusTo = function(active, quizId){
			$http({method: "GET", url: "adminQuiz?mode=6&quizId=" + quizId + "&userId=1&active=" + active}).
			success(function(data){
				window.location.reload(true);
			}).
			error(function(data,status){
				console.log("Error:" + status);
			});	
		};

}]);
