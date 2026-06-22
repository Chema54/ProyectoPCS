package main.business.dto;

import java.sql.Date;

public class DocumentoAceptacionDTO {

    private final int acceptanceDocumentId;
    private final int assignmentId;
    private final String deliverableName;
    private final String file;
    private final String status;
    private final Date deliveryDate;

    private DocumentoAceptacionDTO(DocumentoAceptacionBuilder builder) {
        this.acceptanceDocumentId = builder.acceptanceDocumentId;
        this.assignmentId = builder.assignmentId;
        this.deliverableName = builder.deliverableName;
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

    public String getDeliverableName() {
        return deliverableName;
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

    public static class DocumentoAceptacionBuilder {

        private int acceptanceDocumentId;
        private int assignmentId;
        private String deliverableName;
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

        public DocumentoAceptacionBuilder setDeliverableName(String deliverableName) {
            this.deliverableName = deliverableName;
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
