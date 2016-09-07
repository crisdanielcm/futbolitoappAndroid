package futbolitoapp.apliko.co.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jamper91.WebService.HttpRequest.HttpRequestException;
import com.jamper91.ws.R;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.SSLContext;

/**
 * Esta clase se encarga de hacer el llamado al servicio web y retornar la informacion
 * @author JorgeAndres
 *
 */
public class WebServiceSecure extends AsyncTask<String, Long, String> {

	//Variable con los datos para pasar al web service
	private Map<String, String> datos;
	//Url del servicio web
	private String url= "";
	//Actividad para mostrar el cuadro de progreso
	private Context actividad;
	//Resultado
	private String xml=null;
	//Clase a la cual se le retorna los datos dle ws
	private Asynchtask callback=null;
	//Mensaje a mostrar
	private String mensaje = null;
	public Asynchtask getCallback() {
		return callback;
	}
	public void setCallback(Asynchtask callback) {
		this.callback = callback;
	}

	ProgressDialog progDailog;


	KeyStore keyStore = null;
	KeyManagerFactory kmf = null;
	URL url_1 = null;
	SSLContext context = null;
	private void init()
	{
		try {
			keyStore = KeyStore.getInstance("BKS");
			InputStream in = this.actividad.getResources().openRawResource(R.raw.mykst);
			try {
				// Initialize the keystore with the provided trusted certificates
				// Also provide the password of the keystore
				keyStore.load(in, "apliko2015".toCharArray());
				String algorithm = KeyManagerFactory.getDefaultAlgorithm();
				kmf = KeyManagerFactory.getInstance(algorithm);
				kmf.init((ManagerFactoryParameters) keyStore);
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Crea una estancia de la clase webService para hacer consultas a ws
	 * @param urlWebService Url del servicio web
	 * @param data Datos a enviar del servicios web
	 * @param activity Actividad de donde se llama el servicio web, para mostrar el cuadro de "Cargando"
	 * @param callback CLase a la que se le retornara los datos del servicio web
	 */
	public WebServiceSecure(String urlWebService, Map<String, String> data, Context activity, Asynchtask callback) {
		this.url=urlWebService;
		this.datos=data;
		this.actividad=activity;
		this.callback=callback;
		init();
	}
	public WebServiceSecure() {
		// TODO Auto-generated constructor stub
	}

	public WebServiceSecure(String urlWebService, Map<String, String> data, Context activity, Asynchtask callback, int mensaje) {
		this.url=urlWebService;
		this.datos=data;
		this.actividad=activity;
		this.callback=callback;
		if(mensaje>=0)
			this.mensaje = this.getActividad().getResources().getStringArray(R.array.mensajes)[mensaje];
		else
			this.mensaje =null;
		init();
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
		if(mensaje!=null)
		{
			progDailog = new ProgressDialog(actividad);
			progDailog.setMessage(this.mensaje);
			progDailog.setIndeterminate(false);
			progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDailog.setCancelable(true);
			progDailog.show();
		}else{
			progDailog=null;
		}


		try {
			context = SSLContext.getInstance("TLS");
			context.init(kmf.getKeyManagers(), null, null);
			url_1 = new URL(this.url);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}


	}
	@Override
	protected String doInBackground(String... params) {
		HttpsURLConnection urlConnection = null;
		try {
			urlConnection = (HttpsURLConnection) url_1.openConnection();
			urlConnection.setSSLSocketFactory(context.getSocketFactory());
			InputStream in_2 = urlConnection.getInputStream();
			return  in_2.toString();
		} catch (IOException e) {
			return "";
		}

	}
	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);
        this.xml=response;
		if(progDailog!=null)
        	progDailog.dismiss();
		//Retorno los datos
		HashMap<String, String> datos = this.procesarXml(response);
		int codigo = -1;
		String mensaje = "Ocurrio un error";
		if(datos!=null)
		{
			try {
				codigo =Integer.parseInt(datos.get("codigo"));
				mensaje =datos.get("mensaje");
			} catch (NumberFormatException e) {
				e.printStackTrace();
				Log.e("WebService", e.getMessage());
			}
		}
		callback.processFinish(codigo, mensaje, this.xml);
      
        
 
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

	private HashMap<String,String> procesarXml(String xml)
	{
		HashMap<String,String> retornar=new HashMap<String,String>();
		//Se crea un SAXBuilder para poder parsear el archivo
		SAXBuilder builder = new SAXBuilder();
		try
		{
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
			return  retornar;
		}catch ( IOException io ) {
			System.out.println( io.getMessage() );
			return null;
		}catch ( JDOMException jdomex ) {
			System.out.println( jdomex.getMessage() );
			return  null;
		}



	}
}
