describe("Answer Quiz Controller", function() {
    var controller;
    var scope;

    beforeEach(module('answerQuizControllers'));

    beforeEach(inject(function($controller, $rootScope, $injector) {
        scope = $rootScope;
        var mockServer = {};
        mockServer.success = function(successFunction) {
        	successFunction({sum: 42});
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

  it("has default values", function() {
    expect(scope.userName).toEqual("");
    expect(scope.userEmail).toEqual("");
  });

  it("computes sum", function() {
  	scope.nextQuestion();
  	scope.nextQuestion();
  	expect(scope.showBox(2)).toEqual(true);
  });
});