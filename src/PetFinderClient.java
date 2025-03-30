import java.io.FileInputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;
import org.json.JSONArray;

public class PetFinderClient {

//  API URL
    private final String BASE_URL = "https://api.petfinder.com/v2";

//  Initiate HTTPClient and accessToken
    private final HttpClient client;
    private String accessToken;

//  Constructor
    public PetFinderClient() {
        this.client = HttpClient.newHttpClient();
    }

//  Set the access token using OAUTH
    public void setAccessToken() throws IOException, InterruptedException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("external/petmatch.properties"));

        String authUrl = BASE_URL + "/oauth2/token";

        final String clientId = appProps.getProperty("clientId");
        final String clientSecret = appProps.getProperty("clientSecret");
        String requestBody = "grant_type=client_credentials" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(authUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject json = new JSONObject(response.body());
            accessToken = json.getString("access_token");
            System.out.println("Access token is set! :)");
        } else {
            System.out.println("Error setting access token: " + response.statusCode());
            System.out.println(response.body());
        }
    }

//  Search pets in the PetFinder API and return list of pet objects
    public List<Pet> searchPets(String uri) throws IOException, InterruptedException {
//      Create the endpoint using buildURI from UserPreferenceCollector
        String endpoint = BASE_URL + "/animals?" + uri + "&sort=distance";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//      Initialize array of pet objects
        List<Pet> petList = new ArrayList<>();

        if (response.statusCode() == 200) {
            JSONObject json = new JSONObject(response.body());
            JSONArray animals = json.getJSONArray("animals");
            for (Object obj : animals) {
//                System.out.println(animal);
                JSONObject animal = (JSONObject) obj;

                String name = animal.optString("name", "Unknown");
                String age = animal.optString("age", "Unknown");
                String gender = animal.optString("gender", "Unknown");
                String url = animal.optString("url", "N/A");

                JSONObject breeds = animal.optJSONObject("breeds");
                String breed = (breeds != null) ? breeds.optString("primary", "Unknown") : "Unknown";

                JSONObject contact = animal.optJSONObject("contact");
                JSONObject address = (contact != null) ? contact.optJSONObject("address") : null;
                String city = (address != null) ? address.optString("city", "Unknown") : "Unknown";
                String state = (address != null) ? address.optString("state", "Unknown") : "Unknown";
                double distance = animal.optDouble("distance", -1);
                String photoUrl = "N/A";
                JSONObject photoObj = animal.optJSONObject("primary_photo_cropped");
                if (photoObj != null) {
                    photoUrl = photoObj.optString("medium", "N/A");
                }

                Pet pet = new Pet(name, age, gender, breed, city, state, distance, url, photoUrl);
                petList.add(pet);
            }
            System.out.println("Found " + petList.size() + " adoptable animal(s).");
        } else {
            System.out.println("Error searching pets: " + response.statusCode());
            System.out.println(response.body());
        }
        // send result to pet class to build list of pet objects
        return petList;
    }

//  Use to get pet types available
//  Should only be used when checking for updates, not for end-user
    public void getTypes() throws IOException, InterruptedException {
        String endpoint = BASE_URL + "/types";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject json = new JSONObject(response.body());
            JSONArray animalTypes = json.getJSONArray("types");

            System.out.println(animalTypes);
        } else {
            System.out.println("Error searching pets: " + response.statusCode());
            System.out.println(response.body());
        }
    }

}
