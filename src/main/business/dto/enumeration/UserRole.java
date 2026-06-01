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
    PROFESSOR(2),  
    COORDINATOR(3);   

    private final int role;

    UserRole(int role) {
        this.role = role;
    }

    public int getIdRol() {
        return role;
    }
}
