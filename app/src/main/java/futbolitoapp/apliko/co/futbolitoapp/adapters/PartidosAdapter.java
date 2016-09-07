package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;

import futbolitoapp.apliko.co.futbolitoapp.R;

/**
 * Created by iosdeveloper on 29/08/16.
 */
public class PartidosAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] nombreLocal;
    private final String[] nombreVisitante;
    private final Integer[] marcaLocal;
    private final Integer[] marcaVisitante;

    //private final String[] item5;
    //private final String[] item6;
    //private final String[] item7;

    public PartidosAdapter(Activity context, String[] nombreLocal, String[] nombreVisitante, Integer[] marcaLocal,
                           Integer[] marcaVisitante) {
        super(context, R.layout.activity_partidos_list_adapter, nombreLocal);
        this.context = context;
        this.nombreLocal = nombreLocal;
        this.nombreVisitante = nombreVisitante;
        this.marcaLocal = marcaLocal;
        this.marcaVisitante = marcaVisitante;
        //this.item5 = item5;
        //this.item6 = item6;
        //this.item7 = item7;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_partidos_list_adapter, null, true);

        NumberPicker numberPicker = (NumberPicker) rowView.findViewById(R.id.textView_marcador_local);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);
        numberPicker.setWrapSelectorWheel(false);

        NumberPicker numberPicker1 = (NumberPicker) rowView.findViewById(R.id.textView_marcador_visitante);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(20);
        numberPicker1.setWrapSelectorWheel(false);

        TextView txtTitleLocal = (TextView) rowView.findViewById(R.id.textView_nombre_local);
        txtTitleLocal.setText(nombreLocal[position]);

        TextView txtTitlevisitante = (TextView) rowView.findViewById(R.id.textView_nombre_visitante);
        txtTitlevisitante.setText(nombreVisitante[position]);

        TextView marcadorLocal = (TextView) rowView.findViewById(R.id.textView_marca);
        marcadorLocal.setText(marcaLocal[position] + "-" + marcaVisitante[position]);

        return rowView;
    }


}
