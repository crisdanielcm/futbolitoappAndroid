package futbolitoapp.apliko.co.futbolitoapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.helper.Token;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONObjectRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

/**
 * A login screen that offers login via email/password.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{



    private static final String TAG = "vista_login";
    private static final int RC_SIGN_IN = 9001;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView registroView;
    private DataBaseHelper dataBaseHelper;
    private LoginButton btnloginFB;
    private CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButtonGoogle;
    private AccessTokenTracker accessTokenTracker;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    public void enviarSolicitud(String username, String email, String password) {

        HashMap<String, String> solicitudLogin = new HashMap<>();
        solicitudLogin.put("username", username);
        solicitudLogin.put("email", email);
        solicitudLogin.put("password", password);

        JSONObject jsonObject = new JSONObject(solicitudLogin);

        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(new CustomJSONObjectRequest(
                Request.Method.POST, Constantes.LOGIN, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                procesarRespuesta(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    switch (networkResponse.statusCode) {

                        case 400:
                            json = new String(networkResponse.data);
                            JSONObject jsonObject1 = null;
                            try {
                                jsonObject1 = new JSONObject(json);
                                procesarRespuesta(jsonObject1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }
        }));

    }

    public void procesarRespuesta(JSONObject jsonObject) {

        try {
            if(jsonObject.has("key")){
                String key = jsonObject.getString("key");
                Log.i(TAG, key);
                Token keyToken = new Token(key);
                long id = dataBaseHelper.createToDo(keyToken);
                Intent intent = new Intent(getApplicationContext(),LigasActivity.class);
                startActivity(intent);
            }else if(jsonObject.has("non_field_errors")){
                Toast.makeText(LoginActivity.this, "Los datos que esta ingresando no son correctos.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        setContentView(R.layout.activity_login);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestIdToken("74936133906-ithufk33scoarsqf63ii8g3p0767jndv.apps.googleusercontent.com").build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        signInButtonGoogle = (SignInButton) findViewById(R.id.imageButton_google);

        signInButtonGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signIntent, RC_SIGN_IN );
            }
        });

        btnloginFB = (LoginButton) findViewById(R.id.imageButton_fb);
        btnloginFB.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        btnloginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String token = loginResult.getAccessToken().getToken();
                                    enviarSolicitud(email,email,token);
                                    registrarUsuario(email, email, token, token);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        dataBaseHelper.dropDB();
        registroView = (TextView) findViewById(R.id.textView_registrar);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = ((EditText) findViewById(R.id.email)).getText().toString();
                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String pass = ((EditText) findViewById(R.id.password)).getText().toString();
                enviarSolicitud(username, email, pass);
            }
        });

        registroView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });

    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            String tokenGoogle = acct.getIdToken();
            String emailGoogle = acct.getEmail();
            enviarSolicitud(emailGoogle, emailGoogle,tokenGoogle);
            registrarUsuario(emailGoogle, emailGoogle, tokenGoogle, tokenGoogle);
        } else {

        }
    }

    public void registrarUsuario(String username, String email, String pass1, String pass2) {

        HashMap<String, String> solicitudRegistro = new HashMap<>();
        solicitudRegistro.put("username", username);
        solicitudRegistro.put("password1", pass1);
        solicitudRegistro.put("password2", pass2);
        solicitudRegistro.put("email", email);

        JSONObject jsonObject = new JSONObject(solicitudRegistro);

        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(
                new CustomJSONObjectRequest(
                        Request.Method.POST,
                        Constantes.REGISTRO, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                procesarRespuesta(response);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                String json = null;
                                NetworkResponse networkResponse = error.networkResponse;
                                if (networkResponse != null && networkResponse.data != null) {
                                    switch (networkResponse.statusCode) {

                                        case 400:
                                            json = new String(networkResponse.data);
                                            JSONObject jsonObject1 = null;
                                            try {
                                                jsonObject1 = new JSONObject(json);
                                                procesarRespuestaRegistro(jsonObject1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                    }
                                }
                            }
                        }
                )
        );
    }

    public void procesarRespuestaRegistro(JSONObject jsonObject) {

        try {
            if(jsonObject.has("key")) {
                String key = jsonObject.getString("key");
                Log.i(TAG, key);
                Token keyToken = new Token(key);
                long id = dataBaseHelper.createToDo(keyToken);
                Toast.makeText(LoginActivity.this,id+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),LigasActivity.class);
                startActivity(intent);
            }else if(jsonObject.has("non_field_errors")){
                Toast.makeText(LoginActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }else if(jsonObject.has("email")){
                Toast.makeText(LoginActivity.this, "Ya existe un usuario con el email registrado", Toast.LENGTH_SHORT).show();
            }else if(jsonObject.has("username")){
                Toast.makeText(LoginActivity.this, "Ya existe un usuario con el nombre de usuario registrado", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

