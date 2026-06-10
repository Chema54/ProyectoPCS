/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package main.business.dto.enumeration;

/**
 *
 * @author josem
 */
public enum UserRole {
    INTERN(1),
    COORDINADOR(2), 
    PROFESSOR(3);
     

    private final int role;

    UserRole(int role) {
        this.role = role;
    }

    public int getIdRol() {
        return role;
    }
    
    public static UserRole fromId(int id) {
        for (UserRole role : values()) {
            if (role.getIdRol() == id) {
                return role;
            }
        }
        throw new IllegalArgumentException(
            "Rol no válido: " + id
        );
    }
}
