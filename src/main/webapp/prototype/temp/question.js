function QuestionCtrl($scope) {
    $scope.questions = [
        {text:'Mycodiesel is a fuel made of grass', correct:0},
        {text:'The mass of a cube is measured by density and volume', correct:1},
        {text:'The moving sidewalk was invented in 1893', correct:1},
        {text:'A carbon atom is lighter than a hydrogen atom', correct:0},
        {text:'A planimeter is used to measure height', correct:0},
        {text:'Complex animal life began about 550 million years ago', correct:1},
        {text:'A hologram is two-dimensional', correct:0},
        {text:'The World Wide Web was invented in 1960', correct:0},
        {text:'An anthelion is a kind of insect', correct:0},
        {text:'Holograms were invented in Hungary', correct:1}
    ];

    $scope.answers = [
    ];

    $scope.count = 0;
    $scope.choice = 'null';
    $scope.showQuestion = $scope.questions[0].text;

    $scope.addAnswer = function() {
        $scope.answers.push({correct:$scope.choice});
        if($scope.count>=9){
            $scope.showQuestion = 'Number of correct answers: '+ $scope.calculatePoints();
        }
        else{
            $scope.count++;
            $scope.showQuestion = $scope.questions[$scope.count].text;
        }
    };


    $scope.calculatePoints = function() {
        var n = 0;
        for(var i=0; i<$scope.questions.length; i++){
            var q = $scope.questions[i].correct;
            var a = $scope.answers[i].correct;
            if (q == a){
                n++;
            }
        }
        return n;
    };

}