package main.negocio.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class EvaluacionOVDTO implements Entregable {

    private final int linkedOrganizationEvaluationId;
    private final int asignacionId;
    private final String nombreEntregable;
    private final byte[] archivo;
    private final String estado;
    private final Date fechaLimite;
    private final BigDecimal score;
    private final String comments;

    private EvaluacionOVDTO(LinkedOrganizationEvaluationBuilder builder) {
        this.linkedOrganizationEvaluationId = builder.linkedOrganizationEvaluationId;
        this.asignacionId = builder.asignacionId;
        this.nombreEntregable = builder.nombreEntregable;
        this.archivo = builder.archivo;
        this.estado = builder.estado;
        this.fechaLimite = builder.fechaLimite;
        this.score = builder.score;
        this.comments = builder.comments;
    }

    public int getLinkedOrganizationEvaluationId() {
        return linkedOrganizationEvaluationId;
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

    public BigDecimal getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }

    public static class LinkedOrganizationEvaluationBuilder {

        private int linkedOrganizationEvaluationId;
        private int asignacionId;
        private String nombreEntregable;
        private byte[] archivo;
        private String estado;
        private Date fechaLimite;
        private BigDecimal score;
        private String comments;

        public LinkedOrganizationEvaluationBuilder setLinkedOrganizationEvaluationId(int linkedOrganizationEvaluationId) {
            this.linkedOrganizationEvaluationId = linkedOrganizationEvaluationId;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setAsignacionId(int asignacionId) {
            this.asignacionId = asignacionId;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setNombreEntregable(String nombreEntregable) {
            this.nombreEntregable = nombreEntregable;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setArchivo(byte[] file) {
            this.archivo = file;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setFechaLimite(Date deadline) {
            this.fechaLimite = deadline;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setScore(BigDecimal score) {
            this.score = score;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }

        public EvaluacionOVDTO build() {
            return new EvaluacionOVDTO(this);
        }
    }
}
