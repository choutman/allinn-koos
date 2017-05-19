(function () {
    'use strict';

    function StandingsController(standingsResourceService) {

        var vm = this;
        vm.positions = [];

        function getStandings() {
            standingsResourceService.getStandings().then(function (result) {
                vm.positions = result;
            });
        }

        getStandings();
    }

    StandingsController.$inject = ['standingsResourceService'];

    angular.module('koosCompetitionApp')
        .controller('StandingsController', StandingsController);
})();