describe("Quiz Cotroller", function() {
    var controller;
    var scope;

    beforeEach(module('quizControllers'));

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

        controller = $controller('QuizCtrl', {
            $scope : scope,
            $http: mockhttp
        });

    }));

  it("has default values", function() {
    expect(scope.numberOne).toEqual(3);
    expect(scope.numberTwo).toEqual(2);
  });

  it("computes sum", function() {
  	scope.numberOne = 4;
  	scope.addNumbers();
  	expect(scope.sum).toEqual(6);
  	expect(scope.serversum).toEqual(42);
  });
});