(function() {
	angular.module('quizControllers', []);

	var bootstrap;
	bootstrap = function() {
		angular.module('quizzical', ['quizControllers']).
			config(['$routeProvider', "$httpProvider", function($routeProvider, $httpProvider) {
				$routeProvider.
					when('/', {
						templateUrl: 'templates/loginpage.html',
						controller: 'FrontCtrl'
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
					when('/admin/changepw', {
						templateUrl: 'templates/adminChangePassword.html',
						controller: 'AdminCtrl'
					}).
					when('/admin/results', {
						templateUrl: 'templates/adminResults.html',
						controller: 'AdminCtrl'
					});

				var interceptor = ['$rootScope', '$q', function (scope, $q) {
					function success(response) {
						return response;
					}
					function error(response) {
						var status = response.status;
						if (status === 401) {
							window.location = "#/";
							return;
						}
						return $q.reject(response);
					}
					return function(promise) {
						return promise.then(success, error);
					};
				}];
				$httpProvider.responseInterceptors.push(interceptor);

			}]);


		angular.bootstrap(document,['quizzical']);

	};

	bootstrap();


}());
