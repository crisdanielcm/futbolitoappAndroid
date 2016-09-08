package futbolitoapp.apliko.co.futbolitoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import futbolitoapp.apliko.co.futbolitoapp.adapters.MiembrosAdapter;
import futbolitoapp.apliko.co.futbolitoapp.objects.Miembro;

public class MiembrosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miembros);

        String respuesta = getIntent().getExtras().getString("miembros");

        try {
            JSONArray jsonArray = new JSONArray(respuesta);
            procesarRespuesta(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void procesarRespuesta(JSONArray jsonArray){

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
                int idGrupo = jsonObject.getInt("grupo");

                Miembro miembro = new Miembro(firstName, lastName, username, email, idGrupo, esCreador
                        , puesto, puestoanterior, puntaje );
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
            if(miembros.get(i).getPuestoActual() == miembros.get(i).getPuestoAnterior()){
                estado = 0;
            }else if(miembros.get(i).getPuestoActual() < miembros.get(i).getPuestoAnterior()){
                estado = 1;
            }else if(miembros.get(i).getPuestoActual() > miembros.get(i).getPuestoAnterior()){
                estado = 2;
            }
            itemListImage[i] = estado;
        }

        MiembrosAdapter miembrosAdapter = new MiembrosAdapter(this,itemListNombre,itemListPuntos,itemListPosicion,itemListImage);
        ListView listView = (ListView) findViewById(R.id.listViewMiembros);
        listView.setAdapter(miembrosAdapter);

    }

    public void crearSolicitud(int idgrupo){


    }
}
