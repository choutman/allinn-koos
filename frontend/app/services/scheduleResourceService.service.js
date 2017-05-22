(function () {
    'use strict';

    function ScheduleResourceService($resource) {

        var Schedule = $resource('http://localhost:8082/api/schedule', {}, {});

        function getSchedule() {
            return Schedule.query({}).$promise;
        }

        return {
            getSchedule: getSchedule
        }
    }

    ScheduleResourceService.$inject = ['$resource'];

    angular.module('koosCompetitionApp')
        .factory('scheduleResourceService', ScheduleResourceService);
})();