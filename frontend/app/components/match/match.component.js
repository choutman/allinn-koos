(function () {
    'use strict';

    function MatchController() {
    }

    angular.module('koosCompetitionApp')
        .component('match', {
            templateUrl: 'components/match/match.html',
            controller: MatchController,
            bindings: {
                match: '<'
            }
        });
})();