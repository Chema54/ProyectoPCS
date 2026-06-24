package main.comun;

public class Validador {

    /**
     * Valida que una cadena contenga únicamente letras (incluyendo la 'ñ' y vocales con acentos) y espacios.
     */
    public static boolean isAlphabeticWithAccents(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        return text.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    /**
     * Valida que un texto sea alfanumérico (letras, números, acentos, espacios y algunos signos de puntuación básicos).
     * Ideal para nombres de organizaciones, direcciones, etc.
     */
    public static boolean isAlphanumericWithAccentsAndPunctuation(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        return text.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\.,#-]+$");
    }

    /**
     * Valida un correo de estudiante UV.
     */
    public static boolean isValidStudentEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^zS\\d{8}@estudiantes\\.uv\\.mx$");
    }

    /**
     * Valida una matrícula de estudiante UV.
     */
    public static boolean isValidEnrollment(String enrollment) {
        if (enrollment == null || enrollment.trim().isEmpty()) {
            return false;
        }
        return enrollment.matches("^S\\d{8}$");
    }
}
