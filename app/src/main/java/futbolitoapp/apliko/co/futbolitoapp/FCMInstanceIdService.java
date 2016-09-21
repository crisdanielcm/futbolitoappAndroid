package futbolitoapp.apliko.co.futbolitoapp;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.helper.Token;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONObjectRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class FCMInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    private boolean registro;

    public FCMInstanceIdService() {
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        List<Token> tokens = db.getAllTokens();
        int size = tokens.size();
        while (size >= 0 && size < 2) {
            if (size == 1) {
                sendRegistrationToServer(refreshedToken);
                break;
            } else {
                size = db.getAllTokens().size();
            }
        }


    }

    /**
     * Persist token to third-party servers.
     * <p/>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        registro = false;
        HashMap<String, String> solicitud = new HashMap<>();
        solicitud.put("token", token);
        JSONObject jsonObject = new JSONObject(solicitud);
        CustomJSONObjectRequest objectRequest = new CustomJSONObjectRequest(Request.Method.POST, Constantes.REGISTRAR_DISPOSITIVO,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("", "onResponse: procesando: " + token);
                        Toast.makeText(FCMInstanceIdService.this, response.toString(), Toast.LENGTH_SHORT).show();
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                        dataBaseHelper.createToDo(new Token(token));
                        //procesarRespuesta(response, email, password);
                        registro = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            String errorS = new String(networkResponse.data);
                            Log.i(TAG, "onErrorResponse: " + errorS);
                            switch (networkResponse.statusCode) {

                                case 401:
                                    json = new String(networkResponse.data);
                                    Log.i(TAG, "onErrorResponse: 401");
                                case 500:
                                    json = new String(networkResponse.data);
                                    Log.i(TAG, "onErrorResponse: 500");

                            }
                        }
                    }
                }, getApplicationContext());

        objectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(objectRequest);
        //Log.i(TAG, "sendRegistrationToServer: "+token);

    }
}
