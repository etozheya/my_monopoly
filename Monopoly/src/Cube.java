import java.util.Random;

public class Cube {
    private int number;

    public Cube() {
        Random rand = new Random();
        this.number = rand.nextInt(6) + 1;
    }

    public int returnNumber() {
        return this.number;
    }
}
