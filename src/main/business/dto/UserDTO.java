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

    private final int userID;
    private final String username;
    private final String password;
    private final UserRole role;
    private final boolean access;

    private UserDTO(UserBuilder builder) {
        this.userID = builder.userID;
        this.username = builder.username;
        this.password = builder.password;
        this.role = builder.role;
        this.access = builder.access;
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

        return userID == that.userID
                && access == that.access
                && username.equals(that.username)
                && role == that.role;
    }

    @Override
    public String toString() {
        return username;
    }

    public boolean hasPasswordMatch(String candidate) {
        return BCrypt.checkpw(candidate, this.password);
    }

    public static String getGeneratedHashedPassword(String plain) {
        return BCrypt.hashpw(plain + "@Password", BCrypt.gensalt());
    }

    public static class UserBuilder {

        private int userID;
        private String username;
        private String password;
        private UserRole role;
        private boolean access;

        public UserBuilder setUserID(int userID) {
            this.userID = userID;
            return this;
        }

        public UserBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setRole(UserRole role) {
            this.role = role;
            return this;
        }

        public UserBuilder setAccess(boolean access) {
            this.access = access;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
