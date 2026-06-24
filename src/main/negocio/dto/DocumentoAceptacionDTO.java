package main.negocio.dto;

import java.sql.Date;

public class DocumentoAceptacionDTO implements Entregable {

    private final int documentoAceptacionId;
    private final int asignacionId;
    private final String nombreEntregable;
    private final byte[] archivo;
    private final String estado;
    private final Date fechaLimite;

    private DocumentoAceptacionDTO(DocumentoAceptacionBuilder builder) {
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

    public static class DocumentoAceptacionBuilder {

        private int documentoAceptacionId;
        private int asignacionId;
        private String nombreEntregable;
        private byte[] archivo;
        private String estado;
        private Date fechaLimite;

        public DocumentoAceptacionBuilder setDocumentoAceptacionId(int documentoAceptacionId) {
            this.documentoAceptacionId = documentoAceptacionId;
            return this;
        }

        public DocumentoAceptacionBuilder setAsignacionId(int asignacionId) {
            this.asignacionId = asignacionId;
            return this;
        }

        public DocumentoAceptacionBuilder setNombreEntregable(String nombreEntregable) {
            this.nombreEntregable = nombreEntregable;
            return this;
        }

        public DocumentoAceptacionBuilder setArchivo(byte[] archivo) {
            this.archivo = archivo;
            return this;
        }

        public DocumentoAceptacionBuilder setEstado(String estado) {
            this.estado = estado;
            return this;
        }

        public DocumentoAceptacionBuilder setFechaLimite(Date fechaLimite) {
            this.fechaLimite = fechaLimite;
            return this;
        }

        public DocumentoAceptacionDTO build() {
            return new DocumentoAceptacionDTO(this);
        }
    }
}
