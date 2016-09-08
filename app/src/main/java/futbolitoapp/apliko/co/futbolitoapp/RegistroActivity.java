package futbolitoapp.apliko.co.futbolitoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.helper.Token;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONObjectRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class RegistroActivity extends AppCompatActivity {


    private Button button_registro;
    private DataBaseHelper dataBaseHelper;
    private static final String TAG = "vista_registro";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void registrarUsuario(String username, String email, String pass1, String pass2) {

        HashMap<String, String> solicitudRegistro = new HashMap<>();
        solicitudRegistro.put("username", username);
        solicitudRegistro.put("password1", pass1);
        solicitudRegistro.put("password2", pass2);
        solicitudRegistro.put("email", email);

        JSONObject jsonObject = new JSONObject(solicitudRegistro);

        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(
                new JsonObjectRequest(
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
                                                procesarRespuesta(jsonObject1);
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

    public void procesarRespuesta(JSONObject jsonObject) {

        try {

            if(jsonObject.has("key")) {
                String key = jsonObject.getString("key");
                Log.i(TAG, key);
                Token keyToken = new Token(key);
                long id = dataBaseHelper.createToDo(keyToken);
                Toast.makeText(RegistroActivity.this,id+"", Toast.LENGTH_SHORT).show();
            }else if(jsonObject.has("non_field_errors")){
                Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }else if(jsonObject.has("email")){
                Toast.makeText(RegistroActivity.this, "Ya existe un usuario con el email registrado", Toast.LENGTH_SHORT).show();
            }else if(jsonObject.has("username")){
                Toast.makeText(RegistroActivity.this, "Ya existe un usuario con el nombre de usuario registrado", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        button_registro = (Button) findViewById(R.id.button_registro);
        button_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = ((EditText) findViewById(R.id.email_reg)).getText().toString();
                String username = email;
                String pass1 = ((EditText) findViewById(R.id.pass1_reg)).getText().toString();
                String pass2 = ((EditText) findViewById(R.id.pass2_log)).getText().toString();
                registrarUsuario(username, email, pass1, pass2);

            }
        });
    }




}
