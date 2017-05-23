/**
 * Created by choutman on 22/05/2017.
 */
(function(){
    'use strict';

    function AppController($scope, $location) {
        $scope.currentNavItem = $location.path();
    }

    angular.module('koosCompetitionApp')
        .controller('AppController', AppController);
})();