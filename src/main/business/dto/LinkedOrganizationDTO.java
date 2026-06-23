package main.business.dto;

public class LinkedOrganizationDTO {

    private final int organizationId;
    private final String businessName;
    private final String location;
    private final String phoneNumber;
    private final String email;

    private LinkedOrganizationDTO(LinkedOrganizationBuilder builder) {
        this.organizationId = builder.organizationId;
        this.businessName = builder.businessName;
        this.location = builder.location;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getLocation() {
        return location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return businessName;
    }

    public static class LinkedOrganizationBuilder {

        private int organizationId;
        private String businessName;
        private String location;
        private String phoneNumber;
        private String email;

        public LinkedOrganizationBuilder setOrganizationId(int organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public LinkedOrganizationBuilder setBusinessName(String businessName) {
            this.businessName = businessName;
            return this;
        }

        public LinkedOrganizationBuilder setLocation(String location) {
            this.location = location;
            return this;
        }

        public LinkedOrganizationBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public LinkedOrganizationBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public LinkedOrganizationDTO build() {
            return new LinkedOrganizationDTO(this);
        }
    }
}
