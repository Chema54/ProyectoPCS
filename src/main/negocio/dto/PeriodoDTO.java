package main.negocio.dto;

import java.sql.Date;

public class PeriodoDTO {

    private final int periodoId;
    private final String nombre;
    private final Date fechaInicio;
    private final Date fechaFin;

    private PeriodoDTO(PeriodoBuilder builder) {
        this.periodoId = builder.periodoId;
        this.nombre = builder.nombre;
        this.fechaInicio = builder.fechaInicio;
        this.fechaFin = builder.fechaFin;
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
        if (fechaInicio == null || fechaFin == null) return "Desconocido";
        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.LocalDate inicio = fechaInicio.toLocalDate();
        java.time.LocalDate fin = fechaFin.toLocalDate();
        if (now.isBefore(inicio)) return "Próximo";
        if (now.isAfter(fin)) return "Concluido";
        return "Activo";
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

        public PeriodoBuilder setPeriodoId(int periodoId) {
            this.periodoId = periodoId;
            return this;
        }

        public PeriodoBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public PeriodoBuilder setFechaInicio(Date startDate) {
            this.fechaInicio = startDate;
            return this;
        }

        public PeriodoBuilder setFechaFin(Date endDate) {
            this.fechaFin = endDate;
            return this;
        }

        public PeriodoDTO build() {
            return new PeriodoDTO(this);
        }
    }
}
