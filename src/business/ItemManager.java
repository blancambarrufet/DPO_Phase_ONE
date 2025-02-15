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


    public Armor getRandomArmor() {
        return itemDAO.getRandomArmor();
    }

    public Weapon getRandomWeapon() {
        return itemDAO.getRandomWeapon();
    }

    public List<String> getItemNames(){
        return itemDAO.getItemNames();
    }

    public Item getItemByName(String selectedItemName) {
        return itemDAO.getItemByName(selectedItemName);
    }

    public void equipItemsMember(Member member) throws PersistanceException {
        Weapon weapon = getRandomWeapon();
        Armor armor = getRandomArmor();

        member.equipWeapon(weapon);
        member.equipArmor(armor);
    }

    public void assignRandomWeapon(Member member) {
        Weapon weapon = getRandomWeapon();
        member.equipWeapon(weapon);
    }
}
