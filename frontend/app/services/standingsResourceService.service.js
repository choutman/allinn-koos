(function () {
    'use strict';

    function StandingsResourceService($resource) {

        var Standings = $resource('http://localhost:8080/api/standings', {}, {});

        function getStandings() {
            return Standings.query({}).$promise;
        }

        return {
            getStandings: getStandings
        }
    }

    StandingsResourceService.$inject = ['$resource'];

    angular.module('koosCompetitionApp')
        .factory('standingsResourceService', StandingsResourceService);
})();