angular.module('starter').controller('GroupListCtrl', function ($scope, $state, $ionicPopup) {

  var vm = this;

  $scope.$on('$ionicView.beforeEnter', function() {

    vm.List = [
      {
        "name": "Croissant",
        "type": "classic"
      },
      {
        "name": "Bières",
        "type": "classic"
      }];
  });


 vm.deleteGroup = function(item){

 },

  vm.shouldShowDelete = false;

  vm.showDelete = function(){
    vm.shouldShowDelete = !vm.shouldShowDelete;
  };

  vm.showPopupGroupCreate = function() {
    vm.data = {}

    // An elaborate, custom popup
    var myPopup = $ionicPopup.show({
      templateUrl: "templates/groupAdd.html",
      title: 'Create a new group',
      scope: vm,
      cssClass: 'GroupCreate',
      buttons: [
        { text: 'Cancel',
          type : 'button-cancel'
        },
        {
          text: '<b>Create</b>',
          type: 'button-positive',
          onTap: function(e) {
            if (!vm.data.date && !vm.data.name) {
              //don't allow the user to close unless he enters wifi password
              e.preventDefault();
            } else {
              return vm.data;
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

  vm.getCurrentUser = function(){
  };


});
