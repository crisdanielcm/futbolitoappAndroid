package futbolitoapp.apliko.co.futbolitoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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

import futbolitoapp.apliko.co.futbolitoapp.adapters.LigasPartidosAdapter;
import futbolitoapp.apliko.co.futbolitoapp.adapters.MiembrosAdapter;
import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.objects.Grupo;
import futbolitoapp.apliko.co.futbolitoapp.objects.Miembro;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONArrayRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONObjectRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class MiembrosActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    private int idSelect = 0;
    private int idGrupo;
    private String nombreGrupo;
    private ArrayList<Grupo> grupos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miembros);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        grupos = new ArrayList<>();
        String respuesta = getIntent().getExtras().getString("miembros");
        enviarSolicitudGrupos();


        try {
            JSONArray jsonArray = new JSONArray(respuesta);
            procesarRespuesta(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void share(String subject, String text) {
        String shareBody = "Here is the share content body";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
    }
    List<Miembro> miembros;

    public void procesarRespuesta(JSONArray jsonArray) {

        miembros= new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                boolean esCreador = jsonObject.getBoolean("es_creador");
                int puesto = jsonObject.getInt("puesto");
                int puestoanterior = jsonObject.getInt("puesto_anterior");
                int puntaje = jsonObject.getInt("puntaje");
                int id = jsonObject.getInt("usuario_id");

                JSONObject objectusuarios = jsonObject.getJSONObject("usuario");
                String firstName = objectusuarios.getString("first_name");
                String lastName = objectusuarios.getString("last_name");
                String username = objectusuarios.getString("username");
                String email = objectusuarios.getString("email");
                idGrupo = jsonObject.getInt("grupo");

                Miembro miembro = new Miembro(firstName, lastName, username, email, idGrupo, esCreador
                        , puesto, puestoanterior, puntaje);
                miembro.setId(id);
                miembros.add(miembro);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        String[] itemListNombre;
        int[] itemListPuntos, itemListPosicion, itemListImage;
        itemListImage = new int[miembros.size()];
        itemListNombre = new String[miembros.size()];
        itemListPosicion = new int[miembros.size()];
        itemListPuntos = new int[miembros.size()];

        for (int i = 0; i < miembros.size(); i++) {
            itemListNombre[i] = miembros.get(i).getUsername();
            itemListPosicion[i] = miembros.get(i).getPuestoActual();
            itemListPuntos[i] = miembros.get(i).getPuntaje();
            int estado = 0;
            if (miembros.get(i).getPuestoActual() == miembros.get(i).getPuestoAnterior()) {
                estado = 0;
            } else if (miembros.get(i).getPuestoActual() < miembros.get(i).getPuestoAnterior()) {
                estado = 1;
            } else if (miembros.get(i).getPuestoActual() > miembros.get(i).getPuestoAnterior()) {
                estado = 2;
            }
            itemListImage[i] = estado;
        }

        MiembrosAdapter miembrosAdapter = new MiembrosAdapter(this, itemListNombre, itemListPuntos, itemListPosicion, itemListImage);
        ListView listView = (ListView) findViewById(R.id.listViewMiembros);
        //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(miembrosAdapter);

    }
    public void inviteSelected(View view) {
        try{
            share("Invitación a grupo","Te invito a unite al grupo "+nombreGrupo+" en 1F");

        }catch (Exception e){

        }
    }

    public void deleteSelected(View view) {
        //Obtengo los elementos seleccionados de mi lista
        SparseBooleanArray seleccionados = ((ListView) findViewById(R.id.listViewMiembros)).getCheckedItemPositions();

        if(seleccionados==null || seleccionados.size()==0){
            //Si no había elementos seleccionados...
            Toast.makeText(this,"No hay elementos seleccionados",Toast.LENGTH_SHORT).show();
        }else{
            //si los había, miro sus valores

            //Esto es para ir creando un mensaje largo que mostraré al final
            StringBuilder resultado=new StringBuilder();
            resultado.append("Se eliminarán los siguientes elementos:\n");

            //Recorro my "array" de elementos seleccionados
            Integer [] idusuarios = new Integer [seleccionados.size()];
            final Integer size=seleccionados.size();
            for (int i=0; i<size; i++) {
                //Si valueAt(i) es true, es que estaba seleccionado
                if (seleccionados.valueAt(i)) {
                    //en keyAt(i) obtengo su posición
                    resultado.append("El elemento "+miembros.get(seleccionados.keyAt(i)).getId()+" estaba seleccionado\n");
                    idusuarios[i] = miembros.get(seleccionados.keyAt(i)).getId();
                }
            }



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_grupo", idGrupo);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < idusuarios.length; i++) {
                if(idusuarios[i] != null){

                jsonArray.put(idusuarios[i]);
                }
            }

            jsonObject.put("lista_miembros",jsonArray);
            Log.i("deleteSelected: ", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONObjectRequest customJSONArrayRequest = new CustomJSONObjectRequest(
                Request.Method.POST, Constantes.DAR_BAJA,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(MiembrosActivity.this,response.getString("mensaje"),Toast.LENGTH_LONG).show();
                    MiembrosActivity.this.finish();
                    recreate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse networkResponse = error.networkResponse;
//                String respuesta = new String(networkResponse.data);
                if (networkResponse != null && networkResponse.data != null) {
                    switch (networkResponse.statusCode) {

                        case 500:
                            String men=new String(networkResponse.data);
                            Log.e("lkiuyytrf", "onErrorResponse: "+men, error );
                    }
                }
            }
        }, getApplicationContext()
        );

        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(customJSONArrayRequest);
     }
    }


    public void enviarSolicitudGrupos() {

        CustomJSONArrayRequest customJSONArrayRequest = new CustomJSONArrayRequest(
                Request.Method.GET, Constantes.GRUPOS_IND, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                procesarRespuestaGrupos(response);
                listarGrupos();
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

    public void procesarRespuestaGrupos(JSONArray jsonArray) {

        final int[] arrayGrupo = new int[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                nombreGrupo = jsonObject.getString("nombre_grupo");
                int posicion = jsonObject.getInt("puesto");
                int numeroIntegrantes = jsonObject.getInt("total_miembros");
                idGrupo = jsonObject.getInt("id_grupo");
                Grupo grupo = new Grupo(nombreGrupo, posicion, numeroIntegrantes);
                grupo.setId(idGrupo);
                grupos.add(grupo);
//                long id=dataBaseHelper.createToDoGrupo(grupo);
                int id = idGrupo;
                Log.i("", "procesarRespuestaGrupos: " + id);
                arrayGrupo[i] = (int) id;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final String[] nombres = new String[grupos.size()];
        String nombreGrupo = getIntent().getStringExtra("nombreGrupo");
        int posLigaSelect = 0;

        for (int j = 0; j < nombres.length; j++) {
            nombres[j] = grupos.get(j).getNombre();

            if (nombreGrupo.equals(grupos.get(j).getNombre())) {
                posLigaSelect = j;
            }
        }

        Spinner spinnerGrupos = (Spinner) findViewById(R.id.spinner_grupos);
        spinnerGrupos.setPrompt(nombreGrupo);
        LigasPartidosAdapter gruposAdapter = new LigasPartidosAdapter(this, nombres);
        spinnerGrupos.setAdapter(gruposAdapter);
        spinnerGrupos.setSelection(posLigaSelect);
        idSelect = grupos.get(posLigaSelect).getId();
        spinnerGrupos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                idSelect = grupos.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

    }

    public void enviarSolicitudMiembros(final int idGrupo){

        HashMap<String, Integer> solicitudMiembros = new HashMap<>();
        solicitudMiembros.put("id_grupo", idGrupo);

        JSONObject jsonObject = new JSONObject(solicitudMiembros);

        CustomJSONArrayRequest customJSONArrayRequest = new CustomJSONArrayRequest(
                Request.Method.POST, Constantes.MIEMBROS, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i( "onResponse: ",response.toString());
                procesarRespuesta(response);
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


    public void listarGrupos() {


        final String []nombreGrupos = new String[grupos.size()];
        final String grupo = getIntent().getExtras().getString("nombreGrupo");
        int posGrupoSelect = 0;
        for (int i = 0; i < grupos.size(); i++) {

            nombreGrupos[i] = grupos.get(i).getNombre();
            if(nombreGrupos[i].equals(grupo))
                posGrupoSelect = i;
        }

        final Spinner spinner_grupos = (Spinner) findViewById(R.id.spinner_grupos);
        spinner_grupos.setPrompt(grupo);
        nombreGrupo=grupo;
        LigasPartidosAdapter listAdapter = new LigasPartidosAdapter(this, nombreGrupos);
        spinner_grupos.setAdapter(listAdapter);
        spinner_grupos.setSelection(posGrupoSelect);
        spinner_grupos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = nombreGrupos[i];
                spinner_grupos.setPrompt(item);
                int id = 0;

                if(grupos.get(i).getNombre().equals(item)){
                    id = grupos.get(i).getId();
                }
                enviarSolicitudMiembros(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



}
