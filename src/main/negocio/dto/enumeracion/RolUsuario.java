/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package main.negocio.dto.enumeracion;

/**
 *
 * @author josem
 */
public enum RolUsuario {
    INTERN(1),
    COORDINADOR(2), 
    PROFESSOR(3);
     

    private final int role;

    RolUsuario(int role) {
        this.role = role;
    }

    public int getIdRol() {
        return role;
    }
    
    public static RolUsuario fromId(int id) {
        for (RolUsuario role : values()) {
            if (role.getIdRol() == id) {
                return role;
            }
        }
        throw new IllegalArgumentException(
            "Rol no válido: " + id
        );
    }
}
