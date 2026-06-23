package main.business.dto;

public class InternDTO {

    private final int internId;
    private final String name;
    private final String paternalSurname;
    private final String maternalSurname;
    private final String email;
    private final String enrollment;
    private final String status;
    private final int userId;
    private final String username;

    private InternDTO(InternBuilder builder) {
        this.internId = builder.internId;
        this.name = builder.name;
        this.paternalSurname = builder.paternalSurname;
        this.maternalSurname = builder.maternalSurname;
        this.email = builder.email;
        this.enrollment = builder.enrollment;
        this.status = builder.status;
        this.userId = builder.userId;
        this.username = builder.username;
    }

    public int getInternId() {
        return internId;
    }

    public String getName() {
        return name;
    }

    public String getPaternalSurname() {
        return paternalSurname;
    }

    public String getMaternalSurname() {
        return maternalSurname;
    }

    public String getEmail() {
        return email;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public String getStatus() {
        return status;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return name + " " + paternalSurname + (maternalSurname != null && !maternalSurname.trim().isEmpty() ? " " + maternalSurname : "");
    }

    @Override
    public String toString() {
        return enrollment + " - " + name + " " + paternalSurname;
    }

    public static class InternBuilder {

        private int internId;
        private String name;
        private String paternalSurname;
        private String maternalSurname;
        private String email;
        private String enrollment;
        private String status;
        private int userId;
        private String username;

        public InternBuilder setInternId(int internId) {
            this.internId = internId;
            return this;
        }

        public InternBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public InternBuilder setPaternalSurname(String paternalSurname) {
            this.paternalSurname = paternalSurname;
            return this;
        }

        public InternBuilder setMaternalSurname(String maternalSurname) {
            this.maternalSurname = maternalSurname;
            return this;
        }

        public InternBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public InternBuilder setEnrollment(String enrollment) {
            this.enrollment = enrollment;
            return this;
        }

        public InternBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public InternBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public InternBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public InternDTO build() {
            return new InternDTO(this);
        }
    }
}
