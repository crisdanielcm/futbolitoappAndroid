package futbolitoapp.apliko.co.futbolitoapp.helper;

/**
 * Created by iosdeveloper on 1/09/16.
 */
public class Pronostico {

    private int id;
    private int golesLocal;
    private int golesVisitante;
    private int idPartido;

    public Pronostico(int golesLocal, int golesVisitante, int idPartido) {
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.idPartido = idPartido;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
