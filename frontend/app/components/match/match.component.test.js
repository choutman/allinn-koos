/**
 * Created by choutman on 24/05/2017.
 */
describe('Components', function () {
    beforeEach(angular.mock.module("koosCompetitionApp"));

    describe('match', function () {
        var $compile;
        var $scope;

        beforeEach(module('templates'));

        beforeEach(inject(function(_$compile_, _$rootScope_) {
            $compile = _$compile_;
            $scope = _$rootScope_.$new();
        }));

        it('should not display anything when match is null', inject(function () {
            var element = $compile("<match></match>")($scope);

            $scope.$digest();

            var mdCard = element.find('md-card');

            expect(mdCard.hasClass("ng-hide")).toBe(true);
        }));
    });

});