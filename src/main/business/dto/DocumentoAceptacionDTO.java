package main.business.dto;

import java.sql.Date;

public class DocumentoAceptacionDTO {

    private final int acceptanceDocumentId;
    private final int assignmentId;
    private final String deliverableName;
    private final byte[] file;
    private final String status;

    private DocumentoAceptacionDTO(DocumentoAceptacionBuilder builder) {
        this.acceptanceDocumentId = builder.acceptanceDocumentId;
        this.assignmentId = builder.assignmentId;
        this.deliverableName = builder.deliverableName;
        this.file = builder.file;
        this.status = builder.status;
    }

    public int getAcceptanceDocumentId() {
        return acceptanceDocumentId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public String getDeliverableName() {
        return deliverableName;
    }

    public byte[] getFile() {
        return file;
    }

    public String getStatus() {
        return status;
    }

    public static class DocumentoAceptacionBuilder {

        private int acceptanceDocumentId;
        private int assignmentId;
        private String deliverableName;
        private byte[] file;
        private String status;

        public DocumentoAceptacionBuilder setAcceptanceDocumentId(int acceptanceDocumentId) {
            this.acceptanceDocumentId = acceptanceDocumentId;
            return this;
        }

        public DocumentoAceptacionBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public DocumentoAceptacionBuilder setDeliverableName(String deliverableName) {
            this.deliverableName = deliverableName;
            return this;
        }

        public DocumentoAceptacionBuilder setFile(byte[] file) {
            this.file = file;
            return this;
        }

        public DocumentoAceptacionBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public DocumentoAceptacionDTO build() {
            return new DocumentoAceptacionDTO(this);
        }
    }
}
