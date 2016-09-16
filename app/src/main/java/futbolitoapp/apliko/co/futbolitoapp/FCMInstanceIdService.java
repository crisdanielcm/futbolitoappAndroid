package futbolitoapp.apliko.co.futbolitoapp;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class FCMInstanceIdService extends FirebaseInstanceIdService {
    public FCMInstanceIdService() {
    }

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {

        HashMap<String, String> solicitud = new HashMap<>();
        solicitud.put("Token", token);
        JSONObject jsonObject = new JSONObject(solicitud);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Constantes.REGISTRAR_DISPOSITIVO,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("", "onResponse: procesando: " + token);
                Toast.makeText(FCMInstanceIdService.this, response.toString(), Toast.LENGTH_SHORT).show();
                //procesarRespuesta(response, email, password);

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
                                //procesarRespuesta(jsonObject1, email, password);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }
        });

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
