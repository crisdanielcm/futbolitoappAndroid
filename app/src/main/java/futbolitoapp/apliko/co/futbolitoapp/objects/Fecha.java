package futbolitoapp.apliko.co.futbolitoapp.objects;

import java.util.ArrayList;

/**
 * Created by iosdeveloper on 5/09/16.
 */
public class Fecha {

    private int dia;
    private int anio;
    private int mes;
    private ArrayList<Partido> partidos;

    public Fecha(int dia, int anio, int mes, ArrayList<Partido> partidos) {
        this.dia = dia;
        this.anio = anio;
        this.mes = mes;
        this.partidos = partidos;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public ArrayList<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(ArrayList<Partido> partidos) {
        this.partidos = partidos;
    }
}
