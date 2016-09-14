package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import futbolitoapp.apliko.co.futbolitoapp.R;

public class MiembrosAdapter extends ArrayAdapter<String> {

    private Activity context;
    private String[] itemListNombre;
    private int[] itemListPuntos, itemListPosicion, itemListImage;

    public MiembrosAdapter(Activity context, String[] itemListNombre, int[] itemListPuntos, int[] itemListPosicion, int[] itemListImage) {
        super(context, R.layout.activity_miembros_adapter, itemListNombre);
        this.context = context;
        this.itemListImage = itemListImage;
        this.itemListNombre = itemListNombre;
        this.itemListPosicion = itemListPosicion;
        this.itemListPuntos = itemListPuntos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_miembros_adapter, null, true);

        TextView textViewPosicion = (TextView) rowView.findViewById(R.id.textViewPosicion);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageViewEstado);
        TextView textViewNombre_miembro = (TextView) rowView.findViewById(R.id.textViewNombre_miembro);
        TextView textViewPuntos = (TextView) rowView.findViewById(R.id.textViewPuntos);

        textViewPosicion.setText(itemListPosicion[position]+"");
        textViewNombre_miembro.setText(itemListNombre[position]+"");
        textViewPuntos.setText(itemListPuntos[position]+"");
        //Estable
        if(itemListImage[position] == 0){
            imageView.setImageResource(R.drawable.flechaamarillavistagrupo);
        }else //subio
        if(itemListImage[position] == 1){
            imageView.setImageResource(R.drawable.flechaverde);
        }else //Bajo
        if(itemListImage[position] == 2){
            imageView.setImageResource(R.drawable.flecharoja);
        }

        return rowView;
    }
}
