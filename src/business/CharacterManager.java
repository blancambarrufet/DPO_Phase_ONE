package business;

import java.io.*;
import java.util.*;
import persistance.Character;


public class CharacterManager {
    ArrayList<Character> characters;
/*
    private void loadCharacters() {
        // TODO implement here

        String filePath = "path/to/your/file.json";

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("/Users/User/Desktop/course.json"));
            JSONObject jsonObject = (JSONObject)obj;
            String name = (String)jsonObject.get("Name");
            String course = (String)jsonObject.get("Course");
            JSONArray subjects = (JSONArray)jsonObject.get("Subjects");
            System.out.println("Name: " + name);
            System.out.println("Course: " + course);
            System.out.println("Subjects:");
            Iterator iterator = subjects.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

*/

    public void printCharacter(String characterName) {
        //read and save the contents of the jonson file
        //store them in the ArrayList

        boolean found=false;

        do{
            Character actualCharacter = characters.removeFirst();


            if(characterName.equals(actualCharacter.getName())){
                System.out.println("Id: " + actualCharacter.getId());
                System.out.println("Name: " + actualCharacter.getName());
                System.out.println("Weight: " + actualCharacter.getWeight());

                found=true;
            }else found=false;

        }while(!found ||!characters.isEmpty());

        if (!found) System.out.println("Character not found");


    }

    private void characterList(){
        //read and save the contents of the jonson file
        //store them in the ArrayList

        int i=0;
        do{
            Character actualCharacter = characters.removeFirst();
            System.out.println("\t"+i+") " + actualCharacter.getName());
            i++;
        }while(!characters.isEmpty());
    }

//    Function changed to Team Manager: +showCharactersInTeams

    public void displayCharacterInfo(String characterName){
        // TODO implement here
    }


}
