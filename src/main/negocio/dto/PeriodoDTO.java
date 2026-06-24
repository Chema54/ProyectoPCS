package main.negocio.dto;

import java.sql.Date;

public class PeriodoDTO {

    private final int periodoId;
    private final String nombre;
    private final Date fechaInicio;
    private final Date fechaFin;
    private final String estado;

    private PeriodoDTO(PeriodoBuilder builder) {
        this.periodoId = builder.periodoId;
        this.nombre = builder.nombre;
        this.fechaInicio = builder.fechaInicio;
        this.fechaFin = builder.fechaFin;
        this.estado = builder.estado;
    }

    public int getPeriodoId() {
        return periodoId;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public static class PeriodoBuilder {

        private int periodoId;
        private String nombre;
        private Date fechaInicio;
        private Date fechaFin;
        private String estado;

        public PeriodoBuilder setPeriodoId(int periodoId) {
            this.periodoId = periodoId;
            return this;
        }

        public PeriodoBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public PeriodoBuilder setFechaInicio(Date fechaInicio) {
            this.fechaInicio = fechaInicio;
            return this;
        }

        public PeriodoBuilder setFechaFin(Date fechaFin) {
            this.fechaFin = fechaFin;
            return this;
        }

        public PeriodoBuilder setEstado(String estado) {
            this.estado = estado;
            return this;
        }

        public PeriodoDTO build() {
            return new PeriodoDTO(this);
        }
    }
}
