describe("Admin Controller", function() {
    var controller;
    var scope;
    var route;

    beforeEach(module('quizControllers'));

    beforeEach(inject(function($controller, $rootScope, $injector, $route) {
        scope = $rootScope;
        route = $route;
        route.current = {templateUrl: "templates/adminAddQuiz.html"};

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
        
        controller = $controller('AdminCtrl', {
            $scope : scope,
            $route : route,
            $http: mockhttp
        });

    }));

  it("has no value for name and description yet", function() {
	  expect(scope.newquiz.quizName).toEqual("");
	  expect(scope.newquiz.quizDesc).toEqual("");
  });

  it("add two questions to the quiz", function() {
	  scope.addQuestion();
	  scope.addQuestion();
	  expect(scope.newquiz.questions.length).toEqual(3);
  });
  
  it("add two questions, then remove one question from the quiz, ", function() {
	  scope.addQuestion();
	  scope.addQuestion();
	  expect(scope.newquiz.questions.length).toEqual(3);
  });
  
  it("add two alternatives to the first question", function(){
	  scope.addAlternative(scope.newquiz.questions[0]);
	  scope.addAlternative(scope.newquiz.questions[0]);
	  expect(scope.newquiz.questions[0].alternatives.length).toEqual(3);
  });
  
  it("add two alternatives, then remove one to the first question", function(){
	  scope.addAlternative(scope.newquiz.questions[0]);
	  scope.addAlternative(scope.newquiz.questions[0]);
	  scope.removeAlternative(scope.newquiz.questions[0], 2);
	  expect(scope.newquiz.questions[0].alternatives.length).toEqual(2);
  });
  
  it("it shows respondents by updating the showRespondentState", function(){
	  scope.showRespondents(1);
	  scope.showRespondents(2);
	  expect(scope.showRespondentState).toEqual({q1:true, q2:true});
	  scope.hideRespondents(1);
	  expect(scope.showRespondentState).toEqual({q1:false, q2:true});
  });
  
});
