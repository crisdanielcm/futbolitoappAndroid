package futbolitoapp.apliko.co.futbolitoapp.objects;

/**
 * Created by iosdeveloper on 7/09/16.
 */
public class Miembro {

    private int id;
    private String firstName;
    private String lastname;
    private String username;
    private String email;
    private int idGrupo;
    private boolean esCreador;
    private int puestoActual;
    private int puestoAnterior;
    private int puntaje;



    public Miembro(String firstName, String lastname, String username, String email, int idGrupo, boolean esCreador,
                   int puestoActual, int puestoAnterior, int puntaje) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.idGrupo = idGrupo;
        this.esCreador = esCreador;
        this.puestoActual = puestoActual;
        this.puestoAnterior = puestoAnterior;
        this.puntaje = puntaje;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEsCreador() {
        return esCreador;
    }

    public void setEsCreador(boolean esCreador) {
        this.esCreador = esCreador;
    }

    public int getPuestoActual() {
        return puestoActual;
    }

    public void setPuestoActual(int puestoActual) {
        this.puestoActual = puestoActual;
    }

    public int getPuestoAnterior() {
        return puestoAnterior;
    }

    public void setPuestoAnterior(int puestoAnterior) {
        this.puestoAnterior = puestoAnterior;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
}
