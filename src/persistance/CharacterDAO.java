package persistance;

import business.entities.Character;
import java.util.List;

public interface CharacterDAO {

    List<Character> loadAllCharacters();

    boolean validateFile();

    Character getCharacterById(long id);

    Character getCharacterByName(String name);

    List<String> getCharactersByNames();
}
