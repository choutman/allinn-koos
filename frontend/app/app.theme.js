/**
 * Created by choutman on 23/05/2017.
 */

(function () {
    'use strict';

    angular.module('koosCompetitionApp')
        .config(function($mdThemingProvider) {

            var allInnPrimaryPalette = {
                '50': '#ff6dc3',
                '100': '#ff54b9',
                '200': '#ff3aaf',
                '300': '#ff21a4',
                '400': '#ff079a',
                '500': '#ed008c',
                '600': '#d3007d',
                '700': '#ba006e',
                '800': '#a0005f',
                '900': '#870050',
                'A100': '#ff87ce',
                'A200': '#ffa0d8',
                'A400': '#ffbae3',
                'A700': '#6d0041',
                'contrastDefaultColor': 'light'
            };
            $mdThemingProvider
                .definePalette('allInnPrimaryPalette',
                    allInnPrimaryPalette);

            var allInnBackgroundPalette = {
                '50': '#ffffff',
                '100': '#ffffff',
                '200': '#ffffff',
                '300': '#ffffff',
                '400': '#ffffff',
                '500': '#fff',
                '600': '#f2f2f2',
                '700': '#e6e6e6',
                '800': '#d9d9d9',
                '900': '#cccccc',
                'A100': '#ffffff',
                'A200': '#ffffff',
                'A400': '#ffffff',
                'A700': '#bfbfbf'
            };
            $mdThemingProvider
                .definePalette('allInnBackgroundPalette',
                    allInnBackgroundPalette);

        $mdThemingProvider.theme('default')
            .primaryPalette('allInnPrimaryPalette')
            .accentPalette('deep-purple')
            .backgroundPalette('allInnBackgroundPalette');
    });
})();