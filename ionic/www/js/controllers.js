angular.module('starter.controllers', [])

  .controller("MenuController", function ($scope, $ionicSideMenuDelegate) {

    $scope.logout = function () {
      Parse.User.logOut();

      var currentUser = Parse.User.current();
      alert(currentUser);
    };

    $scope.toggleRight = function() {
      $ionicSideMenuDelegate.toggleRight();
    };

  });

