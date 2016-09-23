package futbolitoapp.apliko.co.futbolitoapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONObjectRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class CambioContrasenia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_contrasenia);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "HelveticaNeue-Bold.otf");
        final EditText textViewActual = (EditText) findViewById(R.id.contraActual);
        final EditText editTextPass1 = (EditText) findViewById(R.id.editTextContraNueva1);
        final EditText editTextPass2 = (EditText) findViewById(R.id.editTextContraNueva2);

        textViewActual.setTypeface(typeface);
        editTextPass1.setTypeface(typeface);
        editTextPass2.setTypeface(typeface);
        Button buttonCambio = (Button) findViewById(R.id.buttonCambio);
        buttonCambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("old_password", textViewActual.getText().toString());
                    jsonObject.put("new_password1", editTextPass1.getText().toString());
                    jsonObject.put("new_password2", editTextPass2.getText().toString());

                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(new CustomJSONObjectRequest(
                            Request.Method.POST, Constantes.CAMBIAR_CONTRASENIA,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if(response.has("success")){
                                        Toast.makeText(CambioContrasenia.this, "El cambio de contraseña se realizo satisfactoriamente", Toast.LENGTH_SHORT).show();
                                        CambioContrasenia.this.finish();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }, getApplicationContext()
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

