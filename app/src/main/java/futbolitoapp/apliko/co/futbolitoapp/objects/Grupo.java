package futbolitoapp.apliko.co.futbolitoapp.objects;

import java.util.ArrayList;

/**
 * Created by iosdeveloper on 7/09/16.
 */
public class Grupo {

    private int id;
    private String nombre;
    private String descripcion;
    private ArrayList<Miembro>miembros;

    public Grupo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.miembros = miembros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<Miembro> getMiembros() {
        return miembros;
    }

    public void setMiembros(ArrayList<Miembro> miembros) {
        this.miembros = miembros;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
