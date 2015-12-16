// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'starter.controllers', 'starter.services', 'ngCordova'])

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
      StatusBar.styleDefault();
    }

    function loadJSON(callback) {

      var xobj = new XMLHttpRequest();
      xobj.overrideMimeType("application/json");
      xobj.open('GET', 'config.json', true);
      xobj.onreadystatechange = function () {
        if (xobj.readyState == 4 && xobj.status == "200") {
          // Required use of an anonymous callback as .open will NOT return a value but simply returns undefined in asynchronous mode
          callback(xobj.responseText);
        }
      };
      xobj.send(null);
    }



    loadJSON(function(response) {
      // Parse JSON string into object
      var config_JSON = JSON.parse(response);
      Parse.initialize(config_JSON.APPLICATION_ID, config_JSON.JAVASCRIPT_KEY);

      if(!(ionic.Platform.isIOS() || ionic.Platform.isAndroid())){
        window.fbAsyncInit = function() {
          Parse.FacebookUtils.init({
            appId      : config_JSON.APPLICATION_ID,
            version    : 'v2.5',
            status     : true,  // check Facebook Login status
            xfbml      : true
          });
        };

        (function(d, s, id){
          var js, fjs = d.getElementsByTagName(s)[0];
          if (d.getElementById(id)) {return;}
          js = d.createElement(s); js.id = id;
          js.src = "//connect.facebook.net/en_US/sdk.js";
          fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));
      }
    });


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
      controller: 'LoginCtrl'
    })
    .state('signup', {
      url: '/signup',
      templateUrl: 'templates/signup.html',
      controller: 'LoginCtrl'
    })
    .state('signin', {
      url: '/signin',
      templateUrl: 'templates/signin.html',
      controller: 'LoginCtrl'
    })
    .state('groupList', {
      url: '/group',
      templateUrl: 'templates/groupList.html',
      controller: 'GroupListCtrl'
    })
    .state('groupAdd', {
      url: '/groupAdd',
      templateUrl: 'templates/groupAdd.html',
      controller: 'GroupAddCtrl'
    })
    .state('main', {
      url: '/main/:groupId',
      templateUrl: 'templates/main.html',
      controller: 'MainCtrl'
    });

  $urlRouterProvider.otherwise("/");

});
