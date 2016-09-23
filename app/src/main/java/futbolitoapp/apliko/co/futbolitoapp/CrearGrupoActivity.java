package futbolitoapp.apliko.co.futbolitoapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import futbolitoapp.apliko.co.futbolitoapp.adapters.LigasPartidosAdapter;
import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.helper.Liga;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONObjectRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class CrearGrupoActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);
        final int idLiga = (int) getIntent().getExtras().get("idLiga");
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "HelveticaNeue-Bold.otf");
        Button buttonCrearGrupo = (Button) findViewById(R.id.button_crear_grupo);
        ((EditText) findViewById(R.id.textview_nombre_grupo)).setTypeface(typeface);
        buttonCrearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreGrupo = ((EditText) findViewById(R.id.textview_nombre_grupo)).getText().toString();

                enviarSolicitudNuevoGrupo(nombreGrupo, nombreGrupo,idLiga);
            }
        });

        ImageButton buttonGrupos = (ImageButton) findViewById(R.id.imageButton5);
        buttonGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrearGrupoActivity.this.finish();
            }
        });

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

            }, getApplicationContext()));

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

    public void listarLigas() {

        List<Liga> ligas = new ArrayList<Liga>();
        ligas = dataBaseHelper.getAllLigas();
        ArrayList<String> arrayLigas = new ArrayList<>();
        final String[] contenido = new String[ligas.size()];
        String nombreLiga = getIntent().getStringExtra("nombreLiga");
        int posLigaSelect = 0;
        for (int i = 0; i < ligas.size(); i++) {
            arrayLigas.add(ligas.get(i).getNombre());
            contenido[i] = ligas.get(i).getNombre();
            if(nombreLiga.equals(ligas.get(i).getNombre())){
                posLigaSelect = i;
            }
        }

        //ArrayAdapter<String> adapterLigas = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayLigas);
        //adapterLigas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner_ligas = (Spinner) findViewById(R.id.spinner_ligas);
        //spinner_ligas.setAdapter(adapterLigas);
        spinner_ligas.setPrompt(getIntent().getStringExtra("nombreLiga"));
        LigasPartidosAdapter listAdapter = new LigasPartidosAdapter(this, contenido);
        spinner_ligas.setAdapter(listAdapter);
        spinner_ligas.setSelection(posLigaSelect);
        spinner_ligas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = contenido[i];
                int id = dataBaseHelper.getLiga(item).getId();
              //  solicitudPartidos(id, dataBaseHelper.getLiga(item).getNombre());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       // tabs();
    }

}
