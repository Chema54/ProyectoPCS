/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.database;

import main.business.dto.UserDTO;

/**
 *
 * @author josem
 */
public class Session {
    
    private static UserDTO currentUser;

    public static UserDTO getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserDTO user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
    }
}
