package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Field;

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
    private final int imageBackground;

    //private final String[] item5;
    //private final String[] item6;
    //private final String[] item7;

    public PartidosAdapter(Activity context, String[] nombreLocal, String[] nombreVisitante, Integer[] marcaLocal,
                           Integer[] marcaVisitante, int image) {
        super(context, R.layout.activity_partidos_list_adapter, nombreLocal);
        this.context = context;
        this.nombreLocal = nombreLocal;
        this.nombreVisitante = nombreVisitante;
        this.marcaLocal = marcaLocal;
        this.marcaVisitante = marcaVisitante;
        this.imageBackground = image;
        //this.item5 = item5;
        //this.item6 = item6;
        //this.item7 = item7;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_partidos_list_adapter, null, true);

        TableRow linearLayout = (TableRow) rowView.findViewById(R.id.layoutBackground);
        linearLayout.setBackgroundResource(imageBackground);


        final NumberPicker numberPicker = (NumberPicker) rowView.findViewById(R.id.textView_marcador_local);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);
        numberPicker.setWrapSelectorWheel(false);
        setNumberPickerTextColor(numberPicker, Color.WHITE);

        final NumberPicker numberPicker1 = (NumberPicker) rowView.findViewById(R.id.textView_marcador_visitante);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(20);
        numberPicker1.setWrapSelectorWheel(false);
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

}
