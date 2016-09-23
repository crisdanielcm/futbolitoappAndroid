package futbolitoapp.apliko.co.futbolitoapp.objects;

import java.util.ArrayList;

/**
 * Created by iosdeveloper on 29/08/16.
 */
public class Semana {

    private String fechaInicio;
    private String fechaFin;
    private Temporada temporada;
    private ArrayList<Fecha> fechas;
    private int numeroSemana;
    private int puntaje;

    public Semana(String fechaInicio, String fechaFin, Temporada temporada, ArrayList<Fecha> fechas, int numeroSemana) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.temporada = temporada;
        this.fechas = fechas;
        this.numeroSemana = numeroSemana;
    }

    public int getNumeroSemana() {
        return numeroSemana;
    }

    public void setNumeroSemana(int numeroSemana) {
        this.numeroSemana = numeroSemana;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Temporada getTemporada() {
        return temporada;
    }

    public void setTemporada(Temporada temporada) {
        this.temporada = temporada;
    }

    public ArrayList<Fecha> getFechas() {
        return fechas;
    }

    public void setFechas(ArrayList<Fecha> fechas) {
        this.fechas = fechas;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
}
