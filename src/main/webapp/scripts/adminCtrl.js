angular.module('quizControllers')
.controller('AdminCtrl', ['$scope', '$http',
    function($scope, $http) {
		var currentaid = {};
		
		$scope.newquiz = {
				quizName: "",
				quizDesc: "",
				submitMsg: "",
				questions: [{id: 1, text: "", alternatives: [{aid:1, atext: ""}], answer: undefined}]
		};
		
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
