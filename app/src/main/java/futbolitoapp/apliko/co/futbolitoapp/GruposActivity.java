package futbolitoapp.apliko.co.futbolitoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONArrayRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class GruposActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    private ImageButton buttonRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        enviarSolicitudGrupos();
        ImageButton buttonRegistro = (ImageButton) findViewById(R.id.imageButton_crear_grupo);


        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CrearGrupoActivity.class);
                startActivity(intent);
            }
        });
    }

    public void procesarRespuestaGrupos(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre_grupo");
                int posicion = jsonObject.getInt("puesto");
                int numeroIntegrantes = jsonObject.getInt("total_miembros");
                int idGrupo = jsonObject.getInt("id_grupo");
                Grupo grupo = new Grupo(nombre, posicion, numeroIntegrantes);
                grupo.setId(idGrupo);
                long id=dataBaseHelper.createToDoGrupo(grupo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        List<Grupo> grupos = dataBaseHelper.getAllGrupos();
        String[] contenido = new String[grupos.size()];
        Integer[] posiciones = new Integer[grupos.size()];
        Integer[] numeroMiembros = new Integer[grupos.size()];

        for (int j = 0; j < contenido.length; j++) {
            contenido[j] = grupos.get(j).getNombre();
            posiciones[j] = grupos.get(j).getPosicion();
            numeroMiembros[j] = grupos.get(j).getNumeroMiembros();
        }

        GruposAdapter gruposAdapter = new GruposAdapter(this, contenido, posiciones, numeroMiembros);
        ListView listView = (ListView) findViewById(R.id.listView_grupos);
        listView.setAdapter(gruposAdapter);
    }


    public void enviarSolicitudGrupos() {

        CustomJSONArrayRequest customJSONArrayRequest = new CustomJSONArrayRequest(
                Request.Method.GET, Constantes.GRUPOS, new Response.Listener<JSONArray>() {

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
