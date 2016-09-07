/**
 * 
 */
package futbolitoapp.apliko.co.webservice;

/**
 * @author JorgeAndres
 *
 */
public interface Asynchtask {
	/**
	 * ESta funcion retorna los datos devueltos por el ws
	 * @param codigo Valor de la respuesta del servicio web
	 * @param mensaje Texto de la respuesta
	 * @param xml Respuesta xml
	 */
	void processFinish(int codigo, String mensaje, String xml);
	void processCancel(int codigo, String mensaje);

}
