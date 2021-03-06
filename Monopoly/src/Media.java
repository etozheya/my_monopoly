public class Media {

    private int id;
    public int price;
    public int rent;
    private String name;

    Media(int id, int price, int rent, String name) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rent = rent;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name of the media: " + getName() + ", its id: " + getID();
    }
}
