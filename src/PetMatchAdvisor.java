import java.io.IOException;

import java.util.*;

public class PetMatchAdvisor {
    public static void main(String[] args) throws IllegalAccessException, IOException, InterruptedException {

        Map<String, String> userPreferences = UserNavigator.collectPreferences();

        UserNavigator.displayPreferences(userPreferences);

        String uri = UserNavigator.buildURI(userPreferences);

//      Instantiate the PetFinderClient for communicate with API
        PetFinderClient client = new PetFinderClient();
        client.setAccessToken();

        List<Pet> petList = client.searchPets(uri);

        List<Pet> favPetsList = UserNavigator.browsePetList(petList);

        if (favPetsList != null) {
            UserNavigator.displayFavoritePets(favPetsList);
        }
    }
}
