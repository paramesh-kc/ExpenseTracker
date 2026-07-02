'use strict';
module.exports = function (environment) {
  const ENV = {
    modulePrefix: 'frontend',
    environment,
    rootURL: '/',
    locationType: 'history',
    EmberENV: {
    EXTEND_PROTOTYPES: false,
    FEATURES: {},
  },
  APP: {
    API_HOST: 'http://localhost:8080',
  },
  };
  if (environment === 'production') {
    ENV.APP.API_HOST = 'https://api.yourapp.com';
  }
  return ENV;
};