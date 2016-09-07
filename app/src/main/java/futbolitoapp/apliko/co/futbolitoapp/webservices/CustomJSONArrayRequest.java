package futbolitoapp.apliko.co.futbolitoapp.webservices;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.helper.Token;

/**
 * Created by iosdeveloper on 27/08/16.
 */
public class CustomJSONArrayRequest extends JsonArrayRequest {

    Context context;

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Content-Type","application/json");
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        List<Token>token = dataBaseHelper.getAllTokens();
        hashMap.put("Authorization","Token "+token.get(0).getToken());
        return hashMap;
    }

    public CustomJSONArrayRequest(int method, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener, Context context) {
        super(method, url, listener, errorListener);
        this.context = context;
    }

    public CustomJSONArrayRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener, Context context) {
        super(method, url, jsonRequest, listener, errorListener);
        this.context = context;
    }
}
