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
                    });
        }]);
            
        angular.bootstrap(document,['quizzical']); 
        
    };

    bootstrap();


}());
