package futbolitoapp.apliko.co.webservice;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import com.jamper91.WebService.HttpRequest.HttpRequestException;
import com.jamper91.ws.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * Esta clase se encarga de hacer el llamado al servicio web y retornar la informacion
 * @author JorgeAndres
 *
 */
public class WebService extends AsyncTask<String, Long, String> {

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

	/**
	 * Crea una estancia de la clase webService para hacer consultas a ws
	 * @param urlWebService Url del servicio web
	 * @param data Datos a enviar del servicios web
	 * @param activity Actividad de donde se llama el servicio web, para mostrar el cuadro de "Cargando"
	 * @param callback CLase a la que se le retornara los datos del servicio web
	 */
	public  WebService(String urlWebService,Map<String, String> data, Context activity, Asynchtask callback) {
		this.url=urlWebService;
		this.datos=data;
		this.actividad=activity;
		this.callback=callback;
	}
	public WebService() {
		// TODO Auto-generated constructor stub
	}

	public  WebService(String urlWebService,Map<String, String> data, Context activity, Asynchtask callback, int mensaje) {
		this.url=urlWebService;
		this.datos=data;
		this.actividad=activity;
		this.callback=callback;
		if(mensaje>=0)
			this.mensaje = this.getActividad().getResources().getStringArray(R.array.mensajes)[mensaje];
		else
			this.mensaje =null;
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

    }
	@Override
	protected String doInBackground(String... params) {
		try {
			String r=HttpRequest.post(this.url).form(this.datos).body();
			return r;
			  
		} catch (HttpRequestException exception) {
			Log.e("doInBackground", exception.getMessage());
			
			return "Error HttpRequestException";
		} catch (Exception e) {
			Log.e("doInBackground", e.getMessage());
			return "Error Exception";
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
