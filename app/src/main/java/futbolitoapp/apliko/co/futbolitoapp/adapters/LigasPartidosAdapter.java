package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import futbolitoapp.apliko.co.futbolitoapp.R;

public class LigasPartidosAdapter extends BaseAdapter {

    private final Activity context;
    private final String[] itemList;
    private final LayoutInflater inflater;

    public LigasPartidosAdapter(Activity context, String[] itemList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        rowView = inflater.inflate(R.layout.activity_ligas_partidos_adapter, null);

        TextView textViewLigaP = (TextView) rowView.findViewById(R.id.textView_ligas_partidos);
        textViewLigaP.setText(itemList[position]);
        return rowView;
    }
}
