public class Pet {
    private final String name;
    private final String age;
    private final String gender;
    private final String breed;
    private final String city;
    private final String state;
    private final double distance;
    private final String url;
    private final String photoUrl;

    public Pet(String name, String age, String gender, String breed,
               String city, String state, double distance, String url, String photoUrl) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.breed = breed;
        this.city = city;
        this.state = state;
        this.distance = distance;
        this.url = url;
        this.photoUrl = photoUrl;
    }

    public void display() {
        System.out.println("   Name: " + name);
        System.out.println("   Age: " + age);
        System.out.println("   Gender: " + gender);
        System.out.println("   Breed: " + breed);
        System.out.println("   Location: " + city + ", " + state);
        System.out.println("   Distance: " + distance + " miles away");
        System.out.println("   Link: " + url);
        System.out.println("   Photo: " + photoUrl);
        System.out.println("----");
    }
}

