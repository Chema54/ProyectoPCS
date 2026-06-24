package main.negocio.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class EvaluacionOVDTO implements Entregable {

    private final int evaluacionId;
    private final int asignacionId;
    private final String nombreEntregable;
    private final byte[] archivo;
    private final String estado;
    private final Date fechaLimite;
    private final BigDecimal puntaje;
    private final String comentarios;

    private EvaluacionOVDTO(EvaluacionOVBuilder builder) {
        this.evaluacionId = builder.evaluacionId;
        this.asignacionId = builder.asignacionId;
        this.nombreEntregable = builder.nombreEntregable;
        this.archivo = builder.archivo;
        this.estado = builder.estado;
        this.fechaLimite = builder.fechaLimite;
        this.puntaje = builder.puntaje;
        this.comentarios = builder.comentarios;
    }

    public int getEvaluacionId() {
        return evaluacionId;
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

    public BigDecimal getPuntaje() {
        return puntaje;
    }

    public String getComentarios() {
        return comentarios;
    }

    public static class EvaluacionOVBuilder {

        private int evaluacionId;
        private int asignacionId;
        private String nombreEntregable;
        private byte[] archivo;
        private String estado;
        private Date fechaLimite;
        private BigDecimal puntaje;
        private String comentarios;

        public EvaluacionOVBuilder setEvaluacionId(int evaluacionId) {
            this.evaluacionId = evaluacionId;
            return this;
        }

        public EvaluacionOVBuilder setAsignacionId(int asignacionId) {
            this.asignacionId = asignacionId;
            return this;
        }

        public EvaluacionOVBuilder setNombreEntregable(String nombreEntregable) {
            this.nombreEntregable = nombreEntregable;
            return this;
        }

        public EvaluacionOVBuilder setArchivo(byte[] archivo) {
            this.archivo = archivo;
            return this;
        }

        public EvaluacionOVBuilder setEstado(String estado) {
            this.estado = estado;
            return this;
        }

        public EvaluacionOVBuilder setFechaLimite(Date fechaLimite) {
            this.fechaLimite = fechaLimite;
            return this;
        }

        public EvaluacionOVBuilder setPuntaje(BigDecimal puntaje) {
            this.puntaje = puntaje;
            return this;
        }

        public EvaluacionOVBuilder setComentarios(String comentarios) {
            this.comentarios = comentarios;
            return this;
        }

        public EvaluacionOVDTO build() {
            return new EvaluacionOVDTO(this);
        }

    }
}
