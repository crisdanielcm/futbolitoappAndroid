package futbolitoapp.apliko.co.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import com.jamper91.ws.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

/**
 * Esta clase se encarga de hacer el llamado al servicio web y retornar la informacion
 *
 * @author JorgeAndres
 */
public class WebServiceSecureSelfSigned extends AsyncTask<String, Long, String> {

    private static final String TAG = "WebServiceSecure";
    ProgressDialog progDailog;
    KeyStore keyStore = null;
    KeyManagerFactory kmf = null;
    URL url_1 = null;
    SSLContext context = null;
    //Variable con los datos para pasar al web service
    private Map<String, String> datos;
    //Url del servicio web
    private String url = "";
    //Actividad para mostrar el cuadro de progreso
    private Context actividad;
    //Resultado
    private String xml = null;
    //Clase a la cual se le retorna los datos dle ws
    private Asynchtask callback = null;
    //Mensaje a mostrar
    private String mensaje = null;
    /**
     * Crea una estancia de la clase webService para hacer consultas a ws
     *
     * @param urlWebService Url del servicio web
     * @param data          Datos a enviar del servicios web
     * @param activity      Actividad de donde se llama el servicio web, para mostrar el cuadro de "Cargando"
     * @param callback      CLase a la que se le retornara los datos del servicio web
     */
    public WebServiceSecureSelfSigned(String urlWebService, Map<String, String> data, Context activity, Asynchtask callback) {
        this.url = urlWebService;
        this.datos = data;
        this.actividad = activity;
        this.callback = callback;
    }


    public WebServiceSecureSelfSigned() {
        // TODO Auto-generated constructor stub
    }

    public WebServiceSecureSelfSigned(String urlWebService, Map<String, String> data, Context activity, Asynchtask callback, String mensaje) {
        this.url = urlWebService;
        this.datos = data;
        this.actividad = activity;
        this.callback = callback;
        this.mensaje = mensaje;
    }

    public Asynchtask getCallback() {
        return callback;
    }

    public void setCallback(Asynchtask callback) {
        this.callback = callback;
    }

    public HttpsClient getTolerantClient() {
        HttpsClient client = new HttpsClient(this.actividad);
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) client
                .getConnectionManager().getSchemeRegistry().getScheme("https")
                .getSocketFactory();
        final X509HostnameVerifier delegate = sslSocketFactory.getHostnameVerifier();
        if (!(delegate instanceof MyVerifier)) {
            sslSocketFactory.setHostnameVerifier(new MyVerifier(delegate));
        }
        return client;
    }

    private UrlEncodedFormEntity putData() {
        if (this.datos != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("id", "12345"));
            for (Map.Entry<String, String> entry : datos.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            try {
                return new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }

    }

    private String init() {
        try {
            // Setup a custom SSL Factory object which simply ignore the certificates validation and accept all type of self signed certificates
            SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
            sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            // Enable HTTP parameters
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            // Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sslFactory, 443));

            // Create a new connection manager using the newly created registry and then create a new HTTP client using this connection manager
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            HttpClient client = new DefaultHttpClient(ccm, params);
            HttpPost post = new HttpPost(this.getUrl());

            //Agrego los parametros a la peticion
            UrlEncodedFormEntity urlEncodedFormEntity = putData();
            if (urlEncodedFormEntity != null) {
                post.setEntity(urlEncodedFormEntity);
            }
            HttpResponse getResponse = null;

            getResponse = client.execute(post);
            String responseStr = EntityUtils.toString(getResponse.getEntity());
            HttpEntity responseEntity = getResponse.getEntity();
            return responseStr;
        } catch (Exception e) {
            Log.e(TAG + " init", e.getMessage());
            return "";
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) actividad.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean r = netInfo != null && netInfo.isConnectedOrConnecting();

        return r;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(actividad);
        this.datos.put("data[User][key]", pre.getString("UserKey", ""));
        if (mensaje != null) {
            if (isOnline()) {
                progDailog = new ProgressDialog(actividad);
                progDailog.setMessage(this.mensaje);
                progDailog.setIndeterminate(false);
                progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDailog.setCancelable(true);
                progDailog.show();
            }

        } else {
            progDailog = null;
        }


    }

    @Override
    protected String doInBackground(String... params) {
        if (isOnline()) {
            //String result = getDataWithMessage(this.url, actividad);
            String s = init();
            return s;
        } else {
            return "Sin conexión";
        }


    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        this.xml = response;
        if (progDailog != null)
            progDailog.dismiss();
        if (!response.equals("Sin conexión")) {

            //Retorno los datos
            HashMap<String, String> datos = this.procesarXml(response);
            int codigo = -1;
            String mensaje = this.actividad.getResources().getString(R.string.mensaje_02);
            if (datos != null) {
                try {
                    codigo = Integer.parseInt(datos.get("codigo"));
                    mensaje = datos.get("mensaje");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Log.e("WebService", e.getMessage());
                }
            }
            callback.processFinish(codigo, mensaje, this.xml);
        } else {
            callback.processCancel(-9999, response);
        }


    }

    public Map<String, String> getDatos() {
        return datos;
    }

    public void setDatos(Map<String, String> datos) {
        this.datos = datos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Context getActividad() {
        return actividad;
    }

    public void setActividad(Context actividad) {
        this.actividad = actividad;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public ProgressDialog getProgDailog() {
        return progDailog;
    }

    public void setProgDailog(ProgressDialog progDailog) {
        this.progDailog = progDailog;
    }

    private HashMap<String, String> procesarXml(String xml) {
        HashMap<String, String> retornar = new HashMap<String, String>();
        //Se crea un SAXBuilder para poder parsear el archivo
        SAXBuilder builder = new SAXBuilder();
        try {
            //Se crea el documento a traves del archivo
            org.jdom2.Document document = (org.jdom2.Document) builder.build(new StringReader(xml));

            //Se obtiene la raiz 'response'
            Element rootNode = document.getRootElement();

            //Se obtiene el hijo message
            Element message = rootNode.getChild("message");


            //Obtengo el elemento codigo
            String codigoR = message.getChildText("codigo");
            String mensaje = message.getChildText("mensaje");
            retornar.put("codigo", codigoR);
            retornar.put("mensaje", mensaje);
            return retornar;
        } catch (IOException io) {
            System.out.println(io.getMessage());
            return null;
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
            return null;
        }


    }

    public String getDataWithMessage(String URL, Context mContext) {
        DefaultHttpClient httpClient = null;

        try {
            // Setup a custom SSL Factory object which simply ignore the certificates validation and accept all type of self signed certificates
            SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
            sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            // Enable HTTP parameters
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            // Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sslFactory, 443));

            // Create a new connection manager using the newly created registry and then create a new HTTP client using this connection manager
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            httpClient = new DefaultHttpClient(ccm, params);
            HttpPost httpPost = new HttpPost(URL);
            //Agrego los parametros a la peticion
            UrlEncodedFormEntity urlEncodedFormEntity = putData();
            if (urlEncodedFormEntity != null) {
                httpPost.setEntity(urlEncodedFormEntity);
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String response = EntityUtils.toString(httpResponse.getEntity());
                return response;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("Error", TAG + " " + e.getMessage());
            httpClient.getConnectionManager().shutdown();
            return null;
        }
    }
}
