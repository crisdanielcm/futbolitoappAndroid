package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import futbolitoapp.apliko.co.futbolitoapp.R;

public class LigasListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemList;

    public LigasListAdapter(Activity context, String[] itemList) {
        super(context, R.layout.activity_ligas_list_adapter, itemList);
        this.context = context;
        this.itemList = itemList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_ligas_list_adapter, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView_item_liga);
        txtTitle.setText(itemList[position]);
        return rowView;
    }
}
