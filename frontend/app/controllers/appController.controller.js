/**
 * Created by choutman on 22/05/2017.
 */
(function(){
    'use strict';

    function AppController($scope) {
        $scope.currentNavItem = 'standings';
    }

    angular.module('koosCompetitionApp')
        .controller('AppController', AppController);
})();