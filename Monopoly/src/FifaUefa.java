public class FifaUefa {

    private int id;
    public int price;
    private String name;

    FifaUefa(int id,int price, String name) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name of the federation: " + getName() + ", its id: " + getID();
    }
}