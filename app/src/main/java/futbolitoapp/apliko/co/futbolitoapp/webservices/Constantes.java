package futbolitoapp.apliko.co.futbolitoapp.webservices;

/**
 * Created by iosdeveloper on 23/08/16.
 */
public class Constantes {

    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    private static final String PUERTO_HOST = "8000";

    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "http://192.168.0.17:";
    /**
     * URLs del Web Service
     */
    public static final String LOGIN = IP + PUERTO_HOST + "/rest-auth/login/";

    public static final String REGISTRO = IP + PUERTO_HOST + "/rest-auth/registration/";

    public static final String LIGAS = IP + PUERTO_HOST + "/ligas/";

    public static final String PARTIDOS = IP + PUERTO_HOST + "/obtener_partidos/";

    public static final String PRONOSTICO = IP + PUERTO_HOST + "/pronostico_vista/";

    public static final String GRUPOS = IP + PUERTO_HOST + "/obtener_grupos/";
}
