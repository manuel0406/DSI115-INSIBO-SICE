package com.dsi.insibo.sice.Seguridad.ClasesDeSeguridad;

import java.security.SecureRandom;

import org.springframework.security.access.prepost.PreAuthorize;
@PreAuthorize("permitAll()")
public class PasswordGenerator {
    //----------------------------------------------------------------
    //         ALGORITMO GENERADOR DE CONTRASEÑA ALEATORIA
    //----------------------------------------------------------------
    // Definimos los caracteres que queremos que se incluyan en la contraseña
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";

    @PreAuthorize("permitAll()")
    public static String generateRandomPassword(int length) {
        // Concatenamos todos los caracteres posibles
        String allCharacters = UPPERCASE_LETTERS + LOWERCASE_LETTERS + NUMBERS;
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder(length);

        // Aseguramos que la contraseña contenga al menos un carácter de cada tipo
        password.append(UPPERCASE_LETTERS.charAt(random.nextInt(UPPERCASE_LETTERS.length())));
        password.append(LOWERCASE_LETTERS.charAt(random.nextInt(LOWERCASE_LETTERS.length())));
        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));

        // Rellenamos el resto de la contraseña con caracteres aleatorios
        for (int i = 4; i < length; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        // Mezclamos los caracteres para que no sigan un patrón fijo
        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = random.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }

        return new String(passwordArray);
    }

    //----------------------------------------------------------------
}
