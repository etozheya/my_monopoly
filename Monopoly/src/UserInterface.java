import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private Scanner reader;
    private Monopoly monopoly;
    private int noOfPlayers;


    public UserInterface() {
        reader = new Scanner(System.in);
        monopoly = new Monopoly();
    }

    public void start() {
        System.out.println("Welcome to the game of football monopoly! ");
        monopoly.players = addPlayers();
        System.out.println("Rules of the game: \n" +
                "The rules of the football monopoly are the same as of the original monopoly, \n" +
                "but it was made to be more interesting. Your are " + monopoly.players.size() + " billionaires, \n" +
                "who decided to compete in the world of football. Each one of you has the net worth of 1.5 billion dollars, \n" +
                "it should be enough for some time. You will roll two six-sided dices and move according to the number \n" +
                "on the dices. You will be able to buy properties, such as football clubs, football media, or shares in \n" +
                "football organizations such as FIFA and UEFA. When other players are landed on the property you possess, \n" +
                "you will get some rent from them. When you are in possession of all clubs from one league, \n" +
                "you will be able to buy player for the teams from that league, you will start with buying a goalkeeper \n" +
                "and finish with buying a forward. But don't forget about FFP, you can buy just one player in a round \n" +
                "During difficult times you will be able to sell the players, as much as you want in one round. \n" +
                "You will also have an opportunity to mortgage property to the bank for half of the price, no one \n" +
                "will have to pay fee when landing on mortgaged property, it can be bought out from the bank by you for \n" +
                "the same price. There is also a chance that you will get to prison. Don't worry, it is not going to be \n" +
                "long vacation, you will skip one round, and then you will be able to buy out for 50 million, or you \n" +
                "can try to hit double in order to escape it. The winner is the last person who is not bankrupt, as easy as that. \n" +
                "Good luck, everyone! ");
        for (int i = 0; i < 1000; i++) {
            monopoly.playRound(i);
        }
        System.out.print("You are playing for too long");
        for (Player player : monopoly.players) {
            System.out.print(", " + player.getName());
        }
        System.out.print(". Let's call it a draw");
    }

    public List<Player> addPlayers() {
        System.out.print("How many of you are going to play? ");
        while (true) {
            String s = reader.nextLine();
            int n = Integer.parseInt(s);
            if ((n > 1) && (n < 5)) {
                this.noOfPlayers = n;
                break;
            }
            System.out.println("Please enter valid number, only 2 to 4 players can play the game");
        }
        List<Player> players = new ArrayList<Player>();

        for (int in = 1; in <= noOfPlayers; in++) {
            System.out.print("Player number " + in + ", please enter your name: ");
            String s = reader.nextLine();
            players.add(new Player(s));
        }
        System.out.println("We will proceed to the game after you read the rules: ");
        return players;
    }

}
