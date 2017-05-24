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

        it('should not display anything when match is not a JSON object', inject(function () {
            var element = $compile("<match match='match'></match>")($scope);
            $scope.match = "not a JSON";

            $scope.$digest();

            var mdCard = element.find('md-card');

            expect(mdCard.hasClass("ng-hide")).toBe(true);
        }));

        it('should not display anything when match is missing a value in the JSON object', inject(function () {
            var element = $compile("<match match='match'></match>")($scope);
            $scope.match = {'homeTeamName': 'team1', 'dateTime': '20170101T12:00:00+0100'};

            $scope.$digest();

            var mdCard = element.find('md-card');

            expect(mdCard.hasClass("ng-hide")).toBe(true);
        }));

        it('should display a match if all variables are present', inject(function () {
            var element = $compile("<match match='match'></match>")($scope);
            $scope.match = {'homeTeamName': 'team1', 'awayTeamName': 'team2', 'dateTime': '20170101T12:00:00+0100'};

            $scope.$digest();

            var mdCardTitleTextElement = element.find('span');

            expect(mdCardTitleTextElement[0].innerHTML).toEqual("team1");
            expect(mdCardTitleTextElement[1].innerHTML).toContain("VS");
            expect(mdCardTitleTextElement[2].innerHTML).toEqual("team2");
        }));
    });

});