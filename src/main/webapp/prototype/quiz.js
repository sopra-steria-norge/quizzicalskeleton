function QuizCtrl($scope){
	$scope.ansValue = null;
	
	$scope.chooseOption = function(){
		
		var element = document.getElementsByName("quiz-choice");
		var labels = document.getElementsByClassName("radio quiz-input-choice-box");
		
		for (var i = 0; i < element.length; i++) {
			//alert(labels[i].style.background);
			//labels[i].style.background = "#fff";
			
			//remove the class quiz-input-choice-box-selected if found
			labels[i].className = 
				labels[i].className.replace( /(?:^|\s)quiz-input-choice-box-selected(?!\S)/g , '' );
			
			if (element[i].checked == true) {
				labels[i].className += " quiz-input-choice-box-selected";
				
				//var newColor = "#aaa"//element[i].value;
				//document.getElementById("changeColor").style.background = newColor;
				//element[i].getParent().style.background = "#888";
				//labels[i].style.background = "#888";
			}
		}
	};
}
