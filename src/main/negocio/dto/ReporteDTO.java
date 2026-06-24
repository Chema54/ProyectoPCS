package main.negocio.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class ReporteDTO implements Entregable {

    private final int monthlyReportId;
    private final int asignacionId;
    private final String nombreEntregable;
    private final byte[] archivo;
    private final String estado;
    private final Date fechaLimite;
    private final int reportedHours;
    private final BigDecimal score;
    private final String comments;

    private ReporteDTO(ReportBuilder builder) {
        this.monthlyReportId = builder.monthlyReportId;
        this.asignacionId = builder.asignacionId;
        this.nombreEntregable = builder.nombreEntregable;
        this.archivo = builder.archivo;
        this.estado = builder.estado;
        this.fechaLimite = builder.fechaLimite;
        this.reportedHours = builder.reportedHours;
        this.score = builder.score;
        this.comments = builder.comments;
    }

    public int getMonthlyReportId() {
        return monthlyReportId;
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

    public int getReportedHours() {
        return reportedHours;
    }

    public BigDecimal getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }

    public static class ReportBuilder {

        private int monthlyReportId;
        private int asignacionId;
        private String nombreEntregable;
        private byte[] archivo;
        private String estado;
        private Date fechaLimite;
        private int reportedHours;
        private BigDecimal score;
        private String comments;

        public ReportBuilder setMonthlyReportId(int monthlyReportId) {
            this.monthlyReportId = monthlyReportId;
            return this;
        }

        public ReportBuilder setAsignacionId(int assignmentId) {
            this.asignacionId = assignmentId;
            return this;
        }

        public ReportBuilder setNombreEntregable(String deliverableName) {
            this.nombreEntregable = deliverableName;
            return this;
        }

        public ReportBuilder setArchivo(byte[] file) {
            this.archivo = file;
            return this;
        }

        public ReportBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public ReportBuilder setFechaLimite(Date deadline) {
            this.fechaLimite = deadline;
            return this;
        }

        public ReportBuilder setReportedHours(int reportedHours) {
            this.reportedHours = reportedHours;
            return this;
        }

        public ReportBuilder setScore(BigDecimal score) {
            this.score = score;
            return this;
        }

        public ReportBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }

        public ReporteDTO build() {
            return new ReporteDTO(this);
        }
    }
}
