import java.lang.reflect.Array;
import java.util.*;

public class Player {
    private String name;
    public int money;
    public int waitInPrison;
    private int doubleCubes;
    public ArrayList<Club> clubs;
    public ArrayList<FifaUefa> fifaUefas;
    public ArrayList<Media> medias;
    private int position;
    public int freeRelease;
    public ArrayList<Object> pledged;
    public int clubToPledge;
    public int mediaToPledge;
    public int fedToPledge;

    public int getWaitInPrison() {
        return this.waitInPrison;
    }

    public void printPlayersProperty() {
        if (this.clubs.isEmpty()) {
            System.out.println("You have got no clubs");
        } else {
            System.out.println("You have got following clubs:");
            for (Club club : this.clubs) {
                System.out.println(club.getID() + ": " + club.getName());
            }
        }
        if (this.medias.isEmpty()) {
            System.out.println("You have got no medias");
        } else {
            System.out.println("You have got following media:");
            for (Media media : this.medias) {
                System.out.println(media.getID() + ": " + media.getName());
            }
        }
        if (this.fifaUefas.isEmpty()) {
            System.out.println("You have got no federations");
        } else {
            System.out.println("You have got following federations:");
            for (FifaUefa fed : this.fifaUefas) {
                System.out.println(fed.getID() + ": " + fed.getName());
            }
        }
    }

    public void addClubToPledged(Club club) {
        pledged.add(club);
    }

    public void addMediaToPledged(Media media) {
        pledged.add(media);
    }

    public void addFedToPledged(FifaUefa fed) {
        pledged.add(fed);
    }

    public void printPledged() {
        System.out.println(getName() +", here is the list of property you pledged:");
        for (Object o : pledged) {
            System.out.println(o);
        }
    }

    public void buyPledged(int i) {
        int x = 0;
        int x1 = -1;
        int y = 0;
        int y1 = -1;
        int z = 0;
        int z1 = -1;
        for (Object o : pledged) {
            if (o instanceof Club) {
                if (((Club) o).getID() == i) {
                    x = i;
                    x1 = pledged.indexOf(o);
                }
            }
            if (o instanceof Media) {
                if (((Media) o).getID() == i) {
                    y = i;
                    y1 = pledged.indexOf(o);
                }
            }
            if (o instanceof FifaUefa) {
                if (((FifaUefa) o).getID() == i) {
                    z = i;
                    z1 = pledged.indexOf(o);
                }
            }
        }
        if (x > 0) {
            Club c = (Club) pledged.get(x1);
            clubs.add(c);
            increaseMoney(c.price/2);
            pledged.remove(x1);
        }
        if (y > 0) {
            Media m = (Media) pledged.get(y1);
            medias.add(m);
            increaseMoney(m.price/2);
            pledged.remove(y1);
        }
        if (z > 0) {
            FifaUefa f = (FifaUefa) pledged.get(z1);
            fifaUefas.add(f);
            increaseMoney(f.price/2);
            pledged.remove(z1);
        }
    }

    public void pledge(Player player, int i) {
        for (Club club : player.clubs) {
            if (club.getID() == i) {
                pledgeClub(player, i);
            }
        }
        if (player.clubToPledge > (-1)) {
            Club club = player.clubs.get(player.clubToPledge);
            addClubToPledged(club);
            player.increaseMoney(club.price/2);
            player.clubs.remove(player.clubToPledge);
        }
        player.clubToPledge = -1;
        for (Media media : player.medias) {
            if (media.getID() == i) {
                pledgeMedia(player, i);
            }
        }
        if (player.mediaToPledge > (-1)) {
            Media media = player.medias.get(player.mediaToPledge);
            addMediaToPledged(media);
            player.increaseMoney(media.price/2);
            player.medias.remove(player.mediaToPledge);
        }
        player.mediaToPledge = -1;
        for (FifaUefa fed : player.fifaUefas) {
            if (fed.getID() == i) {
                pledgeFed(player, i);
            }
        }
        if (player.fedToPledge > (-1)) {
            FifaUefa fifaUefa = player.fifaUefas.get(player.fedToPledge);
            addFedToPledged(fifaUefa);
            player.increaseMoney(fifaUefa.price/2);
            player.fifaUefas.remove(player.fedToPledge);
        }
        player.fedToPledge = -1;
    }

    public void pledgeFed(Player player, int i) {
        for (FifaUefa fed : player.fifaUefas) {
            if (fed.getID() == i) {
                player.fedToPledge = fifaUefas.indexOf(fed);
            }
        }
    }

    public void pledgeMedia(Player player, int i) {
        for (Media media : player.medias) {
            if (media.getID() == i) {
                player.mediaToPledge = medias.indexOf(media);
            }
        }
    }

    public void pledgeClub(Player player, int i) {
        for (Club club : player.clubs) {
            if (club.getID() == i) {
                player.clubToPledge = clubs.indexOf(club);
            }
        }
    }

    public void goTo(Player player, int position) {
        player.position = position;
    }

    public void isInPrison(Player player) {
        player.waitInPrison = player.waitInPrison + 1;
    }

    public int getMoney() {
        return this.money;
    }

    public void increaseMoney(int n) {
        this.money = this.money + n;
    }

    public void decreaseMoney(int n) {
        this.money = this.money - n;
    }

    public String showBalance() {
        return this.getName() + ", your balance is " + this.getMoney();
    }

    public int getPosition() {
        return this.position;
    }

    public void freeFromPrison() {
        this.waitInPrison = 0;
    }

    public void move(int n) {
        this.position = this.position + n;
    }

    public String getName() {
        return this.name;
    }

    public int getDoubleCubes() {
        return doubleCubes;
    }

    public void increaseDoubleCubes() {
        this.doubleCubes++;
    }

    public void zeroDoubleCubes() {
        this.doubleCubes = 0;
    }

    public Player(String name) {
        this.name = name;
        this.money = 1500;
        this.waitInPrison = 0;
        this.doubleCubes = 0;
        this.clubs = new ArrayList<Club>();
        this.medias = new ArrayList<Media>();
        this.fifaUefas = new ArrayList<FifaUefa>();
        this.clubToPledge = -1;
        this.mediaToPledge = -1;
        this.fedToPledge = -1;
        this.pledged = new ArrayList<>();
    }

}
