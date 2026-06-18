package main.business.dto;

public class CoordinatorDTO {

    private final int idCoordinator;
    private final int idUser;
    private final String username;
    private final String academicNumber;
    private final String name;
    private final String lastName;
    private final String motherLastName;
    private final String email;

    public CoordinatorDTO(CoordinatorBuilder builder) {
        this.idCoordinator = builder.idCoordinator;
        this.idUser = builder.idUser;
        this.username = builder.username;
        this.academicNumber = builder.academicNumber;
        this.name = builder.name;
        this.lastName = builder.lastName;
        this.motherLastName = builder.motherLastName;
        this.email = builder.email;
    }

    // Getters
    public int getIDCoordinator() {
        return idCoordinator;
    }

    public int getIDUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getAcademicNumber() {
        return academicNumber;
    }

    public String getNombre() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMotherLastName() {
        return motherLastName;
    }

    public String getEmail() {
        return email;
    }

    public static class CoordinatorBuilder {

        protected int idCoordinator;
        protected int idUser;
        protected String username;
        protected String academicNumber;
        protected String name;
        protected String lastName;
        protected String motherLastName;
        protected String email;

        public CoordinatorBuilder setIDCoordinator(int idCoordinator) {
            this.idCoordinator = idCoordinator;
            return this;
        }

        public CoordinatorBuilder setIDUser(int idUser) {
            this.idUser = idUser;
            return this;
        }

        public CoordinatorBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public CoordinatorBuilder setAcademicNumber(String academicNumber) {
            this.academicNumber = academicNumber;
            return this;
        }

        public CoordinatorBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CoordinatorBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CoordinatorBuilder setMotherLastName(String motherLastName) {
            this.motherLastName = motherLastName;
            return this;
        }

        public CoordinatorBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public CoordinatorDTO build() {
            return new CoordinatorDTO(this);
        }
    }

    @Override
    public String toString() {
        return name + " " + lastName + " (" + academicNumber + ")";
    }
}
