package main.basedatos;

import main.negocio.dto.UsuarioDTO;

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
