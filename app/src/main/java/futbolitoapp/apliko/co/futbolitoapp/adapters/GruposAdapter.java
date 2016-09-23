package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import futbolitoapp.apliko.co.futbolitoapp.R;

public class GruposAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] itemList;
    private final Integer[] itemList2;
    private final Integer[] itemList3;


    public GruposAdapter(Activity context,  String[] itemList, Integer[] itemList2, Integer[] itemList3) {
        super(context, R.layout.activity_grupos_adapter, itemList);
        this.context = context;
        this.itemList = itemList;
        this.itemList2 = itemList2;
        this.itemList3 = itemList3;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_grupos_adapter, null, true);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "HelveticaNeue-Bold.otf");
        TextView textViewNombreGrupo = (TextView) rowView.findViewById(R.id.textView_nombre_grupo);
        textViewNombreGrupo.setText(itemList[position]);
        textViewNombreGrupo.setTypeface(typeface);
        TextView textViewPosicion = (TextView) rowView.findViewById(R.id.textView_posicion_miembro);
        textViewPosicion.setText(itemList2[position] + "/" + itemList3[position]);
        textViewPosicion.setTypeface(typeface);
        return rowView;
    }


}
