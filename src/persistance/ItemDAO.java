package persistance;

import business.entities.Armor;
import business.entities.Item;
import business.entities.Weapon;

import java.util.List;

public interface ItemDAO {
    List<Item> loadItems(); // Load items from JSON
    Item getItemById(int id);
    Weapon getRandomWeapon();
    Armor getRandomArmor();
    List<String> getItemNames();
    Item getItemByName(String name);

    boolean validateFile();
}
