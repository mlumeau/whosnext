angular.module('starter').controller('LoginCtrl', function($scope, $state, $cordovaFacebook, $timeout, $ionicLoading, Auth) {

  $scope.data = {};

  $scope.imgAvatar = "";

  $scope.birthdateSelected = false;

  $scope.bithdateFistSelection = function(){
    $scope.birthdateSelected = true;
    $timeout(function() {
      cordova.plugins.Keyboard.close();
      document.getElementById('birthdate').click();
    }, 0);
  };

  $scope.datepickerObject = {
    titleLabel: 'Select your bithdate',  //Optional
    todayLabel: 'Today',  //Optional
    closeLabel: 'Close',  //Optional
    setLabel: 'Set',  //Optional
    setButtonType : 'button-assertive',  //Optional
    todayButtonType : "ng-hide",  //Optional
    closeButtonType : 'button-assertive',  //Optional
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
      $scope.datepickerObject.inputDate = val;
    }
  };

  $scope.choosePicture = function() {
    var options =   {
      quality: 50,
      destinationType: Camera.DestinationType.FILE_URI,
      sourceType: 0,      // 0:Photo Library, 1=Camera, 2=Saved Photo Album
      encodingType: 0     // 0=JPG 1=PNG
    }
    navigator.camera.getPicture(onSuccess,onFail,options);
  }

  $scope.takePicture = function() {
    var options =   {
      quality: 50,
      destinationType: Camera.DestinationType.FILE_URI,
      sourceType: 1,      // 0:Photo Library, 1=Camera, 2=Saved Photo Album
      encodingType: 0     // 0=JPG 1=PNG
    }
    navigator.camera.getPicture(onSuccess,onFail,options);
  }

  $scope.choosePictureSelection = function() {
    window.plugins.actionsheet.show({
      buttonLabels: ["Take a picture", "Select Picture from your library"],
      'addCancelButtonWithLabel': 'Cancel',
      'androidEnableCancelButton' : true, // default false
      'winphoneEnableCancelButton' : true, // default false
      title: "Choose the way to select your picture"
    }, function (buttonIndex) {
      if(buttonIndex == 1){
        $scope.takePicture();
      }
      else if(buttonIndex == 2) {
        $scope.choosePicture();
      }
    });
  }

  var onSuccess = function(imageData) {
    $scope.$apply(function () {
      $scope.imgAvatar = imageData;
    });
  };
  var onFail = function(e) {
    console.log("On fail " + e);
  }

  $scope.Register = function(){
    // setup an abstract state for the tabs directive
    //Create a new user on Parse
    var user = new Parse.User();

    var file = new Parse.File("myfile.png", $scope.imgAvatar);
    file.save().then(function() {
// The file has been saved to Parse.
    }, function(error) {
// The file either could not be read, or could not be saved to Parse.
    });

    user.set("username", $scope.data.username);
    user.set("birthDate", $scope.datepickerObject.inputDate);
    user.set("password", "motdepasse");
    user.set("avatar", file);

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

  $scope.loginFacebook = function() {
    Auth.$authWithOAuthRedirect("facebook");
  };

  $scope.loginFaceboo = function(){

    //Browser Login
    if(!(ionic.Platform.isIOS() || ionic.Platform.isAndroid())){

      Parse.FacebookUtils.logIn(null, {
        success: function(user) {
          console.log(user);
          if (!user.existed()) {
            alert("User signed up and logged in through Facebook!");
          } else {
            alert("User logged in through Facebook!");
          }
        },
        error: function(user, error) {
          alert("User cancelled the Facebook login or did not fully authorize.");
        }
      });

    }
    //Native Login
    else {

      $cordovaFacebook.login(["public_profile", "email"]).then(function(success){

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

        Parse.FacebookUtils.logIn(facebookAuthData, {
          success: function(user) {
            console.log(user);
            if (!user.existed()) {
              alert("User signed up and logged in through Facebook!");
            } else {
              alert("User logged in through Facebook!");
            }
          },
          error: function(user, error) {
            alert("User cancelled the Facebook login or did not fully authorize.");
          }
        });

      }, function(error){
        console.log(error);
      });

    }

  };


  // This method is executed when the user press the "Sign in with Google" button
  $scope.googleSignIn = function() {
    $ionicLoading.show({
      template: 'Logging in...'
    });

    window.plugins.googleplus.login(
      {},
      function (user_data) {
        var user = new Parse.User();

       // var file = new Parse.File("benoit.png", {base64 : $scope.imgAvatar});
        var parseFile = new Parse.File("mypic.jpg", {base64: $scope.imgAvatar.toString('base64')} );

        // $scope.gotPic($scope.imgAvatar);

        parseFile.save().then(function() {
          console.log("file has been save in Parse");
          // The file has been saved to Parse.
        }, function(error) {
          console.log("Error when saving file in Parse");
          // The file either could not be read, or could not be saved to Parse.
        });

        user.set("username", $scope.data.username);
        user.set("birthDate", $scope.datepickerObject.inputDate);
        user.set("email", user_data.email);
        user.set("avatar", parseFile);
        user.set("password", "motdepasse");

        user.signUp(null, {
          success: function(user) {
            // Hooray! Let them use the app now.
            alert("success!");
            $ionicLoading.hide();
            $state.go('groupList');
          },
          error: function(user, error) {
            // Show the error message somewhere and let the user try again.
            $ionicLoading.hide();
            alert("Error: " + error.code + " " + error.message);
          }
        });

      },
      function (msg) {
        $ionicLoading.hide();
      }
    );
  };

  $scope.gotPic = function(data) {

    window.resolveLocalFileSystemURL(data, function(entry) {

      var reader = new FileReader();

      reader.onloadend = function(evt) {
        var byteArray = new Uint8Array(evt.target.result);
        var output = new Array( byteArray.length );
        var i = 0;
        var n = output.length;
        while( i < n ) {
          output[i] = byteArray[i];
          i++;
        }
        var parseFile = new Parse.File("mypic.jpg", output);

        parseFile.save().then(function(ob) {
          navigator.notification.alert("Got it!", null);
          console.log(JSON.stringify(ob));
        }, function(error) {
          console.log("Error");
          console.log(error);
        });

      }

      reader.onerror = function(evt) {
        console.log('read error');
        console.log(JSON.stringify(evt));
      }

      entry.file(function(s) {
        reader.readAsArrayBuffer(s);
      }, function(e) {
        console.log('ee');
      });

    });

  }

});
