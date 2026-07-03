'use strict';
module.exports = function (environment) {
  const ENV = {
    modulePrefix: 'frontend',
    environment,
    rootURL: '/',
    locationType: 'hash',
    EmberENV: {
    EXTEND_PROTOTYPES: false,
    FEATURES: {},
  },
  APP: {
    API_HOST: 'http://localhost:8080',
  },
  };
  if (environment === 'development') {
    ENV.APP.LOG_RESOLVER = true;
  }
  if (environment === 'test') {
    ENV.rootURL = '/';
    ENV.locationType = 'none';
    ENV.APP.LOG_ACTIVE_GENERATION = false;
    ENV.APP.LOG_VIEW_LOOKUPS = false;
    ENV.APP.rootElement = '#ember-testing';
    ENV.APP.autoboot = false;
  }

  if (environment === 'production') {
    ENV.APP.API_HOST = '';
  }
  return ENV;
};