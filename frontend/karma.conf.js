//jshint strict: false
module.exports = function (config) {
    config.set({

        basePath: './app',

        preprocessors: {
            'components/**/*.html': ['ng-html2js']
        },

        ngHtml2JsPreprocessor: {
            moduleName: 'templates'
        },

        files: [
            '../node_modules/angular/angular.js',
            '../node_modules/angular-route/angular-route.js',
            '../node_modules/angular-resource/angular-resource.js',
            '../node_modules/angular-filter/dist/angular-filter.js',
            '../node_modules/angular-material/angular-material.js',
            '../node_modules/angular-animate/angular-animate.js',
            '../node_modules/angular-aria/angular-aria.js',
            '../node_modules/angular-material-data-table/dist/md-data-table.js',
            '../node_modules/angular-mocks/angular-mocks.js',
            'app.js',
            'components/**/*.js',
            'components/**/*.html'
        ],

        autoWatch: true,

        frameworks: ['jasmine'],

        browsers: ['Chrome'],

        plugins: [
            'karma-chrome-launcher',
            'karma-jasmine',
            'karma-junit-reporter',
            'karma-ng-html2js-preprocessor'
        ],

        junitReporter: {
            outputFile: 'test_out/unit.xml',
            suite: 'unit'
        }

    });
};
