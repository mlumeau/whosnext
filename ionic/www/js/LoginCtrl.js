angular.module('starter').controller('LoginCtrl', function ($scope, $state, $cordovaFacebook, $timeout, $ionicLoading, Auth) {

  var vm = this;

  vm.data = {};

  vm.imgAvatar = "";

  vm.birthdateSelected = false;

  vm.bithdateFistSelection = function () {
    vm.birthdateSelected = true;
    $timeout(function () {
      cordova.plugins.Keyboard.close();
      document.getElementById('birthdate').click();
    }, 0);
  };

  vm.datepickerObject = {
    titleLabel: 'Select your bithdate',  //Optional
    todayLabel: 'Today',  //Optional
    closeLabel: 'Close',  //Optional
    setLabel: 'Set',  //Optional
    setButtonType: 'button-assertive',  //Optional
    todayButtonType: "ng-hide",  //Optional
    closeButtonType: 'button-assertive',  //Optional
    inputDate: new Date(),  //Optional
    mondayFirst: true,  //Optional
    templateType: 'popup', //Optional
    showTodayButton: 'false', //Optional
    modalHeaderColor: 'bar-positive', //Optional
    modalFooterColor: 'bar-positive', //Optional
    callback: function (val) {  //Mandatory
      datePickerCallback(val);
    },
    dateFormat: 'dd-MM-yyyy', //Optional
    closeOnSelect: false //Optional
  };

  var datePickerCallback = function (val) {
    if (typeof(val) === 'undefined') {
      console.log('No date selected');
    } else {
      console.log('Selected date is : ', val)
      vm.datepickerObject.inputDate = val;
    }
  };

  vm.choosePicture = function () {
    var options = {
      quality: 50,
      destinationType: Camera.DestinationType.FILE_URI,
      sourceType: 0,      // 0:Photo Library, 1=Camera, 2=Saved Photo Album
      encodingType: 0     // 0=JPG 1=PNG
    };
    navigator.camera.getPicture(onSuccess, onFail, options);
  };

  vm.takePicture = function () {
    var options = {
      quality: 50,
      destinationType: Camera.DestinationType.FILE_URI,
      sourceType: 1,      // 0:Photo Library, 1=Camera, 2=Saved Photo Album
      encodingType: 0     // 0=JPG 1=PNG
    };
    navigator.camera.getPicture(onSuccess, onFail, options);
  };

  vm.choosePictureSelection = function () {
    window.plugins.actionsheet.show({
      buttonLabels: ["Take a picture", "Select Picture from your library"],
      'addCancelButtonWithLabel': 'Cancel',
      'androidEnableCancelButton': true, // default false
      'winphoneEnableCancelButton': true, // default false
      title: "Choose the way to select your picture"
    }, function (buttonIndex) {
      if (buttonIndex == 1) {
        vm.takePicture();
      }
      else if (buttonIndex == 2) {
        vm.choosePicture();
      }
    });
  };

  var onSuccess = function (imageData) {
    $scope.$apply(function () {
      vm.imgAvatar = imageData;
    });
  };
  var onFail = function (e) {
    console.log("On fail " + e);
  };

  vm.loginFaceboo = function () {
    Auth.$authWithOAuthRedirect("facebook");
  };

  vm.loginFacebook = function () {

    $cordovaFacebook.login(["public_profile", "email"]).then(function (success) {

      console.log(success);

      //Need to convert expiresIn format from FB to date
      var expiration_date = new Date();
      expiration_date.setSeconds(expiration_date.getSeconds() + success.authResponse.expiresIn);
      expiration_date = expiration_date.toISOString();

      var facebookAuthData = {
        "id": success.authResponse.userID,
        "access_token": success.authResponse.accessToken,
        "expiration_date": expiration_date
      };

    }, function (error) {
      console.log(error);
    });

  };


});
