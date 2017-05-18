'use strict';

angular.module('koosCompetitionApp.version', [
  'koosCompetitionApp.version.interpolate-filter',
  'koosCompetitionApp.version.version-directive'
])

.value('version', '0.1.0');
