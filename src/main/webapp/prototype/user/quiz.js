function QuizCtrl($scope){
	$scope.userName = null;
	$scope.userEmail = null;
	
	
	$scope.ansValue = null;
	
	$scope.chooseOption = function(){
		
		var element = document.getElementsByName("quiz-choice");
		var labels = document.getElementsByClassName("radio quiz-input-choice-box");
		
		for (var i = 0; i < element.length; i++) {
			//remove the class quiz-input-choice-box-selected if found
			labels[i].className = labels[i].className.replace( /(?:^|\s)quiz-input-choice-box-selected(?!\S)/g , '' );
			
			if (element[i].checked == true) {
				labels[i].className += " quiz-input-choice-box-selected";
			}
		}
	};
}
