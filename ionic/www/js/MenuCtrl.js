angular.module('starter.controllers', [])

  .controller("MenuController", function ($ionicSideMenuDelegate) {

    var vm = this;

    vm.toggleRight = function() {
      $ionicSideMenuDelegate.toggleRight();
    };

  });

