angular.module('starter.services', [])

  .factory("Auth", function($firebaseArray) {
    var usersRef = new Firebase("https//whoisnext.firebaseio.com/Group");
    return $firebaseArray(usersRef);
  })
