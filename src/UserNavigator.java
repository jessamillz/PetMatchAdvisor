import java.util.*;

public class UserNavigator {

//  Map of animal types in the PetFinder API
    private static final Map<String, String> KNOWN_ANIMAL_TYPES = new LinkedHashMap<>();

    static {
        KNOWN_ANIMAL_TYPES.put("Cat", "cat");
        KNOWN_ANIMAL_TYPES.put("Dog", "dog");
        KNOWN_ANIMAL_TYPES.put("Rabbit", "rabbit");
        KNOWN_ANIMAL_TYPES.put("Small & Furry", "small-furry");
        KNOWN_ANIMAL_TYPES.put("Horse", "horse");
        KNOWN_ANIMAL_TYPES.put("Bird", "bird");
        KNOWN_ANIMAL_TYPES.put("Scales, Fins & Other", "scales-fins-other");
        KNOWN_ANIMAL_TYPES.put("Barnyard", "barnyard");
    }

//  Constructor

//  Collect the user preferences
    public static Map<String, String> collectPreferences() {

        System.out.println("🐶 Welcome to PetMatch Advisor!");

        //  Create map of preferences
        Map<String, String> preferences = new HashMap<>();

        System.out.println("\uD83D\uDC3E Let's get to know your preferences!");

        preferences.put("location", getString("What is your Zip Code?"));

        if (getBoolean("Do you have children (y/n)?")) {
            preferences.put("good_with_children", "true");
        }

        if (getBoolean("Do you have any cats or dogs (y/n)?")) {
            if (getBoolean("Do you have a cat(s) (y/n)?")) {
                preferences.put("good_with_cats", "true");
            }
            if (getBoolean("Do you have a dog(s) (y/n)?")) {
                preferences.put("good_with_dogs", "true");
            }
        }

        System.out.println("Please select the type of pet you are looking for:");
        List<String> displayNames = new ArrayList<>(KNOWN_ANIMAL_TYPES.keySet());
        for (int i = 0; i < displayNames.size(); i++) {
            System.out.println((i + 1) + ". " + displayNames.get(i));
        }
        int maxTries = 3;
        int counter = 0;

        while ( !preferences.containsKey("type") && counter < maxTries ) {
            int choice = (getInt("Choose your pet type number:"));

            if (choice >= 1 && choice <= KNOWN_ANIMAL_TYPES.size()) {
                String label = displayNames.get(choice - 1);
                String slug = KNOWN_ANIMAL_TYPES.get(label);
                preferences.put("type", slug);
            } else {
                System.out.println("Invalid choice. Please try again");
                counter++;
            }
        }
        if (!preferences.containsKey("type")) {
            System.out.println("Too many invalid attempts. Exiting program.");
            System.exit(0);
        }

/*      User input for future recommendation engine

        System.out.print("Do you prefer a low-maintenance pet (yes or no)? ");
        user.setPrefersLowMaintenance(scanner.nextLine().trim().equalsIgnoreCase("yes")) ;

        System.out.println("How active are you (low, medium, or high)? ");
        user.setActivityLevel(scanner.nextLine().trim().toLowerCase());
*/
        System.out.println("\uD83E\uDD98 User preferences saved!");

        return preferences;
    }

    public static String buildURI(Map<String, String> preferences) {
        StringBuilder uri = new StringBuilder();
        // build uri to populate query
        for (String i : preferences.keySet()) {
            if (!uri.isEmpty()) {
                uri.append("&");
            }
            uri.append(i).append("=").append(preferences.get(i));
        }
        return uri.toString();
    }

    //  Display the user's preferences using reflection
    public static void displayPreferences(Map<String, String> preferences) {
        System.out.println("Here are your preferences: ");
        System.out.println("----");
        for (String i : preferences.keySet()) {
            System.out.println(i + ": " + preferences.get(i));
        }
        System.out.println("----");
    }

    public static List<Pet> browsePetList(List<Pet> petList) {

        if (petList.isEmpty()) {
            System.out.println("There are no pets to display that fit your criteria.");
            return null;
        }

        System.out.println("Navigating through your potential companions \uD83D\uDE3B."); // 😻
        System.out.println("Showing pets closest to you.");
        System.out.println("----");

        List<Pet> favList = new ArrayList<>();
        for (Pet pet : petList) {
            System.out.println("You are viewing pet " + (petList.indexOf(pet)+1) + " out of " + petList.size());
            System.out.println("----");

            pet.display();

            if (getBoolean("Do you want to add this pet to your favorites (y/n)?")) {
                favList.add(pet);
            }
        }

        System.out.println("You saved " + favList.size() + " pets to your favorites!");
        System.out.println("----");
        return favList;
    }

    public static void displayFavoritePets(List<Pet> favList) {
        if (favList.isEmpty()) {
            System.out.println("You did not save any pets to your favorites.");
            return;
        }
        System.out.println("Here are your favorites:");
        System.out.println("----");

        int index = 0;
        while (index < favList.size()) {
            Pet pet = favList.get(index);
            pet.display();

            if (index < favList.size() - 1) {
                boolean continueViewing = getBoolean("Type (y) to view the next pet, or any other key to stop.");
                if (!continueViewing) break;
            }
            index ++;
        }

        System.out.println("Done viewing favorites.");

    }

    //  Input Helper
    public static Object getInput(String prompt, String type) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(prompt + " (or 'q' to quit): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Goodbye for now!");
                System.exit(0);
            }

            switch (type.toLowerCase()) {
                case "boolean":
                    if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) return true;
                    if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) return false;
                    System.out.println("Please enter 'y' or 'n'.");
                    break;

                case "string":
                    return input;

                case "int":
                    try {
                        return Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported input type: " + type);
            }
        }
    }

    //  Input helper wrappers for different types
   public static String getString(String prompt) {
        return (String) getInput(prompt, "string");
    }

    public static boolean getBoolean(String prompt) {
        return (Boolean) getInput(prompt, "boolean");
    }

    public static int getInt(String prompt) {
        return (Integer) getInput(prompt, "int");
    }
}
