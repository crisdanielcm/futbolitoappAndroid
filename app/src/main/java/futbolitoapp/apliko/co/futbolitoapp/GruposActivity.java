package futbolitoapp.apliko.co.futbolitoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import futbolitoapp.apliko.co.futbolitoapp.adapters.GruposAdapter;
import futbolitoapp.apliko.co.futbolitoapp.adapters.LigasPartidosAdapter;
import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.helper.Liga;
import futbolitoapp.apliko.co.futbolitoapp.objects.Grupo;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONArrayRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class GruposActivity extends AppCompatActivity {

    private static final String TAG = "GruposActivity";
    private DataBaseHelper dataBaseHelper;
    private ImageButton buttonRegistro;
    private int idSelect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        int idLiga = getIntent().getExtras().getInt("id_liga");
        listarLigas();
        enviarSolicitudGrupos(idLiga);
        ImageButton buttonRegistro = (ImageButton) findViewById(R.id.imageButton_crear_grupo);


        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CrearGrupoActivity.class);
                intent.putExtra("idLiga", idSelect);
                startActivity(intent);
            }
        });

        ImageButton buttonPartidos = (ImageButton) findViewById(R.id.imageButton_partidos_activity);
        buttonPartidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GruposActivity.this.finish();
            }   });
    }

    public void procesarRespuestaGrupos(JSONArray jsonArray) {

        List<Grupo> grupos = new ArrayList<>();
        final int[] arrayGrupo = new int[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                int posicion = jsonObject.getInt("puesto");
                int numeroIntegrantes = jsonObject.getInt("total_miembros");
                int idGrupo = jsonObject.getInt("id");
                Grupo grupo = new Grupo(nombre, posicion, numeroIntegrantes);
                grupo.setId(idGrupo);
                grupos.add(grupo);
//                long id=dataBaseHelper.createToDoGrupo(grupo);
                int id = idGrupo;
                Log.i(TAG, "procesarRespuestaGrupos: "+id);
                arrayGrupo[i] = (int) id;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //List<Grupo> grupos = dataBaseHelper.getAllGrupos();
        final String[] contenido = new String[grupos.size()];
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String item = contenido[i];
                int id = arrayGrupo[i];
                enviarSolicitudMiembros(id, item);
            }
        });
    }

    public void enviarSolicitudGrupos(int idLiga) {

        HashMap<String, Integer> solicitud = new HashMap<>();
        solicitud.put("id_liga", idLiga);

        JSONObject jsonObject = new JSONObject(solicitud);

        CustomJSONArrayRequest customJSONArrayRequest = new CustomJSONArrayRequest(
                Request.Method.POST, Constantes.GRUPOS,jsonObject, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                procesarRespuestaGrupos(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse networkResponse = error.networkResponse;
//                String respuesta = new String(networkResponse.data);
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

    public void listarLigas() {

        List<Liga> ligas = new ArrayList<Liga>();
        ligas = dataBaseHelper.getAllLigas();
        final String[] contenido = new String[ligas.size()];
        String nombreLiga = getIntent().getStringExtra("nombreLiga");
        int posLigaSelect = 0;
        for (int i = 0; i < ligas.size(); i++) {
            contenido[i] = ligas.get(i).getNombre();

            if(nombreLiga.equals(ligas.get(i).getNombre())){
                posLigaSelect = i;
            }
        }

        //ArrayAdapter<String> adapterLigas = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayLigas);
        //adapterLigas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner_ligas = (Spinner) findViewById(R.id.spinner_ligas);
        //spinner_ligas.setAdapter(adapterLigas);
        spinner_ligas.setPrompt(nombreLiga);
        LigasPartidosAdapter listAdapter = new LigasPartidosAdapter(this, contenido);
        spinner_ligas.setAdapter(listAdapter);
        spinner_ligas.setSelection(posLigaSelect);
        idSelect = dataBaseHelper.getLiga(contenido[posLigaSelect]).getId();
        spinner_ligas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = contenido[i];
                idSelect = dataBaseHelper.getLiga(item).getId();
                enviarSolicitudGrupos(idSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void enviarSolicitudMiembros(int idGrupo, final String nombre){

        HashMap<String, Integer> solicitudMiembros = new HashMap<>();
        solicitudMiembros.put("id_grupo", idGrupo);

        JSONObject jsonObject = new JSONObject(solicitudMiembros);

        CustomJSONArrayRequest customJSONArrayRequest = new CustomJSONArrayRequest(
                Request.Method.POST, Constantes.MIEMBROS, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Intent intent = new Intent(getApplicationContext(), MiembrosActivity.class);
                intent.putExtra("miembros",response.toString());
                intent.putExtra("nombreGrupo", nombre);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String json = null;
                NetworkResponse networkResponse = error.networkResponse;
//                String respuesta = new String(networkResponse.data);
                if (networkResponse != null && networkResponse.data != null) {
                    switch (networkResponse.statusCode) {

                        case 400:

                    }
                }
            }
        }, getApplicationContext());

        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(customJSONArrayRequest);
    }

}
