angular.module('quizControllers')
.controller('FrontCtrl', ['$scope', '$http', '$routeParams',
    function($scope, $http, $routeParams) {
		
		$scope.adminView = false;	
		
		$scope.setAdminView = function(show){
			$scope.adminView = show;
		};
		
}]);
