package main.negocio.dto;

import java.sql.Date;

import java.math.BigDecimal;

public class AutoevaluacionDTO implements Entregable {

    private final int selfAssessmentId;
    private final int asignacionId;
    private final String nombreEntregable;
    private final byte[] archivo;
    private final BigDecimal score;
    private final String comments;
    private final String estado;
    private final Date fechaLimite; // Added because all deliverables have status

    private AutoevaluacionDTO(SelfAssessmentBuilder builder) {
        this.selfAssessmentId = builder.selfAssessmentId;
        this.asignacionId = builder.asignacionId;
        this.nombreEntregable = builder.nombreEntregable;
        this.archivo = builder.archivo;
        this.score = builder.score;
        this.comments = builder.comments;
        this.estado = builder.estado;
        this.fechaLimite = builder.fechaLimite;
    }

    public int getSelfAssessmentId() {
        return selfAssessmentId;
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

    public BigDecimal getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }
    
    public String getEstado() {
        return estado;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public static class SelfAssessmentBuilder {

        private int selfAssessmentId;
        private int asignacionId;
        private String nombreEntregable;
        private byte[] archivo;
        private BigDecimal score;
        private String comments;
        private String estado;
        private Date fechaLimite;

        public SelfAssessmentBuilder setSelfAssessmentId(int selfAssessmentId) {
            this.selfAssessmentId = selfAssessmentId;
            return this;
        }

        public SelfAssessmentBuilder setAsignacionId(int asignacionId) {
            this.asignacionId = asignacionId;
            return this;
        }

        public SelfAssessmentBuilder setNombreEntregable(String nombreEntregable) {
            this.nombreEntregable = nombreEntregable;
            return this;
        }

        public SelfAssessmentBuilder setArchivo(byte[] file) {
            this.archivo = file;
            return this;
        }

        public SelfAssessmentBuilder setScore(BigDecimal score) {
            this.score = score;
            return this;
        }

        public SelfAssessmentBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }
        
        public SelfAssessmentBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public SelfAssessmentBuilder setFechaLimite(Date deadline) {
            this.fechaLimite = deadline;
            return this;
        }

        public AutoevaluacionDTO build() {
            return new AutoevaluacionDTO(this);
        }
    }
}
