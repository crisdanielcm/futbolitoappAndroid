package futbolitoapp.apliko.co.futbolitoapp.objects;

import futbolitoapp.apliko.co.futbolitoapp.helper.Liga;

/**
 * Created by iosdeveloper on 29/08/16.
 */
public class Temporada {

    private String fechaInicio;
    private String fechaFin;
    private Liga liga;
    private boolean esActual;

    public Temporada(String fechaInicio, String fechaFin, Liga liga, boolean esActual) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.liga = liga;
        this.esActual = esActual;
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

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }

    public boolean isEsActual() {
        return esActual;
    }

    public void setEsActual(boolean esActual) {
        this.esActual = esActual;
    }
}
