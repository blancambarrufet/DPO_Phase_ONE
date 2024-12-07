package business;

import persistance.Item;

import java.util.ArrayList;

public class ItemManager {


    //constructor
    public ItemManager() {
        ArrayList<Item> items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void printItem(String name) {
        // TODO implement here
        for (Item item: items) {
            System.out.println(item.getName());
        }


        return null;
    }


    private void loadItem() {
        // TODO implement here
        return null;
    }


    private Set<Item> listItem() {
        // TODO implement here
        return null;
    }
}
