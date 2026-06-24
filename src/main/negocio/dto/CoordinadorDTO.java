package main.negocio.dto;

public class CoordinadorDTO {

    private final int idCoordinator;
    private final int idUser;
    private final String nombreUsuario;
    private final String numeroPersonal;
    private final String nombre;
    private final String lastName;
    private final String motherLastName;
    private final String correo;

    public CoordinadorDTO(CoordinatorBuilder builder) {
        this.idCoordinator = builder.idCoordinator;
        this.idUser = builder.idUser;
        this.nombreUsuario = builder.username;
        this.numeroPersonal = builder.numeroPersonal;
        this.nombre = builder.name;
        this.lastName = builder.lastName;
        this.motherLastName = builder.motherLastName;
        this.correo = builder.email;
    }

    // Getters
    public int getIDCoordinator() {
        return idCoordinator;
    }

    public int getIDUser() {
        return idUser;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNumeroPersonal() {
        return numeroPersonal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMotherLastName() {
        return motherLastName;
    }

    public String getCorreo() {
        return correo;
    }

    public static class CoordinatorBuilder {

        protected int idCoordinator;
        protected int idUser;
        protected String username;
        protected String numeroPersonal;
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

        public CoordinatorBuilder setNombreUsuario(String username) {
            this.username = username;
            return this;
        }

        public CoordinatorBuilder setNumeroPersonal(String numeroPersonal) {
            this.numeroPersonal = numeroPersonal;
            return this;
        }

        public CoordinatorBuilder setNombre(String name) {
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

        public CoordinatorBuilder setCorreo(String email) {
            this.email = email;
            return this;
        }

        public CoordinadorDTO build() {
            return new CoordinadorDTO(this);
        }
    }

    @Override
    public String toString() {
        return nombre + " " + lastName + " (" + numeroPersonal + ")";
    }
}
