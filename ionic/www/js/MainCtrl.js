angular.module('starter').controller('MainCtrl', function ($scope, $state, $stateParams) {

  console.log('init');

  /* Flip Effect */
  var jFlip = $('.flip').find('li');

  function flip()
  {
    if (jFlip.length >= 1)
    {
      var oNextImg = jFlip[Math.floor(Math.random() * jFlip.length)];
      $(oNextImg).toggleClass('transition');
    }
  }

// loop
  $('img:first-child').on('transitionend webkitTransitionEnd mozTransitionEnd otransition MSTransitionEnd', flip);

// start
  $scope.startAnim = function(){
    window.setTimeout(flip, 5000);

  };



});

