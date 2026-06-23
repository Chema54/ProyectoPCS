package main.business.dto;

public class ProfessorDTO {

    private final int professorId;
    private final String personalNumber;
    private final String name;
    private final String paternalSurname;
    private final String maternalSurname;
    private final String email;
    private final String status;
    private final int userId;
    private final String username;

    private ProfessorDTO(ProfessorBuilder builder) {
        this.professorId = builder.professorId;
        this.personalNumber = builder.personalNumber;
        this.name = builder.name;
        this.paternalSurname = builder.paternalSurname;
        this.maternalSurname = builder.maternalSurname;
        this.email = builder.email;
        this.status = builder.status;
        this.userId = builder.userId;
        this.username = builder.username;
    }

    public int getProfessorId() {
        return professorId;
    }

    public String getPersonalNumber() {
        return personalNumber;
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
        return name + " " + paternalSurname + " " + maternalSurname;
    }

    public static class ProfessorBuilder {

        private int professorId;
        private String personalNumber;
        private String name;
        private String paternalSurname;
        private String maternalSurname;
        private String email;
        private String status;
        private int userId;
        private String username;

        public ProfessorBuilder setProfessorId(int professorId) {
            this.professorId = professorId;
            return this;
        }

        public ProfessorBuilder setPersonalNumber(String personalNumber) {
            this.personalNumber = personalNumber;
            return this;
        }

        public ProfessorBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProfessorBuilder setPaternalSurname(String paternalSurname) {
            this.paternalSurname = paternalSurname;
            return this;
        }

        public ProfessorBuilder setMaternalSurname(String maternalSurname) {
            this.maternalSurname = maternalSurname;
            return this;
        }

        public ProfessorBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public ProfessorBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public ProfessorBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public ProfessorBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public ProfessorDTO build() {
            return new ProfessorDTO(this);
        }
    }
}
