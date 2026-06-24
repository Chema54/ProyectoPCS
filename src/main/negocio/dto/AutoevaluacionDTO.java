package main.negocio.dto;

import java.sql.Date;

import java.math.BigDecimal;

public class AutoevaluacionDTO implements Entregable {

    private final int autoevaluacionId;
    private final int asignacionId;
    private final String nombreEntregable;
    private final byte[] archivo;
    private final BigDecimal puntaje;
    private final String comentarios;
    private final String estado;
    private final Date fechaLimite; // Added because all deliverables have status

    private AutoevaluacionDTO(AutoevaluacionBuilder builder) {
        this.autoevaluacionId = builder.autoevaluacionId;
        this.asignacionId = builder.asignacionId;
        this.nombreEntregable = builder.nombreEntregable;
        this.archivo = builder.archivo;
        this.puntaje = builder.puntaje;
        this.comentarios = builder.comentarios;
        this.estado = builder.estado;
        this.fechaLimite = builder.fechaLimite;
    }

    public int getAutoevaluacionId() {
        return autoevaluacionId;
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

    public BigDecimal getPuntaje() {
        return puntaje;
    }

    public String getComentarios() {
        return comentarios;
    }
    
    public String getEstado() {
        return estado;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public static class AutoevaluacionBuilder {

        private int autoevaluacionId;
        private int asignacionId;
        private String nombreEntregable;
        private byte[] archivo;
        private BigDecimal puntaje;
        private String comentarios;
        private String estado;
        private Date fechaLimite;

        public AutoevaluacionBuilder setAutoevaluacionId(int autoevaluacionId) {
            this.autoevaluacionId = autoevaluacionId;
            return this;
        }

        public AutoevaluacionBuilder setAsignacionId(int asignacionId) {
            this.asignacionId = asignacionId;
            return this;
        }

        public AutoevaluacionBuilder setNombreEntregable(String nombreEntregable) {
            this.nombreEntregable = nombreEntregable;
            return this;
        }

        public AutoevaluacionBuilder setArchivo(byte[] file) {
            this.archivo = file;
            return this;
        }

        public AutoevaluacionBuilder setPuntaje(BigDecimal puntaje) {
            this.puntaje = puntaje;
            return this;
        }

        public AutoevaluacionBuilder setComentarios(String comentarios) {
            this.comentarios = comentarios;
            return this;
        }
        
        public AutoevaluacionBuilder setEstado(String estado) {
            this.estado = estado;
            return this;
        }

        public AutoevaluacionBuilder setFechaLimite(Date fechaLimite) {
            this.fechaLimite = fechaLimite;
            return this;
        }

        public AutoevaluacionDTO build() {
            return new AutoevaluacionDTO(this);
        }
    }
}
