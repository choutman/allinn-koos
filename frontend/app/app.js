'use strict';

// Declare app level module which depends on views, and components
angular.module('koosCompetitionApp', [
    'ngRoute',
    'ngResource',
    'koosCompetitionApp.version',
    'ngMaterial',
    'md.data.table'
]).config(['$locationProvider', '$routeProvider', function ($locationProvider, $routeProvider) {
    $locationProvider.hashPrefix('!');

    $routeProvider.when('/standings', {
        templateUrl: 'standings/standings.html',
        controller: 'StandingsController'
    }).otherwise({redirectTo: '/standings'});
}]);