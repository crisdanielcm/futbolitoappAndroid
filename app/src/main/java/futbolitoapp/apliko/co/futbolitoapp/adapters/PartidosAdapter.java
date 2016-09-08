package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;

import futbolitoapp.apliko.co.futbolitoapp.R;
import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.helper.Pronostico;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONObjectRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

/**
 * Created by iosdeveloper on 29/08/16.
 */
public class PartidosAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] nombreLocal;
    private final String[] nombreVisitante;
    private final Integer[] marcaLocal;
    private final Integer[] marcaVisitante;
    private final int imageBackground;
    private Integer[] idPartido;

    //private final String[] item5;
    //private final String[] item6;
    //private final String[] item7;

    public PartidosAdapter(Activity context, String[] nombreLocal, String[] nombreVisitante, Integer[] marcaLocal,
                           Integer[] marcaVisitante, int image, Integer []id) {
        super(context, R.layout.activity_partidos_list_adapter, nombreLocal);
        this.context = context;
        this.nombreLocal = nombreLocal;
        this.nombreVisitante = nombreVisitante;
        this.marcaLocal = marcaLocal;
        this.marcaVisitante = marcaVisitante;
        this.imageBackground = image;
        this.idPartido = id;
        //this.item5 = item5;
        //this.item6 = item6;
        //this.item7 = item7;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_partidos_list_adapter, null, true);

        TableRow linearLayout = (TableRow) rowView.findViewById(R.id.layoutBackground);
        linearLayout.setBackgroundResource(imageBackground);


        final NumberPicker numberPicker = (NumberPicker) rowView.findViewById(R.id.textView_marcador_local);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(marcaLocal[position]);
        setNumberPickerTextColor(numberPicker, Color.WHITE);

        final NumberPicker numberPicker1 = (NumberPicker) rowView.findViewById(R.id.textView_marcador_visitante);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(20);
        numberPicker1.setWrapSelectorWheel(false);
        numberPicker1.setValue(marcaVisitante[position]);
        setNumberPickerTextColor(numberPicker1, Color.WHITE);

        TextView txtTitleLocal = (TextView) rowView.findViewById(R.id.textView_nombre_local);
        txtTitleLocal.setText(nombreLocal[position]);

        TextView txtTitlevisitante = (TextView) rowView.findViewById(R.id.textView_nombre_visitante);
        txtTitlevisitante.setText(nombreVisitante[position]);

        TextView marcadorLocal = (TextView) rowView.findViewById(R.id.textView_marca);
        marcadorLocal.setText(marcaLocal[position] + "-" + marcaVisitante[position]);

        ImageButton localButton = (ImageButton) rowView.findViewById(R.id.localButton);
        ImageButton visitanteButton = (ImageButton) rowView.findViewById(R.id.visitanteButton);

        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = numberPicker.getValue();
                numberPicker.setValue(value+1);
            }
        });

        visitanteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = numberPicker1.getValue();
                numberPicker1.setValue(value+1);
            }
        });

        Button buttonEnviar = (Button) rowView.findViewById(R.id.buttonEnviar);
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int valueLocal = numberPicker.getValue();
                int valueVisitante = numberPicker1.getValue();
                solicitudpronostico(valueLocal,valueVisitante,idPartido[position]);

            }
        });
        return rowView;
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

            }
        }
        return false;
    }

    public void solicitudpronostico(int goles_local, int goles_visitante, int id_partido) {

        HashMap<String, Integer> solicitud = new HashMap<>();
        solicitud.put("goles_local", goles_local);
        solicitud.put("goles_visitante", goles_visitante);
        solicitud.put("id_partido", id_partido);
        Pronostico pronostico = new Pronostico(goles_local, goles_visitante, id_partido);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        dataBaseHelper.createToDoPronostico(pronostico);

        JSONObject jsonObject = new JSONObject(solicitud);

        CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest(Request.Method.POST,
                Constantes.PRONOSTICO, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "El pronostico se ha registrado", Toast.LENGTH_SHORT).show();
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

}
