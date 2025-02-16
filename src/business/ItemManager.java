package business;

import business.entities.*;
import persistance.ItemDAO;
import persistance.api.exception.ApiException;
import persistance.json.exceptions.PersistanceException;
import persistance.ItemTotalDAO;


import java.util.List;

public class ItemManager {

    private final ItemDAO itemDAO;

    public ItemManager() throws PersistanceException, ApiException {
        this.itemDAO = new ItemTotalDAO();
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
