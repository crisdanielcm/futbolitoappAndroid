package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import futbolitoapp.apliko.co.futbolitoapp.R;

public class GruposAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] itemList;

    public GruposAdapter(Activity context,  String[] itemList) {
        super(context, R.layout.activity_grupos_adapter, itemList);
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_grupos_adapter, null, true);

        TextView textView = (TextView) rowView.findViewById(R.id.textView_nombre_grupo);
        textView.setText(itemList[position]);

        return rowView;
    }
}
