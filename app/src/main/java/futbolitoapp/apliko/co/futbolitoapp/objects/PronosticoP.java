package futbolitoapp.apliko.co.futbolitoapp.objects;

/**
 * Created by Christian on 10/09/2016.
 */
public class PronosticoP {

    private int id;
    private int pronosticoLocal;
    private int pronosticoVisitante;

    public PronosticoP(int id, int pronosticoLocal, int pronosticoVisitante) {
        this.id = id;
        this.pronosticoLocal = pronosticoLocal;
        this.pronosticoVisitante = pronosticoVisitante;
    }

    public int getPronosticoLocal() {
        return pronosticoLocal;
    }

    public void setPronosticoLocal(int pronosticoLocal) {
        this.pronosticoLocal = pronosticoLocal;
    }

    public int getPronosticoVisitante() {
        return pronosticoVisitante;
    }

    public void setPronosticoVisitante(int pronosticoVisitante) {
        this.pronosticoVisitante = pronosticoVisitante;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
