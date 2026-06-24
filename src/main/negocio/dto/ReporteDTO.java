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

    private ReporteDTO(ReporteBuilder builder) {
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

    public static class ReporteBuilder {

        private int monthlyReportId;
        private int asignacionId;
        private String nombreEntregable;
        private byte[] archivo;
        private String estado;
        private Date fechaLimite;
        private int reportedHours;
        private BigDecimal score;
        private String comments;

        public ReporteBuilder setMonthlyReportId(int monthlyReportId) {
            this.monthlyReportId = monthlyReportId;
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

        public ReporteBuilder setArchivo(byte[] file) {
            this.archivo = file;
            return this;
        }

        public ReporteBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public ReporteBuilder setFechaLimite(Date deadline) {
            this.fechaLimite = deadline;
            return this;
        }

        public ReporteBuilder setReportedHours(int reportedHours) {
            this.reportedHours = reportedHours;
            return this;
        }

        public ReporteBuilder setScore(BigDecimal score) {
            this.score = score;
            return this;
        }

        public ReporteBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }

        public ReporteDTO build() {
            return new ReporteDTO(this);
        }
    }
}
