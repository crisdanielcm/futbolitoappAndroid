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
import java.util.List;

import futbolitoapp.apliko.co.futbolitoapp.adapters.LigasPartidosAdapter;
import futbolitoapp.apliko.co.futbolitoapp.adapters.MiembrosAdapter;
import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.objects.Grupo;
import futbolitoapp.apliko.co.futbolitoapp.objects.Miembro;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONArrayRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class MiembrosActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    private int idSelect = 0;
    private int idGrupo;
    private String nombreGrupo;

    public MiembrosActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miembros);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        String respuesta = getIntent().getExtras().getString("miembros");
        enviarSolicitudGrupos();

        try {
            JSONArray jsonArray = new JSONArray(respuesta);
            procesarRespuesta(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageButton invitarAmigo = (ImageButton) findViewById(R.id.imageButton_invitar_amigo);
        invitarAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share("Invitación a grupo","Te invito a unite al grupo "+nombreGrupo);
            }
        });

    }

    public void share(String subject, String text) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
//        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.compartir)));
        startActivity(sharingIntent);
    }

    public void procesarRespuesta(JSONArray jsonArray) {

        ArrayList<Miembro> miembros = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                boolean esCreador = jsonObject.getBoolean("es_creador");
                int puesto = jsonObject.getInt("puesto");
                int puestoanterior = jsonObject.getInt("puesto_anterior");
                int puntaje = jsonObject.getInt("puntaje");

                JSONObject objectusuarios = jsonObject.getJSONObject("usuario");
                String firstName = objectusuarios.getString("first_name");
                String lastName = objectusuarios.getString("last_name");
                String username = objectusuarios.getString("username");
                String email = objectusuarios.getString("email");
                idGrupo = jsonObject.getInt("grupo");

                Miembro miembro = new Miembro(firstName, lastName, username, email, idGrupo, esCreador
                        , puesto, puestoanterior, puntaje);
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
            itemListNombre[i] = miembros.get(i).getFirstName() + miembros.get(i).getLastname();
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
        listView.setAdapter(miembrosAdapter);

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

        final List<Grupo> grupos = new ArrayList<>();
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


    public void listarGrupos() {

        List<Grupo> grupos = new ArrayList<>();

    }
}
