/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.basedatos;

import main.negocio.dto.UsuarioDTO;

/**
 *
 * @author josem
 */
public class Sesion {
    
    private static UsuarioDTO currentUser;

    public static UsuarioDTO getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UsuarioDTO user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
    }
}
