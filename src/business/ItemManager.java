package business;

import business.entities.Armor;
import business.entities.Item;
import business.entities.Member;
import business.entities.Weapon;
import persistance.ItemDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.ItemJsonDAO;


import java.util.List;

public class ItemManager {

    private final ItemDAO itemDAO;

    public ItemManager() throws PersistanceException {
        this.itemDAO = new ItemJsonDAO();
    }

    public boolean validatePersistenceSource() {
        return itemDAO.validateFile();
    }


}
