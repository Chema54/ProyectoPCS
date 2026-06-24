package main.negocio.dto;

import java.sql.Date;

public class DocumentoAceptacionDTO implements Entregable {

    private final int documentoAceptacionId;
    private final int asignacionId;
    private final String nombreEntregable;
    private final byte[] archivo;
    private final String estado;
    private final Date fechaLimite;

    private DocumentoAceptacionDTO(AcceptanceDocumentBuilder builder) {
        this.documentoAceptacionId = builder.documentoAceptacionId;
        this.asignacionId = builder.asignacionId;
        this.nombreEntregable = builder.nombreEntregable;
        this.archivo = builder.archivo;
        this.estado = builder.estado;
        this.fechaLimite = builder.fechaLimite;
    }

    public int getDocumentoAceptacionId() {
        return documentoAceptacionId;
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

    public static class AcceptanceDocumentBuilder {

        private int documentoAceptacionId;
        private int asignacionId;
        private String nombreEntregable;
        private byte[] archivo;
        private String estado;
        private Date fechaLimite;

        public AcceptanceDocumentBuilder setDocumentoAceptacionId(int acceptanceDocumentId) {
            this.documentoAceptacionId = acceptanceDocumentId;
            return this;
        }

        public AcceptanceDocumentBuilder setAsignacionId(int asignacionId) {
            this.asignacionId = asignacionId;
            return this;
        }

        public AcceptanceDocumentBuilder setNombreEntregable(String nombreEntregable) {
            this.nombreEntregable = nombreEntregable;
            return this;
        }

        public AcceptanceDocumentBuilder setArchivo(byte[] file) {
            this.archivo = file;
            return this;
        }

        public AcceptanceDocumentBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public AcceptanceDocumentBuilder setFechaLimite(Date deadline) {
            this.fechaLimite = deadline;
            return this;
        }

        public DocumentoAceptacionDTO build() {
            return new DocumentoAceptacionDTO(this);
        }
    }
}
