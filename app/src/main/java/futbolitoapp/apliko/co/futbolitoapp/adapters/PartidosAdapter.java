package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

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

import futbolitoapp.apliko.co.futbolitoapp.R;
import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.helper.Pronostico;
import futbolitoapp.apliko.co.futbolitoapp.objects.Fecha;
import futbolitoapp.apliko.co.futbolitoapp.objects.Partido;
import futbolitoapp.apliko.co.futbolitoapp.objects.PronosticoP;
import futbolitoapp.apliko.co.futbolitoapp.objects.Semana;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONObjectRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

/**
 * Created by iosdeveloper on 29/08/16.
 */
public class PartidosAdapter extends ArrayAdapter<String> {

    private static final String TAG = "PartidoAdapter";
    private final Activity context;
    private final String[] nombreLocal;
    private final String[] nombreVisitante;
    private final Integer[] marcaLocal;
    private final Integer[] marcaVisitante;
    private final Integer[] pronosticoLocal;
    private final Integer[] pronosticoVisitante;
    private final int imageBackground;
    private Integer[] idPartido;
    private List<Semana> semanas;

    //private final String[] item5;
    //private final String[] item6;
    //private final String[] item7;

    public PartidosAdapter(Activity context, String[] nombreLocal, String[] nombreVisitante, Integer[] marcaLocal,
                           Integer[] marcaVisitante, Integer[] pronosticoLocal,
                           Integer[] pronosticoVisitante, int image, Integer[] id, ArrayList<Semana> semanas) {
        super(context, R.layout.activity_partidos_list_adapter, nombreLocal);
        this.context = context;
        this.nombreLocal = nombreLocal;
        this.nombreVisitante = nombreVisitante;
        this.marcaLocal = marcaLocal;
        this.marcaVisitante = marcaVisitante;
        this.imageBackground = image;
        this.idPartido = id;
        this.semanas = semanas;
        this.pronosticoLocal = pronosticoLocal;
        this.pronosticoVisitante = pronosticoVisitante;
        //this.item5 = item5;
        //this.item6 = item6;
        //this.item7 = item7;
    }

    public static void setTextColor(EditText numberPicker1, EditText numberPicker, int marcador) {

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_partidos_list_adapter, null, true);

