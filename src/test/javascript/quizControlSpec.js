describe("Quiz Cotroller", function() {
    var controller;
    var scope;

    beforeEach(inject(function($controller, $rootScope, $injector) {
        scope = $rootScope;

        controller = $controller('QuizCtrl', {
            $scope : scope
        });

    }));

  it("has default values", function() {
    expect(scope.numberOne).toEqual(3);
    expect(scope.numberTwo).toEqual(2);
  });
});