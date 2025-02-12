package persistance;

import business.entities.Item;
import java.util.List;

public interface ItemDAO {
    List<Item> loadItems(); // Load items from JSON
    boolean validateFile();
}
