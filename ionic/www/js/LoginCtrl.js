angular.module('starter').controller('LoginCtrl', function($scope, $state) {

  $scope.data = {};

  $scope.Register = function(){
    // setup an abstract state for the tabs directive
    //Create a new user on Parse
    var user = new Parse.User();
    user.set("username", $scope.data.username);
    user.set("birthDate", $scope.data.birthDate);
    user.set("password", "motdepasse");

    // other fields can be set just like with Parse.Object
    user.set("somethingelse", "like this!");

    user.signUp(null, {
      success: function(user) {
        // Hooray! Let them use the app now.
        alert("success!");
      },
      error: function(user, error) {
        // Show the error message somewhere and let the user try again.
        alert("Error: " + error.code + " " + error.message);

      }
    });
  };

  $scope.loginEmail = function(){
    Parse.User.logIn($scope.data.username, $scope.data.password, {
      success: function(user) {
        // Do stuff after successful login.
        console.log(user);
        $state.go('groupList');
      },
      error: function(user, error) {
        // The login failed. Check error to see why.
        alert("error!");
      }
    });
  };

});
