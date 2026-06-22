package main.business.dto;

public class PracticanteDTO {

    private final int internId;
    private final String name;
    private final String paternalSurname;
    private final String maternalSurname;
    private final String email;
    private final String enrollment;
    private final String status;
    private final int userId;
    private final String username;

    private PracticanteDTO(PracticanteBuilder builder) {
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

    public static class PracticanteBuilder {

        private int internId;
        private String name;
        private String paternalSurname;
        private String maternalSurname;
        private String email;
        private String enrollment;
        private String status;
        private int userId;
        private String username;

        public PracticanteBuilder setInternId(int internId) {
            this.internId = internId;
            return this;
        }

        public PracticanteBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PracticanteBuilder setPaternalSurname(String paternalSurname) {
            this.paternalSurname = paternalSurname;
            return this;
        }

        public PracticanteBuilder setMaternalSurname(String maternalSurname) {
            this.maternalSurname = maternalSurname;
            return this;
        }

        public PracticanteBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public PracticanteBuilder setEnrollment(String enrollment) {
            this.enrollment = enrollment;
            return this;
        }

        public PracticanteBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public PracticanteBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public PracticanteBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public PracticanteDTO build() {
            return new PracticanteDTO(this);
        }
    }
}
