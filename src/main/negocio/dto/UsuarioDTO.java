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

    private final int usuarioId;
    private final String nombreUsuario;
    private final String contrasenia;
    private final RolUsuario rol;
    private final boolean acceso;

    private UsuarioDTO(UsuarioBuilder builder) {
        this.usuarioId = builder.usuarioId;
        this.nombreUsuario = builder.nombreUsuario;
        this.contrasenia = builder.contrasenia;
        this.rol = builder.rol;
        this.acceso = builder.acceso;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public boolean tieneAcceso() {
        return acceso;
    }

    @Override
    public String toString() {
        return nombreUsuario;
    }

    public boolean verificarContrasenia(String candidate) {
        return this.contrasenia.equals(candidate);
    }

    public static class UsuarioBuilder {

        private int usuarioId;
        private String nombreUsuario;
        private String contrasenia;
        private RolUsuario rol;
        private boolean acceso;

        public UsuarioBuilder setUsuarioId(int usuarioId) {
            this.usuarioId = usuarioId;
            return this;
        }

        public UsuarioBuilder setNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
            return this;
        }

        public UsuarioBuilder setContrasenia(String contrasenia) {
            this.contrasenia = contrasenia;
            return this;
        }

        public UsuarioBuilder setRol(RolUsuario rol) {
            this.rol = rol;
            return this;
        }

        public UsuarioBuilder setAcceso(boolean acceso) {
            this.acceso = acceso;
            return this;
        }

        public UsuarioDTO build() {
            return new UsuarioDTO(this);
        }
    }
}
