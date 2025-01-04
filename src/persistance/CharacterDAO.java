package persistance;

import business.entities.Character;
import java.util.List;

public interface CharacterDAO {

    List<Character> loadAllCharacters();

    void saveCharacters(List<Character> characters);

    boolean validateFile();
}
