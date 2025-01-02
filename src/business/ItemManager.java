package business;

import persistance.Item;

import java.util.ArrayList;

public class ItemManager {

    ArrayList<Item> items;

    //constructor
//   public ItemManager() {
//        ArrayList<Item> items = new ArrayList<>();
//    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void printItem(String name) {
        // TODO implement here
        for (Item item: items) {
            if (item.getName().equals(name)) {
                System.out.println("\tID: "+ item.getId());
                System.out.println("\tName: "+ item.getName());
                System.out.println("\tClass: "+ item.getClass());
                System.out.println("\tPower: "+ item.getPower());
                System.out.println("\tDurability: "+ item.getDurability());
                break;
            }
        }


    }


    private void loadItem() {
        // TODO implement here
    }


    private Set<Item> listItem() {
        // TODO implement here
        return null;
    }
}
