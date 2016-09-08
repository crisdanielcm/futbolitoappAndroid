package futbolitoapp.apliko.co.futbolitoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONObjectRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class CrearGrupoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);
    }

    public void enviarSolicitudNuevoGrupo(String nombre, String descripción, int idLiga){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre",nombre);
            jsonObject.put("descripcion", descripción);
            jsonObject.put("id_liga", idLiga);

            VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(new CustomJSONObjectRequest(
                    Request.Method.POST, Constantes.CREAR_GRUPO, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    procesarRespuestaCrearGrupo(response);
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
                                    procesarRespuestaCrearGrupo(jsonObject1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        }
                    }
                }
            },getApplicationContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void procesarRespuestaCrearGrupo(JSONObject jsonObject){

        if(jsonObject.has("id_grupo")){

            Toast.makeText(CrearGrupoActivity.this, "Grupo creado con éxito", Toast.LENGTH_SHORT).show();
        }
        else{

            Toast.makeText(CrearGrupoActivity.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
