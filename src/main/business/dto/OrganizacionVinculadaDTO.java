package main.business.dto;

public class OrganizacionVinculadaDTO {

    private final int organizationId;
    private final String businessName;
    private final String location;
    private final String phoneNumber;
    private final String email;

    private OrganizacionVinculadaDTO(OrganizacionVinculadaBuilder builder) {
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
    public boolean equals(Object instance) {
        if (this == instance) {
            return true;
        }
        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }
        OrganizacionVinculadaDTO that = (OrganizacionVinculadaDTO) instance;
        return organizationId == that.organizationId
                && businessName.equals(that.businessName)
                && location.equals(that.location)
                && email.equals(that.email);
    }

    @Override
    public String toString() {
        return businessName;
    }

    public static class OrganizacionVinculadaBuilder {

        private int organizationId;
        private String businessName;
        private String location;
        private String phoneNumber;
        private String email;

        public OrganizacionVinculadaBuilder setOrganizationId(int organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public OrganizacionVinculadaBuilder setBusinessName(String businessName) {
            this.businessName = businessName;
            return this;
        }

        public OrganizacionVinculadaBuilder setLocation(String location) {
            this.location = location;
            return this;
        }

        public OrganizacionVinculadaBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public OrganizacionVinculadaBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public OrganizacionVinculadaDTO build() {
            return new OrganizacionVinculadaDTO(this);
        }
    }
}
