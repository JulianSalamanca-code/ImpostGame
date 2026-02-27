package game.impostgame.util;

import java.security.SecureRandom;

public class CodeGenerator {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateCode() {

        StringBuilder code = new StringBuilder();

        // 3 letras
        for (int i = 0; i < 3; i++) {
            code.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }

        // 3 números
        for (int i = 0; i < 3; i++) {
            code.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }

        return code.toString();
    }
}