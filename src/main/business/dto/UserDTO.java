/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dto;

import main.business.dto.enumeration.UserRole;
import main.common.BCrypt;

/**
 *
 * @author josem
 */
public class UserDTO {
    private int userID; 
    private String username;
    private String password;
    private UserRole role;
    private boolean access;


    
    public UserDTO(int userID, String username, String password, UserRole role, boolean access) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
        this.access = access;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean hasAccess() {
        return access;
    }

    @Override
    public boolean equals(Object instance) {

        if (this == instance) {
            return true;
        }

        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }

        UserDTO that = (UserDTO) instance;
        return username.equals(that.username)
                && access == that.access
                && role == that.role;
    }

    public boolean hasPasswordMatch(String candidate) {
        return BCrypt.checkpw(candidate, this.password);
    }

    public static String getGeneratedHashedPassword(String plain) {
        return BCrypt.hashpw(plain + "@Password", BCrypt.gensalt());
    }
}
