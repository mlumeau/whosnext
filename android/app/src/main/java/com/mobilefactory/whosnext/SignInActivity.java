package com.mobilefactory.whosnext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobilefactory.whosnext.model.parse.ParseUser;
import com.mobilefactory.whosnext.utils.Animations;
import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private final static String GPLUS_SCOPE = "https://www.googleapis.com/auth/plus.login";
    public static final String VALIDATE_GOOGLE_SIGN_IN_TOKEN = "validateGoogleSignInToken";
    public static final String ERROR_NEW_USER = "new_user";


    private final static String mScopes = "oauth2:" + GPLUS_SCOPE;

    // UI references.
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* Activity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        boolean isSignedIn = getSharedPreferences(getString(R.string.login_prefs), Context.MODE_PRIVATE).getBoolean(getString(R.string.signed_in_key),false);

        if(isSignedIn){
            signIn();
        }
        else{
            initUI();
        }
    }

    private void initUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animations.reveal(findViewById(R.id.sign_in_button), null);

                // Customize sign-in button. The sign-in button can be displayed in
                // multiple sizes and color schemes. It can also be contextually
                // rendered based on the requested scopes. For example. a red button may
                // be displayed when Google+ scopes are requested, but a white button
                // may be displayed when only basic profile is requested. Try adding the
                // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
                // difference.
                SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
                signInButton.setSize(SignInButton.SIZE_STANDARD);
                signInButton.setScopes(gso.getScopeArray());

                findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signIn();
                    }
                });
            }
        });
    }




    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void signIn() {
        Animations.hide(findViewById(R.id.sign_in_button), null);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /**
     * Handles the GoogleSignIn result and tries to authorize via google oAuth.
     */
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, get auth token.
            acct = result.getSignInAccount();

            getSharedPreferences(getString(R.string.login_prefs), MODE_PRIVATE).edit().putBoolean(getString(R.string.signed_in_key),true).commit();

            GetTokenTask tokenTask = new GetTokenTask(acct.getEmail(),mScopes);

            tokenTask.execute();


        } else {
            // Signed out, show unauthenticated UI.
            getSharedPreferences(getString(R.string.login_prefs),MODE_PRIVATE).edit().putBoolean(getString(R.string.signed_in_key),false).commit();
        }
    }

    /**
     * Handles the oAuth result and logs the user in parse with the token.
     */
    private void handleGetTokenResult(String token){
        if(token!=null && !token.isEmpty()) {
            Map<String, String> signInParameters = new HashMap<>();
            signInParameters.put("token", token);

            ParseCloud.callFunctionInBackground(VALIDATE_GOOGLE_SIGN_IN_TOKEN, signInParameters, new FunctionCallback<String>() {
                @Override
                public void done(String sessionKey, ParseException e) {

                    if (e == null) {
                        ParseUser.becomeInBackground(sessionKey, new LogInCallback() {
                            @Override
                            public void done(com.parse.ParseUser user, ParseException e) {
                                if (e == null) {
                                    updateUI(true);
                                }
                            }
                        });
                    } else if (e.getMessage().equals(ERROR_NEW_USER)) {
                        updateUI(false);
                    }else{
                        Log.e(getLocalClassName(),e.getMessage());
                        initUI();
                    }
                }
            });
        }
    }

    private void updateUI(final boolean isSignedIn){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isSignedIn){
                    startActivity(new Intent(SignInActivity.this,MainActivity.class));
                    finish();
                }else{
                    Intent i = new Intent(SignInActivity.this,SignUpActivity.class);
                    i.putExtra("acct", acct);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    class GetTokenTask extends AsyncTask<Void, Void, Void> {
        String mScope;
        String mEmail;

        GetTokenTask(String name, String scope) {
            this.mScope = scope;
            this.mEmail = name;
        }

        /**
         * Executes the asynchronous job. This runs when you call execute()
         * on the AsyncTask instance.
         */
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String token = fetchToken();
                SignInActivity.this.handleGetTokenResult(token);
            } catch (IOException e) {
                // The fetchToken() method handles Google-specific exceptions,
                // so this indicates something went wrong at a higher level.
                // TIP: Check for network connectivity before starting the AsyncTask.

            }
            return null;
        }

        /**
         * Gets an authentication token from Google and handles any
         * GoogleAuthException that may occur.
         */
        private String fetchToken() throws IOException {
            try {
                return GoogleAuthUtil.getToken(SignInActivity.this, mEmail, mScope);
            } catch (UserRecoverableAuthException userRecoverableException) {
                // GooglePlayServices.apk is either old, disabled, or not present
                // so we need to show the user some UI in the activity to recover.
                SignInActivity.this.handleGoogleSignInException(userRecoverableException);
            } catch (GoogleAuthException fatalException) {
                // Some other type of unrecoverable exception has occurred.
                // Report and log the error as appropriate for your app.
            }
            return null;
        }
    }
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;

    /**
     * This method is a hook for background threads and async tasks that need to
     * provide the user a response UI when an exception occurs.
     */
    public void handleGoogleSignInException(final Exception e) {
        // Because this call comes from the AsyncTask, we must ensure that the following
        // code instead executes on the UI thread.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            SignInActivity.this,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

}

