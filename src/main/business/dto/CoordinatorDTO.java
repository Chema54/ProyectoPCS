/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dto;

/**
 *
 * @author josem
 */
public class CoordinatorDTO {

    private final int idCoordinator;
    private final int idUser;
    private final String username;
    private final String academicNumber;
    private final String name;

    public CoordinatorDTO(CoordinatorBuilder builder) {
        this.idCoordinator = builder.idCoordinator;
        this.idUser = builder.idUser;
        this.username = builder.username;
        this.academicNumber = builder.academicNumber;
        this.name = builder.name;
    }

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

    @Override
    public boolean equals(Object instance) {
        if (this == instance) {
            return true;
        }

        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }

        CoordinatorDTO that = (CoordinatorDTO) instance;

        return idCoordinator == that.idCoordinator
                && idUser == that.idUser
                && username.equals(that.username)
                && academicNumber.equals(that.academicNumber)
                && name.equals(that.name);
    }

    public static class CoordinatorBuilder {

        protected int idCoordinator;
        protected int idUser;
        protected String username;
        protected String academicNumber;
        protected String name;

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

        public CoordinatorDTO build() {
            return new CoordinatorDTO(this);
        }
    }

    @Override
    public String toString() {
        return username + " - " + name + " (" + academicNumber + ")";
    }
}