package futbolitoapp.apliko.co.futbolitoapp.helper;

/**
 * Created by iosdeveloper on 26/08/16.
 */
public class Liga {

    private int id;
    private String nombre;

    public Liga(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
