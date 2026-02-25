package game.impostgame.Util;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WordBank {

    private final Map<String, List<String>> words = Map.of(
            "TECNOLOGIA", List.of("microservicios", "backend", "api", "servidor"),
            "COMIDA", List.of("pizza", "hamburguesa", "pasta"),
            "ANIMALES", List.of("perro", "gato", "elefante")
    );

    public String getRandomWord(String category) {

        List<String> categoryWords = words.get(category);

        if (categoryWords == null) {
            throw new RuntimeException("Categoría no existe");
        }

        return categoryWords.get(new Random().nextInt(categoryWords.size()));
    }
}