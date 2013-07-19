describe("Answer Quiz Controller", function() {
    var controller;
    var scope;

    beforeEach(module('quizControllers'));

    beforeEach(inject(function($controller, $rootScope, $injector) {
        scope = $rootScope;
        var mockServer = {};
        mockServer.success = function(successFunction) {
        	successFunction({});
        	return mockServer;
        };
        mockServer.error = function(errorFunction) {
        	return mockServer;
        };
        var mockhttp = function(config) {
        	return mockServer;
        };

        controller = $controller('AnswerQuizCtrl', {
            $scope : scope,
            $http: mockhttp
        });

    }));

  it("has no value for name and email yet", function() {
    expect(scope.userName).toEqual("");
    expect(scope.userEmail).toEqual("");
  });

  it("increments currentQuestion private variable", function() {
  	scope.nextQuestion();
  	scope.nextQuestion();
  	scope.nextQuestion();
  	expect(scope.isCurrent(3)).toEqual(true);
  });
  
  it("decrements currentQuestion private variable", function() {
	  	scope.prevQuestion();
	  	scope.prevQuestion();
	  	scope.prevQuestion();
	  	expect(scope.isCurrent(-3)).toEqual(true);
   });
  
  it("updates the the answers object", function() {
	  	scope.updateAnswers(1,2);
	  	scope.updateAnswers(2,3);
	  	scope.updateAnswers(3,4);
	  	scope.updateAnswers(4,1);
	  	expect(scope.answers["q1"]).toEqual(2);
	  	expect(scope.answers["q2"]).toEqual(3);
	  	expect(scope.answers["q3"]).toEqual(4);
	  	expect(scope.answers["q4"]).toEqual(1);
	  	expect(scope.answers["1"]).toEqual(undefined);
	  	expect(scope.answers["q5"]).toEqual(undefined);
   });
   
   it("submittes the info to the server", function() {
	   scope.quiz.quizId = 1;
	   scope.userName = "Ola Nordmann";
	   scope.userEmail = "ola@example.com";
	   scope.updateAnswers(1,2);
	   scope.updateAnswers(2,3);
	   scope.updateAnswers(3,4);
	   scope.updateAnswers(4,1);
	   scope.submitQuiz();
	   expect(scope.isCurrent(1)).toEqual(true);
 });
  

});
