(function() {
    angular.module('quizControllers', []);

    var bootstrap;
    bootstrap = function() {
        angular.module('quizzical', ['quizControllers']).
        config(['$routeProvider', function($routeProvider) {
                $routeProvider.
                    when('/', {
                        templateUrl: 'templates/frontpage.html', 
                        controller: ''
                    }).
                    when('/answerQuiz/:quizId', {
                        templateUrl: 'templates/answerQuizView.html', 
                        controller: 'AnswerQuizCtrl'
                    }).
	                when('/admin/', {
	                    templateUrl: 'templates/adminOverview.html', 
	                    controller: 'AdminCtrl'
	                }).
	                when('/admin/overview', {
	                    templateUrl: 'templates/adminOverview.html', 
	                    controller: 'AdminCtrl'
	                }).
	                when('/admin/addquiz/:quizId', {
	                    templateUrl: 'templates/adminAddQuiz.html', 
	                    controller: 'AdminCtrl'
	                }).
	                when('/admin/addquiz', {
	                    templateUrl: 'templates/adminAddQuiz.html', 
	                    controller: 'AdminCtrl'
	                }).
	                when('/admin/results', {
	                    templateUrl: 'templates/adminResults.html', 
	                    controller: 'AdminCtrl'
	                });                
        }]);
        
        angular.bootstrap(document,['quizzical']); 
        
    };

    bootstrap();


}());
