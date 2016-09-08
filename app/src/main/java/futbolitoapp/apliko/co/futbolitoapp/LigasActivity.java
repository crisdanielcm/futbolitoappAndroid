package futbolitoapp.apliko.co.futbolitoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import futbolitoapp.apliko.co.futbolitoapp.adapters.LigasListAdapter;
import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.helper.Liga;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONArrayRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class LigasActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;

    public void procesarRespuestaLiga(JSONArray jsonArray) {


        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String nombreLiga = jsonObject.getString("descripcion");
                Liga liga = new Liga(id, nombreLiga);
                dataBaseHelper.createToDoLiga(liga);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<Liga> listLigas = dataBaseHelper.getAllLigas();

        final String[] contenido = new String[listLigas.size()];

        for (int i = 0; i < listLigas.size(); i++) {

            contenido[i] = listLigas.get(i).getNombre();
        }

        ListView listView = (ListView) findViewById(R.id.listView_ligas);
        LigasListAdapter ligasListAdapter = new LigasListAdapter(this, contenido);
        listView.setAdapter(ligasListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String item = contenido[i];
                int id = dataBaseHelper.getLiga(item).getId();
                solicitudPartidos(id, dataBaseHelper.getLiga(item).getNombre());
            }
        });
    }

    public void enviarSolicitudLigas() {

        CustomJSONArrayRequest customJSONArrayRequest = new CustomJSONArrayRequest(
                Request.Method.GET, Constantes.LIGAS, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                procesarRespuestaLiga(response);
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

    public void solicitudPartidos(int id, final String nombre) {

        HashMap<String, Integer> solicitudPartidos = new HashMap<>();
        solicitudPartidos.put("id_liga", id);

        JSONObject jsonObject = new JSONObject(solicitudPartidos);

        CustomJSONArrayRequest customJSONArrayRequest = new CustomJSONArrayRequest(
                Request.Method.POST, Constantes.PARTIDOS, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Intent intent = new Intent(getApplicationContext(), PartidosActivity.class);
                intent.putExtra("partidos", response.toString());
                intent.putExtra("nombreLiga", nombre);
                startActivity(intent);
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
        }, getApplicationContext());

        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(customJSONArrayRequest);

    }

    public void procesarrespuestaPronostico(JSONObject jsonObject) {

        if (jsonObject.has("mensaje")) {

            Toast.makeText(LigasActivity.this, "Pronostico registrado", Toast.LENGTH_SHORT).show();
        } else {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ligas);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        enviarSolicitudLigas();

    }
}
