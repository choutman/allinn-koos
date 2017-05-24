'use strict';

describe('koosCompetitionApp.version module', function() {
  beforeEach(module('koosCompetitionApp.version'));

  describe('version service', function() {
    it('should return current version', inject(function(version) {
      expect(version).toEqual('0.1.0');
    }));
  });
});
