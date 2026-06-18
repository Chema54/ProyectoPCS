package main.business.dto;

import java.sql.Date;

public class DocumentoAceptacionDTO {

    private final int acceptanceDocumentId;
    private final int assignmentId;
    private final String file;
    private final String status;
    private final Date deliveryDate;

    private DocumentoAceptacionDTO(DocumentoAceptacionBuilder builder) {
        this.acceptanceDocumentId = builder.acceptanceDocumentId;
        this.assignmentId = builder.assignmentId;
        this.file = builder.file;
        this.status = builder.status;
        this.deliveryDate = builder.deliveryDate;
    }

    public int getAcceptanceDocumentId() {
        return acceptanceDocumentId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public String getFile() {
        return file;
    }

    public String getStatus() {
        return status;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) {
            return true;
        }
        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }
        DocumentoAceptacionDTO that = (DocumentoAceptacionDTO) instance;
        return acceptanceDocumentId == that.acceptanceDocumentId
                && assignmentId == that.assignmentId;
    }

    public static class DocumentoAceptacionBuilder {

        private int acceptanceDocumentId;
        private int assignmentId;
        private String file;
        private String status;
        private Date deliveryDate;

        public DocumentoAceptacionBuilder setAcceptanceDocumentId(int acceptanceDocumentId) {
            this.acceptanceDocumentId = acceptanceDocumentId;
            return this;
        }

        public DocumentoAceptacionBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public DocumentoAceptacionBuilder setFile(String file) {
            this.file = file;
            return this;
        }

        public DocumentoAceptacionBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public DocumentoAceptacionBuilder setDeliveryDate(Date deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public DocumentoAceptacionDTO build() {
            return new DocumentoAceptacionDTO(this);
        }
    }
}
