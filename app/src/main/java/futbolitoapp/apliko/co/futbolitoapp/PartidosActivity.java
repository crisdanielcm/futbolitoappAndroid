package futbolitoapp.apliko.co.futbolitoapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
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
import futbolitoapp.apliko.co.futbolitoapp.objects.Equipo;
import futbolitoapp.apliko.co.futbolitoapp.objects.Fecha;
import futbolitoapp.apliko.co.futbolitoapp.objects.Partido;
import futbolitoapp.apliko.co.futbolitoapp.objects.Semana;
import futbolitoapp.apliko.co.futbolitoapp.objects.Temporada;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONArrayRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class PartidosActivity extends AppCompatActivity {

    private static final String TAG = "Partidos activity";
    private DataBaseHelper dataBaseHelper;
    private ArrayList<Semana> semanas;
    private ArrayList<Fecha> fechas;
    private ImageButton buttonGrupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidos);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
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

        buttonGrupos = (ImageButton) findViewById(R.id.imageButton2);
        buttonGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), GruposActivity.class);
                String nombre = ((Spinner)findViewById(R.id.spinner_ligas)).getPrompt().toString();
                intent.putExtra("nombreLiga",nombre);

                startActivity(intent);
            }
        });


    }

    public void solicitudPartidos(int id, final String nombre){

        HashMap<String, Integer> solicitudPartidos = new HashMap<>();
        solicitudPartidos.put("id_liga", id);

        JSONObject jsonObject = new JSONObject(solicitudPartidos);

        CustomJSONArrayRequest customJSONArrayRequest = new CustomJSONArrayRequest(
                Request.Method.POST, Constantes.PARTIDOS, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                Intent intent = new Intent(getApplicationContext(), PartidosActivity.class);
//                intent.putExtra("partidos", response.toString());
//                intent.putExtra("nombreLiga", nombre);
                procesarRespuestaPartidos(response);
                tabs();

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
                    int id_partido = partido.getInt("id");
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
                    partidoObject.setId(id_partido);
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
                solicitudPartidos(id, dataBaseHelper.getLiga(item).getNombre());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tabs();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void tabs() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.clearAllTabs();
        //final ListView listView = (ListView) findViewById(R.id.listView_partidos);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();


        for (int i = semanas.size() - 1; i >= 0; i--) {

            TabHost.TabSpec tabSpect = tabHost.newTabSpec("Texto");
            tabSpect.setIndicator("Semana " + ("\n") + semanas.get(i).getNumeroSemana());
            final ScrollView scrollView = new ScrollView(getApplicationContext());
            final LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            for (int j = 0; j < semanas.get(i).getFechas().size(); j++) {
                int dia = semanas.get(i).getFechas().get(j).getDia();
                int mes = semanas.get(i).getFechas().get(j).getMes();
                int anio = semanas.get(i).getFechas().get(j).getAnio();

                String fecha = new DateFormatSymbols().getMonths()[mes - 1] + " " + dia + ", " + anio;
                String[] nombreLocal = new String[semanas.get(i).getFechas().get(j).getPartidos().size()];
                String[] nombreVisitante = new String[semanas.get(i).getFechas().get(j).getPartidos().size()];
                Integer[] marcaLocal = new Integer[semanas.get(i).getFechas().get(j).getPartidos().size()];
                Integer[] marcaVisitante = new Integer[semanas.get(i).getFechas().get(j).getPartidos().size()];
                Integer[] idPartido = new Integer[semanas.get(i).getFechas().get(j).getPartidos().size()];


                TextView textViewFecha = new TextView(getApplicationContext());
                textViewFecha.setText(fecha);
                textViewFecha.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textViewFecha.setGravity(Gravity.CENTER);
                textViewFecha.setTextColor(Color.WHITE);
                textViewFecha.setPadding(0, 10, 0, 10);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    textViewFecha.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }

                for (int k = 0; k < semanas.get(i).getFechas().get(j).getPartidos().size(); k++) {

                    nombreLocal[k] = semanas.get(i).getFechas().get(j).getPartidos().get(k).getEquipoLocal().getNombre();
                    nombreVisitante[k] = semanas.get(i).getFechas().get(j).getPartidos().get(k).getEquipoVisitante().getNombre();
                    marcaLocal[k] = semanas.get(i).getFechas().get(j).getPartidos().get(k).getGolesLocal();
                    marcaVisitante[k] = semanas.get(i).getFechas().get(j).getPartidos().get(k).getGolesVisitante();
                    idPartido[k] =  semanas.get(i).getFechas().get(j).getPartidos().get(k).getId();

                }
                int image = 0;
                int dayAct = cal.get(Calendar.DAY_OF_MONTH);
                int monthAct = cal.get(Calendar.MONTH) + 1;
                if (dayAct == dia && monthAct == mes)
                    image = R.drawable.fondomarcadores01;
                else
                    image = R.drawable.fondomarcadores02;

                final PartidosAdapter partidosAdapter = new PartidosAdapter(this, nombreLocal, nombreVisitante, marcaLocal, marcaVisitante, image, idPartido);
                ListView listView = new ListView(getApplicationContext());

                listView.setAdapter(partidosAdapter);
                listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(130) * (semanas.get(i).getFechas().get(j).getPartidos().size()) + textViewFecha.getLayoutParams().height + 50));

                LinearLayout linearLayout2 = new LinearLayout(getApplicationContext());
                linearLayout2.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                linearLayout2.setLayoutParams(layoutParams2);
                linearLayout2.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                linearLayout2.addView(textViewFecha);
                linearLayout2.addView(listView);
                linearLayout2.setPadding(0, 30, 0, 0);

                linearLayout.addView(linearLayout2);


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

            tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 70;
            TextView textView = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            //textView.setTextColor(ColorStateList.createFromXml(getResources().getDrawable(R.drawable.textcolortabs)));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            //textView.setLayoutParams(new TableRow.LayoutParams(50, TableRow.LayoutParams.WRAP_CONTENT));ç
            textView.setPadding(0, 5, 0, 0);
            textView.setWidth(120);
            textView.setHeight(70);
            textView.setTextSize(10);

        }

    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
