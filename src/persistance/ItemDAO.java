package persistance;

import business.entities.Item;
import java.util.List;

public interface ItemDAO {
    List<Item> loadItems(); // Load items from JSON
    void saveItems(List<Item> items); // Save items to JSON

    boolean isFileOk();
}
