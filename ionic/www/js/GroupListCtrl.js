angular.module('starter').controller('GroupListCtrl', function ($scope, $state, $ionicPopup) {


  $scope.$on('$ionicView.beforeEnter', function() {
    var Group = Parse.Object.extend("Group");
    var queryObject = new Parse.Query(Group);

    queryObject.find({
      success: function (results) {
        $scope.$apply(function () {
          $scope.groupList = results;
        });
        for (var i = 0; i < results.length; i++) {
          // Iteratoration for class object.
        }
      },
      error: function (error) {
        alert("Error: " + error.code + " " + error.message);
      }
    });
  });


 $scope.deleteGroup = function(item){

   var query = new Parse.Query("Group");
   query.get(item.id, {
     success: function(parseGroup) {
       parseGroup.destroy({
         success: function(parseGroup) {
           // The object was deleted from the Parse Cloud.
           $window.location.reload(true);

         },
         error: function(parseGroup, error) {
           // The delete failed.
           // error is a Parse.Error with an error code and message.
         }
       });     },
     error: function(parseGroup, error) {
       // The object was not retrieved successfully.
       // error is a Parse.Error with an error code and message.
     }
   });

 },

  $scope.shouldShowDelete = false;

  $scope.showDelete = function(){
    $scope.shouldShowDelete = !$scope.shouldShowDelete;
  };

  $scope.showPopupGroupCreate = function() {
    $scope.data = {}

    // An elaborate, custom popup
    var myPopup = $ionicPopup.show({
      templateUrl: "templates/groupAdd.html",
      title: 'Create a new group',
      scope: $scope,
      cssClass: 'GroupCreate',
      buttons: [
        { text: 'Cancel',
          type : 'button-cancel'
        },
        {
          text: '<b>Create</b>',
          type: 'button-positive',
          onTap: function(e) {
            if (!$scope.data.date && !$scope.data.name) {
              //don't allow the user to close unless he enters wifi password
              e.preventDefault();
            } else {
              return $scope.data;
            }
          }
        },
      ]
    });
    myPopup.then(function(data) {
      console.log('Tapped!', data);
      createParseGroup(data);
    });

  };

  var createParseGroup = function(data){
    var newParseGroup = new Parse.Object("Group");
    newParseGroup.set("name", $scope.data.name);
    newParseGroup.set("frequency", 1);

    newParseGroup.save(null, {
      success: function(newParseGroup) {
        // Execute any logic that should take place after the object is saved.
        $state.go('groupList',null,{reload:true});
      },
      error: function(newParseGroup, error) {
        // Execute any logic that should take place if the save fails.
        // error is a Parse.Error with an error code and message.
      }
  });
  };

  $scope.getCurrentUser = function(){
    alert(Parse.User.current());
  };


});