        TableRow linearLayout = (TableRow) rowView.findViewById(R.id.layoutBackground);
        linearLayout.setBackgroundResource(imageBackground);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "HelveticaNeue-Bold.otf");

        final EditText numberPicker = (EditText) rowView.findViewById(R.id.textView_marcador_local);
        numberPicker.setText(pronosticoLocal[position] + "");
        numberPicker.setTypeface(typeface);
        numberPicker.setEnabled(false);

        final EditText numberPicker1 = (EditText) rowView.findViewById(R.id.textView_marcador_visitante);
        numberPicker1.setText(pronosticoVisitante[position] + "");
        numberPicker1.setTypeface(typeface);
        numberPicker1.setEnabled(false);


        TextView txtTitleLocal = (TextView) rowView.findViewById(R.id.textView_nombre_local);
        txtTitleLocal.setText(nombreLocal[position]);
        txtTitleLocal.setTypeface(typeface);

        TextView txtTitlevisitante = (TextView) rowView.findViewById(R.id.textView_nombre_visitante);
        txtTitlevisitante.setText(nombreVisitante[position]);
        txtTitlevisitante.setTypeface(typeface);

        TextView textViewmarcadorLocal = (TextView) rowView.findViewById(R.id.textView_marca);
        textViewmarcadorLocal.setText(marcaLocal[position] + "-" + marcaVisitante[position]);
        textViewmarcadorLocal.setTypeface(typeface);


        ImageButton localButton = (ImageButton) rowView.findViewById(R.id.localButton);
        localButton.setEnabled(false);
        ImageButton visitanteButton = (ImageButton) rowView.findViewById(R.id.visitanteButton);
        visitanteButton.setEnabled(false);
        ImageView imageViewEstadoLocal = (ImageView) rowView.findViewById(R.id.imageViewEstadoLocal);
        ImageView imageViewEstadoVisitante = (ImageView) rowView.findViewById(R.id.imageViewEstadoVisitante);
        ImageView imageViewEstadoMarcadorLocal = (ImageView) rowView.findViewById(R.id.imageViewEstadoMarcadorLocal);
        ImageView imageViewEstadoMarcadorVisitante = (ImageView) rowView.findViewById(R.id.imageViewEstadoMarcadorVisitante);

        //si acierta el marcador local
        if (marcaLocal[position] == pronosticoLocal[position]) {
            imageViewEstadoMarcadorLocal.setImageResource(R.drawable.chulitoverdep);
        } else {
            imageViewEstadoMarcadorLocal.setImageResource(R.drawable.xp);
        }

        //si acierta el marcador visitante
        if (marcaVisitante[position] == pronosticoVisitante[position]) {
            imageViewEstadoMarcadorVisitante.setImageResource(R.drawable.chulitoverdep);
        } else {
            imageViewEstadoMarcadorVisitante.setImageResource(R.drawable.xp);
        }
        int marcadorLocal = marcaLocal[position];
        int marcadorVisitante = marcaVisitante[position];
        int pronoLocal = pronosticoLocal[position];
        int pronoVisitante = pronosticoVisitante[position];

        //si acierta el marcador partido local
        if (marcaLocal[position] > marcaVisitante[position] && pronosticoLocal[position] > pronosticoVisitante[position]) {
            imageViewEstadoLocal.setImageResource(R.drawable.chulitoverdeg);
            imageViewEstadoVisitante.setImageResource(R.drawable.xg);
        } else if (marcaLocal[position] < marcaVisitante[position] && pronosticoLocal[position] < pronosticoVisitante[position]) {
            imageViewEstadoLocal.setImageResource(R.drawable.xg);
            imageViewEstadoVisitante.setImageResource(R.drawable.chulitoverdeg);
        } else if (marcaLocal[position] == marcaVisitante[position] && pronosticoLocal[position] == pronosticoVisitante[position]) {
            imageViewEstadoLocal.setImageResource(R.drawable.chulitoverdeg);
            imageViewEstadoVisitante.setImageResource(R.drawable.chulitoverdeg);
        } else if ((pronosticoLocal[position] > pronosticoVisitante[position] || pronosticoLocal[position] < pronosticoVisitante[position]) && (marcaLocal[position] == marcaVisitante[position])) {
            imageViewEstadoLocal.setImageResource(R.drawable.xg);
            imageViewEstadoVisitante.setImageResource(R.drawable.xg);
        } else if ((marcaLocal[position] > marcaVisitante[position] || marcaLocal[position] < marcaVisitante[position]) && (pronosticoLocal[position] != pronosticoVisitante[position])) {
            imageViewEstadoLocal.setImageResource(R.drawable.xg);
            imageViewEstadoVisitante.setImageResource(R.drawable.xg);
        }

        int puntos = 0;

        // opciones ganadoras con todos los datos coincidiendo
        if (marcadorLocal == pronoLocal && marcadorVisitante == pronoVisitante && marcadorLocal > marcadorVisitante) {
            imageViewEstadoLocal.setImageResource(R.drawable.chulitoverdeg);
            imageViewEstadoVisitante.setImageResource(R.drawable.xg);
            puntos = 10;
        } else if (marcadorLocal == pronoLocal && marcadorVisitante == pronoVisitante && marcadorLocal < marcadorVisitante) {
            imageViewEstadoLocal.setImageResource(R.drawable.xg);
            imageViewEstadoVisitante.setImageResource(R.drawable.chulitoverdeg);
            puntos = 10;
        } else if (marcadorLocal == pronoLocal && marcadorVisitante == pronoVisitante && marcadorLocal == marcadorVisitante) {
            imageViewEstadoLocal.setImageResource(R.drawable.chulitoverdeg);
            imageViewEstadoVisitante.setImageResource(R.drawable.chulitoverdeg);
            puntos = 10;
        } else // Opciones ganadoras pronosticos y marcadores diferentes
            if (marcadorLocal > marcadorVisitante && pronoLocal > pronoVisitante && marcadorLocal != pronoLocal && marcadorVisitante != pronoVisitante) {
                imageViewEstadoLocal.setImageResource(R.drawable.chulitoverdeg);
                imageViewEstadoVisitante.setImageResource(R.drawable.xg);
                puntos = 5;
                if (Math.abs(marcadorLocal - marcadorVisitante) == Math.abs(pronoLocal - pronoVisitante)) {
                    puntos++;
                }
            } else if (marcadorLocal < marcadorVisitante && pronoLocal < pronoVisitante && marcadorLocal != pronoLocal && marcadorVisitante != pronoVisitante) {
                imageViewEstadoLocal.setImageResource(R.drawable.xg);
                imageViewEstadoVisitante.setImageResource(R.drawable.chulitoverdeg);
                puntos = 5;
                if (Math.abs(marcadorLocal - marcadorVisitante) == Math.abs(pronoLocal - pronoVisitante)) {
                    puntos++;
                }
            } else if (marcadorLocal == marcadorVisitante && pronoLocal == pronoVisitante && marcadorLocal != pronoLocal && marcadorVisitante != pronoVisitante) {
                imageViewEstadoLocal.setImageResource(R.drawable.chulitoverdeg);
                imageViewEstadoVisitante.setImageResource(R.drawable.chulitoverdeg);
                puntos = 5;
                if (Math.abs(marcadorLocal - marcadorVisitante) == Math.abs(pronoLocal - pronoVisitante)) {
                    puntos++;
                }
            } // Opciones combinadas
            else if ((pronoVisitante == marcadorVisitante || pronoLocal == marcadorLocal) && marcadorLocal > marcadorVisitante && pronoLocal > pronoVisitante) {
                imageViewEstadoLocal.setImageResource(R.drawable.chulitoverdeg);
                imageViewEstadoVisitante.setImageResource(R.drawable.xg);
                puntos = 7;
                if (Math.abs(marcadorLocal - marcadorVisitante) == Math.abs(pronoLocal - pronoVisitante)) {
                    puntos++;
                }
            } else if ((pronoVisitante == marcadorVisitante || pronoLocal == marcadorLocal) && marcadorLocal < marcadorVisitante && pronoLocal < pronoVisitante) {
                imageViewEstadoLocal.setImageResource(R.drawable.chulitoverdeg);
                imageViewEstadoVisitante.setImageResource(R.drawable.xg);
                puntos = 7;
                if (Math.abs(marcadorLocal - marcadorVisitante) == Math.abs(pronoLocal - pronoVisitante)) {
                    puntos++;
                }
            } else if ((marcadorLocal == pronoLocal || marcadorVisitante == pronoVisitante) && marcadorLocal == marcadorVisitante && (pronoLocal > pronoVisitante || pronoLocal < pronoVisitante)) {
                imageViewEstadoLocal.setImageResource(R.drawable.xg);
                imageViewEstadoVisitante.setImageResource(R.drawable.xg);
                puntos = 2;
                if (Math.abs(marcadorLocal - marcadorVisitante) == Math.abs(pronoLocal - pronoVisitante)) {
                    puntos++;
                }
            } else if ((marcadorLocal == pronoLocal || marcadorVisitante == pronoVisitante) && (marcadorLocal < marcadorVisitante || marcadorLocal > marcadorVisitante) && (pronoLocal == pronoVisitante)) {
                imageViewEstadoLocal.setImageResource(R.drawable.xg);
                imageViewEstadoVisitante.setImageResource(R.drawable.xg);
                puntos = 2;
                if (Math.abs(marcadorLocal - marcadorVisitante) == Math.abs(pronoLocal - pronoVisitante)) {
                    puntos++;
                }
            } else {
                imageViewEstadoLocal.setImageResource(R.drawable.xg);
                imageViewEstadoVisitante.setImageResource(R.drawable.xg);
                puntos = 0;
                if (Math.abs(marcadorLocal - marcadorVisitante) == Math.abs(pronoLocal - pronoVisitante)) {
                    puntos++;
                }
            }

        TextView textViewPuntos = (TextView) rowView.findViewById(R.id.textViewPuntos);
        if (puntos == 1) {
            textViewPuntos.setText(puntos + " punto");
            textViewPuntos.setTypeface(typeface);
        } else {
            textViewPuntos.setText(puntos + " puntos");
            textViewPuntos.setTypeface(typeface);
        }


        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valor = numberPicker.getText().toString();
                if (!valor.equals("")) {
                    int value = Integer.parseInt(valor);
                    numberPicker.setText((value + 1) + "");
                } else {
                    numberPicker.setText(0 + "");
                }
            }
        });

        visitanteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valor = numberPicker1.getText().toString();
                if (!valor.equals("")) {
                    int value = Integer.parseInt(valor);
                    numberPicker1.setText((value + 1) + "");
                } else {
                    numberPicker1.setText(0 + "");
                }
            }
        });

        //Button buttonEnviar = (Button) rowView.findViewById(R.id.buttonEnviar);
        TextView textmarcadorVisitante = (TextView) rowView.findViewById(R.id.textView_marcador_visitante);
        textmarcadorVisitante.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() != 0) {
                    int valueLocal;
                    int valueVisitante;
                    if (!numberPicker.getText().toString().equals("")) {
                        valueLocal = Integer.parseInt(numberPicker.getText().toString());
                    } else {
                        valueLocal = 0;
                    }
                    if (!numberPicker1.getText().toString().equals("")) {
                        valueVisitante = Integer.parseInt(numberPicker1.getText().toString());
                    } else {
                        valueVisitante = 0;
                    }

                    for (int i = 0; i < semanas.size(); i++) {
                        for (int j = 0; j < semanas.get(i).getFechas().size(); j++) {
                            for (int k = 0; k < semanas.get(i).getFechas().get(j).getPartidos().size(); k++) {
                                Partido partido = semanas.get(i).getFechas().get(j).getPartidos().get(k);
                                if (partido.getId() == idPartido[position]) {
                                    try {
                                        if (partido.getPronostico() == null) {
                                            solicitudpronostico(valueLocal, valueVisitante, idPartido[position], i, j, k);
                                        } else {
                                            actualizarPronostico(partido.getPronostico().getId(), valueLocal, valueVisitante, i, j, k);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                    }
                                }
                            }
                        }
                    }

                }
            }
        });
        numberPicker.setEnabled(false);
        numberPicker1.setEnabled(false);
        localButton.setEnabled(false);
        visitanteButton.setEnabled(false);


        TextView textMarcadorLocal = (TextView) rowView.findViewById(R.id.textView_marcador_local);
        textMarcadorLocal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() != 0) {
                    int valueLocal;
                    int valueVisitante;
                    if (!numberPicker.getText().toString().equals("")) {
                        valueLocal = Integer.parseInt(numberPicker.getText().toString());
                    } else {
                        valueLocal = 0;
                    }
                    if (!numberPicker1.getText().toString().equals("")) {
                        valueVisitante = Integer.parseInt(numberPicker1.getText().toString());
                    } else {
                        valueVisitante = 0;
                    }

                    for (int i = 0; i < semanas.size(); i++) {
                        for (int j = 0; j < semanas.get(i).getFechas().size(); j++) {
                            for (int k = 0; k < semanas.get(i).getFechas().get(j).getPartidos().size(); k++) {
                                Partido partido = semanas.get(i).getFechas().get(j).getPartidos().get(k);
                                if (partido.getId() == idPartido[position]) {
                                    try {
                                        if (partido.getPronostico() == null) {
                                            solicitudpronostico(valueLocal, valueVisitante, idPartido[position], i, j, k);
                                        } else {
                                            actualizarPronostico(partido.getPronostico().getId(), valueLocal, valueVisitante, i, j, k);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                    }
                                }
                            }
                        }
                    }

                }
            }
        });
        if (partidoEnVivo(idPartido[position])) {
            textViewmarcadorLocal.setText("VIVO");
            textMarcadorLocal.setEnabled(false);
            textmarcadorVisitante.setEnabled(false);
            textMarcadorLocal.setTextColor(Color.GRAY);
            textmarcadorVisitante.setTextColor(Color.GRAY);
            textViewPuntos.setText("");
            numberPicker.setEnabled(false);
            numberPicker1.setEnabled(false);
            localButton.setEnabled(false);
            visitanteButton.setEnabled(false);
            imageViewEstadoLocal.setEnabled(false);
            imageViewEstadoVisitante.setVisibility(ImageView.INVISIBLE);
            imageViewEstadoMarcadorLocal.setVisibility(ImageView.INVISIBLE);
            imageViewEstadoMarcadorVisitante.setVisibility(ImageView.INVISIBLE);
            imageViewEstadoLocal.setVisibility(ImageView.INVISIBLE);
        }

        if(partidoSinJugar(idPartido[position], textViewmarcadorLocal)){
            linearLayout.setBackgroundResource(R.drawable.fondomarcadores02);
            textMarcadorLocal.setTextColor(context.getResources().getColor(R.color.colorAccent));
            textmarcadorVisitante.setTextColor(context.getResources().getColor(R.color.colorAccent));
            textViewPuntos.setText("");
            numberPicker.setEnabled(true);
            numberPicker1.setEnabled(true);
            localButton.setEnabled(true);
            visitanteButton.setEnabled(true);
            imageViewEstadoLocal.setEnabled(false);
            imageViewEstadoVisitante.setVisibility(ImageView.INVISIBLE);
            imageViewEstadoMarcadorLocal.setVisibility(ImageView.INVISIBLE);
            imageViewEstadoMarcadorVisitante.setVisibility(ImageView.INVISIBLE);
            imageViewEstadoLocal.setVisibility(ImageView.INVISIBLE);
        }


        return rowView;
    }


    public void solicitudpronostico(final int goles_local, final int goles_visitante, final int id_partido, final int i, final int j, final int k) {

        HashMap<String, Integer> solicitud = new HashMap<>();
        solicitud.put("goles_local", goles_local);
        solicitud.put("goles_visitante", goles_visitante);
        solicitud.put("id_partido", id_partido);
        final Pronostico pronostico = new Pronostico(goles_local, goles_visitante, id_partido);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        dataBaseHelper.createToDoPronostico(pronostico);

        JSONObject jsonObject = new JSONObject(solicitud);

        CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                Constantes.PRONOSTICO, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int idPronostico = response.getInt("id_pronostico");
                    PronosticoP pronosticoP = new PronosticoP(idPronostico, goles_local, goles_visitante);
                    semanas.get(i).getFechas().get(j).getPartidos().get(k).setPronostico(pronosticoP);
                    Toast.makeText(context, "El pronostico se ha registrado ", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        }, context);

        VolleySingleton.getInstance(context).addToRequestQueue(customJSONObjectRequest);

    }

    public void actualizarPronostico(int id, final int goles_local, final int goles_visitante, final int i, final int j, final int k) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id_pronostico", id);
        jsonObject.put("goles_local", goles_local);
        jsonObject.put("goles_visitante", goles_visitante);
        VolleySingleton.getInstance(context).addToRequestQueue(new CustomJSONObjectRequest(
                Request.Method.PUT, Constantes.PRONOSTICO, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                semanas.get(i).getFechas().get(j).getPartidos().get(k).getPronostico().setPronosticoLocal(goles_local);
                semanas.get(i).getFechas().get(j).getPartidos().get(k).getPronostico().setPronosticoVisitante(goles_visitante);
                Toast.makeText(context, "El pronostico se ha actualizado ", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, context
        ));
    }

    public boolean partidoEnVivo(int idPartido) {
        Calendar calendar = Calendar.getInstance();
        int anioA = calendar.get(Calendar.YEAR);
        int mesA = calendar.get(Calendar.MONTH) + 1;
        int diaA = calendar.get(Calendar.DAY_OF_MONTH);
        int horaA = calendar.get(Calendar.HOUR_OF_DAY);
        int minutoA = calendar.get(Calendar.MINUTE);
        int segundoA = calendar.get(Calendar.SECOND);

        for (Semana semana : semanas) {
            for (Fecha fecha : semana.getFechas()) {
                for (Partido partido : fecha.getPartidos()) {
                    Log.i(TAG, "partidoSinJugar: "+partido.getFechaHora());
                    if (partido.getId() == idPartido) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        format.setTimeZone(TimeZone.getTimeZone("GMT"));
                        Date date = null;
                        Calendar calendarP = Calendar.getInstance();
                        try {
                            date = format.parse(partido.getFechaHora());
                            calendarP.setTime(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int anio = calendarP.get(Calendar.YEAR);
                        int mes = calendarP.get(Calendar.MONTH) + 1;
                        int dia = calendarP.get(Calendar.DAY_OF_MONTH);
                        int hora = calendarP.get(Calendar.HOUR_OF_DAY);
                        int minuto = calendarP.get(Calendar.MINUTE);
                        int segundo = calendarP.get(Calendar.SECOND);
                        if (anio == anioA && mesA == mes && dia == diaA) {
                            if (hora <= horaA && minuto <= minutoA) {
                                return true;
                            }
                        }
                    }
                }
            }

        }
        return false;

    }

    public boolean partidoSinJugar(int idPartido, TextView textViewmarcadorLocal) {
        Calendar calendar = Calendar.getInstance();
        int anioA = calendar.get(Calendar.YEAR);
        int mesA = calendar.get(Calendar.MONTH) + 1;
        int diaA = calendar.get(Calendar.DAY_OF_MONTH);
        int horaA = calendar.get(Calendar.HOUR_OF_DAY);
        int minutoA = calendar.get(Calendar.MINUTE);
        int segundoA = calendar.get(Calendar.SECOND);

        for (Semana semana : semanas) {
            for (Fecha fecha : semana.getFechas()) {
                for (Partido partido : fecha.getPartidos()) {
                    Log.i(TAG, "partidoSinJugar: "+partido.getFechaHora());
                    if (partido.getId() == idPartido) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        format.setTimeZone(TimeZone.getTimeZone("GMT"));
                        Date date = null;
                        Calendar calendarP = Calendar.getInstance();
                        try {
                            date = format.parse(partido.getFechaHora());
                            calendarP.setTime(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int anio = calendarP.get(Calendar.YEAR);
                        int mes = calendarP.get(Calendar.MONTH) + 1;
                        int dia = calendarP.get(Calendar.DAY_OF_MONTH);
                        int hora = calendarP.get(Calendar.HOUR_OF_DAY);
                        int minuto = calendarP.get(Calendar.MINUTE);
                        int segundo = calendarP.get(Calendar.SECOND);
                        if (anio >= anioA && mesA <= mes && dia > diaA) {
                            textViewmarcadorLocal.setText(dia+":"+minuto);
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }

}
