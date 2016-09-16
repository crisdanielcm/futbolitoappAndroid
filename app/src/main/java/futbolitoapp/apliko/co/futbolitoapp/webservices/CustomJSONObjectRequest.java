package futbolitoapp.apliko.co.futbolitoapp.webservices;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;
import futbolitoapp.apliko.co.futbolitoapp.helper.Token;

/**
 * Created by iosdeveloper on 25/08/16.
 */
public class CustomJSONObjectRequest extends JsonObjectRequest {

    private final Context context;

    public CustomJSONObjectRequest(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Context context) {
        super(method, url, listener, errorListener);
        this.context = context;
    }

    public CustomJSONObjectRequest(int method, String url, JSONObject jsonObject, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Context applicationContext) {
        super(method, url, jsonObject, listener, errorListener);
        this.context = applicationContext;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        List<Token> token = dataBaseHelper.getAllTokens();
        if(token.size()>0)
            hashMap.put("Authorization", "Token " + token.get(0).getToken());
        return hashMap;
    }

}
