angular.module('starter').controller('GroupListCtrl', function ($scope, $state) {

  $scope.groupList = [
    {
      "id": 1,
      "name": "Croissant",
      "type": "classic"
    },
    {
      "id": 2,
      "name": "Bières",
      "type": "classic"
    }];

  $scope.moveItem = function(item, fromIndex, toIndex) {
    //Move the item in the array
    $scope.groupList.splice(fromIndex, 1);
    $scope.groupList.splice(toIndex, 0, item);
  };

  $scope.shouldShowDelete = false;
  $scope.shouldShowReorder = false;

  $scope.showDelete = function(){
    $scope.shouldShowDelete = !$scope.shouldShowDelete;
  }

  $scope.showReorder = function(){
    $scope.shouldShowReorder = !$scope.shouldShowReorder;
  }


});
