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
    private static final String IP = "http://192.168.0.22:";
    /**
     * URLs del Web Service
     */
    public static final String LOGIN = IP + PUERTO_HOST + "/rest-auth/login/";

    public static final String REGISTRO = IP + PUERTO_HOST + "/rest-auth/registration/";

    public static final String LIGAS = IP + PUERTO_HOST + "/ligas/";

    public static final String PARTIDOS = IP + PUERTO_HOST + "/obtener_partidos/";

    public static final String PRONOSTICO = IP + PUERTO_HOST + "/pronostico_vista/";

    public static final String GRUPOS = IP + PUERTO_HOST + "/obtener_grupos_liga/";

    public static final String GRUPOS_IND = IP + PUERTO_HOST + "/obtener_grupos/";

    public static final String MIEMBROS = IP + PUERTO_HOST + "/obtener_miembros/";

    public static final String CREAR_GRUPO = IP + PUERTO_HOST + "/crear_grupo/";

    public static final String CAMBIAR_CONTRASENIA = IP + PUERTO_HOST + "/rest-auth/password/change/";

    public static final String REGISTRAR_DISPOSITIVO = IP + PUERTO_HOST + "/registrar_dispositivo/";

    public static final String DAR_BAJA= IP + PUERTO_HOST + "/dar_baja_usuario/";

    public static final String FACEBOOK= IP + PUERTO_HOST + "/rest-auth/facebook/";

    public static final String GLOBAL= IP + PUERTO_HOST + "/registrar_global/";

    public static final String PUNTAJE_SEMANAL= IP + PUERTO_HOST + "/puntaje_semanal/";

    public static final String PUNTAJE_TEMPORADA= IP + PUERTO_HOST + "/puntaje_temporada/";


}
