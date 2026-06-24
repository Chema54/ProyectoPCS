package main.negocio.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class ReporteDTO implements Entregable {

    private final int reporteId;
    private final int asignacionId;
    private final String nombreEntregable;
    private final byte[] archivo;
    private final String estado;
    private final Date fechaLimite;
    private final int horasReportadas;
    private final BigDecimal puntaje;
    private final String comentarios;

    private ReporteDTO(ReporteBuilder builder) {
        this.reporteId = builder.reporteId;
        this.asignacionId = builder.asignacionId;
        this.nombreEntregable = builder.nombreEntregable;
        this.archivo = builder.archivo;
        this.estado = builder.estado;
        this.fechaLimite = builder.fechaLimite;
        this.horasReportadas = builder.horasReportadas;
        this.puntaje = builder.puntaje;
        this.comentarios = builder.comentarios;
    }

    public int getReporteId() {
        return reporteId;
    }

    public int getAsignacionId() {
        return asignacionId;
    }

    public String getNombreEntregable() {
        return nombreEntregable;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public String getEstado() {
        return estado;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public int getHorasReportadas() {
        return horasReportadas;
    }

    public BigDecimal getPuntaje() {
        return puntaje;
    }

    public String getComentarios() {
        return comentarios;
    }

    public static class ReporteBuilder {

        private int reporteId;
        private int asignacionId;
        private String nombreEntregable;
        private byte[] archivo;
        private String estado;
        private Date fechaLimite;
        private int horasReportadas;
        private BigDecimal puntaje;
        private String comentarios;

        public ReporteBuilder setReporteId(int reporteId) {
            this.reporteId = reporteId;
            return this;
        }

        public ReporteBuilder setAsignacionId(int asignacionId) {
            this.asignacionId = asignacionId;
            return this;
        }

        public ReporteBuilder setNombreEntregable(String nombreEntregable) {
            this.nombreEntregable = nombreEntregable;
            return this;
        }

        public ReporteBuilder setArchivo(byte[] archivo) {
            this.archivo = archivo;
            return this;
        }

        public ReporteBuilder setEstado(String estado) {
            this.estado = estado;
            return this;
        }

        public ReporteBuilder setFechaLimite(Date fechaLimite) {
            this.fechaLimite = fechaLimite;
            return this;
        }

        public ReporteBuilder setHorasReportadas(int horasReportadas) {
            this.horasReportadas = horasReportadas;
            return this;
        }

        public ReporteBuilder setPuntaje(BigDecimal puntaje) {
            this.puntaje = puntaje;
            return this;
        }

        public ReporteBuilder setComentarios(String comentarios) {
            this.comentarios = comentarios;
            return this;
        }

        public ReporteDTO build() {
            return new ReporteDTO(this);
        }

    }
}
