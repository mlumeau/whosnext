// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'starter.controllers', 'starter.services', 'ngCordova', 'ionic-datepicker', 'firebase'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);

    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleLightContent();
    }

  });
})


.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider
    .state('login', {
      url: '/',
      templateUrl: 'templates/login.html',
      controller: 'LoginCtrl',
      controllerAs: 'login'
    })
    .state('groupList', {
      url: '/group',
      templateUrl: 'templates/groupList.html',
      controller: 'GroupListCtrl',
      controllerAs: 'groupList'
    })
    .state('groupAdd', {
      url: '/groupAdd',
      templateUrl: 'templates/groupAdd.html',
      controller: 'GroupAddCtrl',
      controllerAs: 'groupAdd'
    })
    .state('main', {
      url: '/main/:groupId',
      templateUrl: 'templates/main.html',
      controller: 'MainCtrl',
      controllerAs: 'maint'
    });

  $urlRouterProvider.otherwise("/");

});


