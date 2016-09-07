package futbolitoapp.apliko.co.futbolitoapp.webservices;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iosdeveloper on 25/08/16.
 */
public class CustomJSONObjectRequest extends JsonObjectRequest {

    public CustomJSONObjectRequest(int method, String url, JSONObject jsonObject, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Context applicationContext) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Content-Type","application/json; charset=utf-8");
        return super.getHeaders();
    }

    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }
}
