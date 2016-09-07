package futbolitoapp.apliko.co.futbolitoapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import futbolitoapp.apliko.co.futbolitoapp.adapters.LigasPartidosAdapter;
import futbolitoapp.apliko.co.futbolitoapp.adapters.PartidosAdapter;
import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.helper.Liga;
import futbolitoapp.apliko.co.futbolitoapp.helper.Pronostico;
import futbolitoapp.apliko.co.futbolitoapp.objects.Equipo;
import futbolitoapp.apliko.co.futbolitoapp.objects.Fecha;
import futbolitoapp.apliko.co.futbolitoapp.objects.Partido;
import futbolitoapp.apliko.co.futbolitoapp.objects.Semana;
import futbolitoapp.apliko.co.futbolitoapp.objects.Temporada;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONObjectRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class PartidosActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    private ArrayList<Semana> semanas;
    private ArrayList<Fecha> fechas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidos);
        fechas = new ArrayList<>();

        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        //solicitudpronostico();
        String respuesta = getIntent().getExtras().getString("partidos");
        try {
            JSONArray jsonArray = new JSONArray(respuesta);
            procesarRespuestaPartidos(jsonArray);
            listarLigas();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void procesarRespuestaPartidos(JSONArray jsonArray) {

        try {
            semanas = new ArrayList<Semana>();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String fechaInicioSemana = jsonObject.getString("fecha_inicio");
                String fechaFinSemana = jsonObject.getString("fecha_fin");
                int numero = jsonObject.getInt("numero");

                JSONObject temporadaObject = jsonObject.getJSONObject("temporada");
                String fechaInicio = temporadaObject.getString("fecha_inicio");
                String fechaFin = temporadaObject.getString("fecha_fin");
                boolean esActual = temporadaObject.getBoolean("es_actual");

                JSONObject Object = temporadaObject.getJSONObject("liga");
                int id = Object.getInt("id");
                String descripcion = Object.getString("descripcion");

                Liga liga = new Liga(id, descripcion);
                Temporada temporada = new Temporada(fechaInicio, fechaFin, liga, esActual);

                JSONArray partidosListObject = jsonObject.getJSONArray("partidos");
                ArrayList<Partido> partidos = new ArrayList<>();
                Partido partidoObject;
                for (int j = 0; j < partidosListObject.length(); j++) {
                    JSONObject partido = partidosListObject.getJSONObject(j);
                    String fechaHora = partido.getString("fecha_hora");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    format.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date date = null;
                    Calendar calendar = Calendar.getInstance();
                    try {
                        date = format.parse(fechaHora);
                        calendar.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int anio = calendar.get(Calendar.YEAR);
                    int mes = calendar.get(Calendar.MONTH) + 1;
                    int dia = calendar.get(Calendar.DAY_OF_MONTH);
                    ;

                    Log.i("fecha", date.getDate() + "");
                    JSONObject equipoLocalObject = partido.getJSONObject("equipo_local");
                    int idLocal = equipoLocalObject.getInt("id");
                    String nombreLocal = equipoLocalObject.getString("nombre");
                    String descripcionLocal = equipoLocalObject.getString("descripcion");
                    String imagen = null;
                    int golesLocal = partido.getInt("goles_local");

                    Equipo equipoLocal = new Equipo(idLocal, nombreLocal, descripcionLocal, imagen);

                    JSONObject equipoVisitanteobject = partido.getJSONObject("equipo_visitante");
                    int idVisitante = equipoVisitanteobject.getInt("id");
                    String nombreVisitante = equipoVisitanteobject.getString("nombre");
                    String descripcioVisitante = equipoVisitanteobject.getString("descripcion");
                    String imagenVisitante;
                    int golesVisitante = partido.getInt("goles_visitante");

                    Equipo equipoVisitante = new Equipo(idVisitante, nombreVisitante, descripcioVisitante, imagen);

                    partidoObject = new Partido(fechaHora, equipoLocal, equipoVisitante, golesLocal, golesVisitante);
                    verificarExistFecha(fechas, dia, mes, anio, partidoObject);
                }
                Semana semana = new Semana(fechaInicioSemana, fechaFinSemana, temporada, fechas, numero);
                semanas.add(semana);
                fechas = new ArrayList<>();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void verificarExistFecha(ArrayList<Fecha> listFecha, int day, int month, int year, Partido partido) {
        int find = -1;
        if (listFecha.size() != 0) {

            for (int i = 0; i < listFecha.size(); i++) {
                Fecha fecha = listFecha.get(i);

                if (fecha.getDia() == day && fecha.getMes() == month && fecha.getAnio() == year) {
                    find = i;
                    break;
                } else {
                    find = -1;
                }
            }
            if (find == -1) {
                ArrayList<Partido> partidos = new ArrayList<>();
                partidos.add(partido);
                Fecha newFecha = new Fecha(day, year, month, partidos);
                listFecha.add(newFecha);
            } else {
                listFecha.get(find).getPartidos().add(partido);
            }
        } else {
            ArrayList<Partido> partidos = new ArrayList<>();
            partidos.add(partido);
            Fecha newFecha = new Fecha(day, year, month, partidos);
            listFecha.add(newFecha);
        }
    }

    public void solicitudpronostico(int goles_local, int goles_visitante, int id_partido) {

        HashMap<String, Integer> solicitud = new HashMap<>();
        solicitud.put("goles_local", goles_local);
        solicitud.put("goles_visitante", goles_visitante);
        solicitud.put("id_partido", id_partido);
        Pronostico pronostico = new Pronostico(goles_local, goles_visitante, id_partido);
        dataBaseHelper.createToDoPronostico(pronostico);

        JSONObject jsonObject = new JSONObject(solicitud);

        CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                Constantes.PRONOSTICO, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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

        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(customJSONObjectRequest);

    }

    public void listarLigas() {

        List<Liga> ligas = new ArrayList<Liga>();
        ligas = dataBaseHelper.getAllLigas();
        ArrayList<String> arrayLigas = new ArrayList<>();
        String[] contenido = new String[ligas.size()];

        for (int i = 0; i < ligas.size(); i++) {
            arrayLigas.add(ligas.get(i).getNombre());
            contenido[i] = ligas.get(i).getNombre();
        }

        //ArrayAdapter<String> adapterLigas = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayLigas);
        //adapterLigas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner_ligas = (Spinner) findViewById(R.id.spinner_ligas);
        //spinner_ligas.setAdapter(adapterLigas);
        spinner_ligas.setPrompt(getIntent().getStringExtra("nombreLiga"));
        LigasPartidosAdapter listAdapter = new LigasPartidosAdapter(this, contenido);
        spinner_ligas.setAdapter(listAdapter);
        tabs();
    }

    public void tabs() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        //final ListView listView = (ListView) findViewById(R.id.listView_partidos);


        for (int i = semanas.size() - 1; i >= 0; i--) {

            TabHost.TabSpec tabSpect = tabHost.newTabSpec("Texto");
            tabSpect.setIndicator("Semana " + ("\n") + semanas.get(i).getNumeroSemana());
            final ScrollView scrollView = new ScrollView(getApplicationContext());
            final LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setWeightSum(1);

            for (int j = 0; j < semanas.get(i).getFechas().size(); j++) {
                int dia = semanas.get(i).getFechas().get(j).getDia();
                int mes = semanas.get(i).getFechas().get(j).getMes();
                int anio = semanas.get(i).getFechas().get(j).getAnio();
                String fecha = dia + "/" + mes + "/" + anio;
                String[] nombreLocal = new String[semanas.get(i).getFechas().get(j).getPartidos().size()];
                String[] nombreVisitante = new String[semanas.get(i).getFechas().get(j).getPartidos().size()];
                Integer[] marcaLocal = new Integer[semanas.get(i).getFechas().get(j).getPartidos().size()];
                Integer[] marcaVisitante = new Integer[semanas.get(i).getFechas().get(j).getPartidos().size()];


                TextView textViewFecha = new TextView(getApplicationContext());
                textViewFecha.setText(fecha);

                for (int k = 0; k < semanas.get(i).getFechas().get(j).getPartidos().size(); k++) {

                    nombreLocal[k] = semanas.get(i).getFechas().get(j).getPartidos().get(k).getEquipoLocal().getNombre();
                    nombreVisitante[k] = semanas.get(i).getFechas().get(j).getPartidos().get(k).getEquipoVisitante().getNombre();
                    marcaLocal[k] = semanas.get(i).getFechas().get(j).getPartidos().get(k).getGolesLocal();
                    marcaVisitante[k] = semanas.get(i).getFechas().get(j).getPartidos().get(k).getGolesVisitante();


                }

                final PartidosAdapter partidosAdapter = new PartidosAdapter(this, nombreLocal, nombreVisitante, marcaLocal, marcaVisitante);
                ListView listView = new ListView(getApplicationContext());
                listView.setAdapter(partidosAdapter);


                linearLayout.addView(textViewFecha);
                linearLayout.addView(listView);


            }
            scrollView.addView(linearLayout);
            tabSpect.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String s) {
                    return scrollView;
                }
            });
            tabHost.addTab(tabSpect);

        }

        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {

            tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 90;
            TextView textView = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            //textView.setLayoutParams(new TableRow.LayoutParams(50, TableRow.LayoutParams.WRAP_CONTENT));ç
            //textView.setPadding(0,0,0,0);
            textView.setWidth(120);
            textView.setHeight(70);
            textView.setTextSize(10);

        }

    }
}
