angular.module('starter').controller('GroupListCtrl', function ($scope, $state, $ionicPopup) {


  $scope.$on('$ionicView.beforeEnter', function() {

    $scope.groupList = [
      {
        "name": "Croissant",
        "type": "classic"
      },
      {
        "name": "Bières",
        "type": "classic"
      }];
  });


 $scope.deleteGroup = function(item){

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
    });

  };

  var createParseGroup = function(data){

  };

  $scope.getCurrentUser = function(){
  };


});
