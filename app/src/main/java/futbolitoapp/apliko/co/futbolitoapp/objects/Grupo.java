package futbolitoapp.apliko.co.futbolitoapp.objects;

import java.util.ArrayList;

/**
 * Created by iosdeveloper on 7/09/16.
 */
public class Grupo {

    private int id;
    private String nombre;
    private ArrayList<Miembro>miembros;
    private int posicion;
    private int numeroMiembros;

    public Grupo(String nombre, int posicion, int numeroMiembros) {
        this.nombre = nombre;
        this.miembros = miembros;
        this.posicion = posicion;
        this.numeroMiembros = numeroMiembros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getNumeroMiembros() {
        return numeroMiembros;
    }

    public void setNumeroMiembros(int numeroMiembros) {
        this.numeroMiembros = numeroMiembros;
    }
}
