package main.comun;

public class Validador {

    public static boolean isAlphabeticWithAccents(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        return text.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    public static boolean isAlphanumericWithAccentsAndPunctuation(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        return text.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\.,#-]+$");
    }

    public static boolean isValidStudentEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^zS\\d{8}@estudiantes\\.uv\\.mx$");
    }

    public static boolean isValidEnrollment(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            return false;
        }
        return matricula.matches("^S\\d{8}$");
    }

}
