package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;

import futbolitoapp.apliko.co.futbolitoapp.R;
import futbolitoapp.apliko.co.futbolitoapp.webservices.Constantes;
import futbolitoapp.apliko.co.futbolitoapp.webservices.CustomJSONArrayRequest;
import futbolitoapp.apliko.co.futbolitoapp.webservices.VolleySingleton;

public class GruposAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] itemList;
    int id;

    public GruposAdapter(Activity context,  String[] itemList, int id) {
        super(context, R.layout.activity_grupos_adapter, itemList);
        this.context = context;
        this.itemList = itemList;
        this.id = id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_grupos_adapter, null, true);

        TextView textView = (TextView) rowView.findViewById(R.id.textView_nombre_grupo);
        textView.setText(itemList[position]);

        CustomJSONArrayRequest customJSONArrayRequest = new CustomJSONArrayRequest(
                Request.Method.GET, Constantes.Mi, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {
                procesarRespuestaGrupos(response);
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
        }, getApplicationContext()
        );

        VolleySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(customJSONArrayRequest);
    };

        return rowView;
    }

    public void buscarPosicion(){

    }

}
