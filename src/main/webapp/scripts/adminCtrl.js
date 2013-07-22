angular.module('quizControllers')
.controller('AdminCtrl', ['$scope', '$http',
    function($scope, $http) {
		$scope.newquiz = {
				quizName: "",
				quizDesc: "",
				submitMsg: "",
				questions: [{id: 1, text: "", alternatives: [{aid:1, atext: "Aaa"}], answer: 0}]
		};
		
		$scope.addAlternative = function(question){
			question.alternatives.push({aid:2, atext: ""});
		};
}]);

