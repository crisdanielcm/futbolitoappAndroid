package futbolitoapp.apliko.co.futbolitoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import futbolitoapp.apliko.co.futbolitoapp.adapters.GruposAdapter;
import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.objects.Grupo;
import futbolitoapp.apliko.co.futbolitoapp.objects.Miembro;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONArrayRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class GruposActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);

        enviarSolicitudGrupos();
    }

    public void procesarRespuestaGrupos(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                String descripcion = jsonObject.getString("descripcion");
                Grupo grupo = new Grupo(nombre, descripcion);
                dataBaseHelper.createToDoGrupo(grupo);

                JSONArray jsonArrayMiembros = jsonObject.getJSONArray("miembros");
                for (int j = 0; j < jsonArrayMiembros.length(); j++) {

                    JSONObject jsonObjectMiembros = jsonArrayMiembros.getJSONObject(j);
                    String firstName = jsonObjectMiembros.getString("first_name");
                    String lastName = jsonObjectMiembros.getString("last_name");
                    String username = jsonObjectMiembros.getString("username");
                    String email = jsonObjectMiembros.getString("email");
                    Miembro miembro = new Miembro(firstName, lastName, username, email, grupo.getId());
                    dataBaseHelper.createToDoMiembro(miembro);
                }

                List<Grupo> grupos = dataBaseHelper.getAllGrupos();
                String [] contenido = new String[grupos.size()];

                for (int j = 0; j < contenido.length; j++) {
                    contenido[j] = grupos.get(j).getNombre();
                }

                GruposAdapter gruposAdapter = new GruposAdapter(this, contenido);
                ListView listView = (ListView) findViewById(R.id.listView_grupos);
                listView.setAdapter(gruposAdapter);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void enviarSolicitudGrupos(){

        CustomJSONArrayRequest customJSONArrayRequest = new CustomJSONArrayRequest(
                Request.Method.GET, Constantes.GRUPOS, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {
                procesarRespuestaGrupos(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse networkResponse = error.networkResponse;
                String respuesta = new String(networkResponse.data);
                if (networkResponse != null && networkResponse.data != null) {
                    switch (networkResponse.statusCode) {

                        case 400:

                    }
                }
            }
        }, getApplicationContext()
        );

        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(customJSONArrayRequest);
    }
}
