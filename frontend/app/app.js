'use strict';

// Declare app level module which depends on views, and components
angular.module('koosCompetitionApp', [
    'ngRoute',
    'ngResource',
    'angular.filter',
    'koosCompetitionApp.version',
    'ngMaterial',
    'md.data.table'
]).config(['$locationProvider', '$routeProvider', function ($locationProvider, $routeProvider) {
    $locationProvider.hashPrefix('!');

    $routeProvider.when('/standings', {
        templateUrl: 'standings/standings.html',
    }).when('/schedule', {
        templateUrl: 'schedule.html',
    }).otherwise({redirectTo: '/standings'});
}]);