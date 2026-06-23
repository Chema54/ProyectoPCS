package main.business.dto;

import java.sql.Date;

public class AcceptanceDocumentDTO {

    private final int acceptanceDocumentId;
    private final int assignmentId;
    private final String deliverableName;
    private final byte[] file;
    private final String status;

    private AcceptanceDocumentDTO(AcceptanceDocumentBuilder builder) {
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

    public static class AcceptanceDocumentBuilder {

        private int acceptanceDocumentId;
        private int assignmentId;
        private String deliverableName;
        private byte[] file;
        private String status;

        public AcceptanceDocumentBuilder setAcceptanceDocumentId(int acceptanceDocumentId) {
            this.acceptanceDocumentId = acceptanceDocumentId;
            return this;
        }

        public AcceptanceDocumentBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public AcceptanceDocumentBuilder setDeliverableName(String deliverableName) {
            this.deliverableName = deliverableName;
            return this;
        }

        public AcceptanceDocumentBuilder setFile(byte[] file) {
            this.file = file;
            return this;
        }

        public AcceptanceDocumentBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public AcceptanceDocumentDTO build() {
            return new AcceptanceDocumentDTO(this);
        }
    }
}
