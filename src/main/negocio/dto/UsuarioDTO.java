/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.negocio.dto;

import main.negocio.dto.enumeracion.RolUsuario;


/**
 *
 * @author josem
 */
public class UsuarioDTO {

    private final int userID;
    private final String nombreUsuario;
    private final String password;
    private final RolUsuario role;
    private final boolean access;

    private UsuarioDTO(UsuarioBuilder builder) {
        this.userID = builder.userID;
        this.nombreUsuario = builder.nombreUsuario;
        this.password = builder.password;
        this.role = builder.role;
        this.access = builder.access;
    }

    public int getUserID() {
        return userID;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public RolUsuario getRole() {
        return role;
    }

    public boolean hasAccess() {
        return access;
    }

    @Override
    public String toString() {
        return nombreUsuario;
    }

    public boolean verifyPassword(String candidate) {
        return this.password.equals(candidate);
    }

    public static class UsuarioBuilder {

        private int userID;
        private String nombreUsuario;
        private String password;
        private RolUsuario role;
        private boolean access;

        public UsuarioBuilder setUserID(int userID) {
            this.userID = userID;
            return this;
        }

        public UsuarioBuilder setNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
            return this;
        }

        public UsuarioBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UsuarioBuilder setRole(RolUsuario role) {
            this.role = role;
            return this;
        }

        public UsuarioBuilder setAccess(boolean access) {
            this.access = access;
            return this;
        }

        public UsuarioDTO build() {
            return new UsuarioDTO(this);
        }
    }
}
