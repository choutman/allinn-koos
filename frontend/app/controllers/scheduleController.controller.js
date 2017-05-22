(function () {
    'use strict';

    function ScheduleController(scheduleResourceService) {
        var ctrl = this;

        ctrl.schedule = [];

        function getSchedule() {
            scheduleResourceService.getSchedule().then(function (result) {
                ctrl.schedule = result;
            });
        }

        getSchedule();
    }

    ScheduleController.$inject = ['scheduleResourceService'];

    angular.module('koosCompetitionApp')
        .controller('ScheduleController', ScheduleController);
})();