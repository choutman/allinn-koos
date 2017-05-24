/**
 * Created by choutman on 24/05/2017.
 */
describe('match component', function () {
    var $componentController;

    beforeEach(module('koosCompetitionApp'));

    beforeEach(inject(function(_$componentController_) {
        $componentController = _$componentController_;
    }));

    it('should not display anything when match is null', function () {
        var controller = $componentController('match', null, {
            match: null
        });

        expect(controller.match).toBeNull();
    })
});