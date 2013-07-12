(function() {
    angular.module('quizControllers', []);

    var bootstrap;
    bootstrap = function() {
        angular.module('quizzical', ['quizControllers']).
        config(['$routeProvider', function($routeProvider) {
                $routeProvider.
                    when('/', {
                        templateUrl: 'templates/testTemplate.html', 
                        controller: 'QuizCtrl'
                    }).
                    when('/answerQuiz', {
                        templateUrl: 'templates/userview.html', 
                        controller: 'AnswerQuizCtrl'
                    });
        }]);
            
        angular.bootstrap(document,['quizzical']); 
        
    };

    bootstrap();


}());
