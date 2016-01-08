package com.mobilefactory.whosnext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobilefactory.whosnext.model.parse.ParseUser;
import com.mobilefactory.whosnext.utils.Animations;
import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

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
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();

            getSharedPreferences(getString(R.string.login_prefs), MODE_PRIVATE).edit().putBoolean(getString(R.string.signed_in_key),true).commit();


            Map<String,String> signInParameters = new HashMap<>();
            signInParameters.put("token", acct.getIdToken());

            ParseCloud.callFunctionInBackground("validateGoogleSignInToken", signInParameters, new FunctionCallback<String>() {
                @Override
                public void done(String sessionKey, ParseException e) {

                    if (e == null) {
                        ParseUser.becomeInBackground(sessionKey, new LogInCallback() {
                            @Override
                            public void done(com.parse.ParseUser user, ParseException e) {
                                if (e == null) {
                                    updateUI(true);
                                } else {
                                    updateUI(false);
                                }
                            }
                        });
                    }else{
                        initUI();
                    }
                }
            });

        } else {
            // Signed out, show unauthenticated UI.
            getSharedPreferences(getString(R.string.login_prefs),MODE_PRIVATE).edit().putBoolean(getString(R.string.signed_in_key),false).commit();
        }
    }

    private void updateUI(boolean isSignedIn){
        if(isSignedIn){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else{
            Intent i = new Intent(this,SignUpActivity.class);
            i.putExtra("acct",acct);
            startActivity(i);
            finish();
        }
    }

}

