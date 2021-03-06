import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Monopoly {
    public List<Player> players;
    private ArrayList<Media> nonboughtMedia;
    private ArrayList<FifaUefa> nonboughtFifaUefa;
    private Scanner reader;
    private int currentCube;
    private ArrayList<Club> nonboughtClubs;
    private int clubToDelete;
    private int mediaToDelete;
    private int fedToDelete;

    public Monopoly(){
        this.reader = new Scanner(System.in);
        this.nonboughtClubs = new ArrayList<Club>();
        addClubs(nonboughtClubs);
        this.nonboughtFifaUefa = new ArrayList<FifaUefa>();
        addFederations(nonboughtFifaUefa);
        this.nonboughtMedia = new ArrayList<Media>();
        addMedia(nonboughtMedia);
    }

    public void playRound(int round) {
        System.out.println("Round number " + (round + 1) + " begins");
        checkBalance();
        checkEndGame();
        for (Player player : players) {
            this.fedToDelete = -1;
            this.mediaToDelete = -1;
            this.clubToDelete = -1;
            player.zeroDoubleCubes();
            checkForPrison(player);
            if (player.getWaitInPrison() == 0) {
                whatToDo(player, askWhatToDo(player));
                if (player.getDoubleCubes() == 1) {
                    if (player.waitInPrison > 0) {
                        break;
                    }
                    System.out.println(player.getName() + ", you get to throw one more time");
                    makeAMove(player);
                }
                if (player.getDoubleCubes() == 2) {
                    System.out.println(player.getName() + ", you get to throw one more time, but be careful, next time you get double, you will be sent to prison");
                    throwOnce(player);
                    if (player.getDoubleCubes() == 3) {
                        System.out.println(player.getName() + ", you hit double three times, it is illegal, go to prison");
                        goToPrison(player);
                    } else {
                        move(player, currentCube);
                        checkIfWentThroughZero(player);
                        whereAreYou(player);
                    }
                }
            }
            if (this.clubToDelete > (-1)) {
                nonboughtClubs.remove(this.clubToDelete);
            }
            if (this.mediaToDelete > (-1)) {
                nonboughtMedia.remove(this.mediaToDelete);
            }
            if (this.fedToDelete > (-1)) {
                nonboughtFifaUefa.remove(this.fedToDelete);
            }
            player.showBalance();
        }
        System.out.println("Round number " + (round + 1) + " has finished");
        for (Player player : players) {
            System.out.println(player.getName() + ", your balance is " + player.showBalance());
        }
        System.out.println("---------------------------------");
    }

    public void makeAMove(Player player) {
        throwOnce(player);
        move(player, currentCube);
        checkIfWentThroughZero(player);
        whereAreYou(player);
    }

    public int askWhatToDo(Player player) {
        System.out.println(player.getName()+", it is your turn, you are on the field " + player.getPosition() + ". Your " +
                "balance is: " +  player.getMoney());
        System.out.println("What do you want to do now? Press 1 to roll the dices. Press 2 to see available property. Press 3 to see your property. \n" +
                "Press 4 to offer transfer to someone. Press 5 to buy players for your clubs. Press 6 to sell players. Press 7 to mortgage property. \n" +
                "Press 8 to buy mortgaged property from the bank. Press 9 to give up and leave the game");
        String s = reader.nextLine();
        return Integer.parseInt(s);
    }

    public void whatToDo(Player player, int i) {
        switch (i) {
            case 1 :
                makeAMove(player);
                break;
            case 2 :
                printNonBought();
                whatToDo(player, askWhatToDo(player));
                break;
            case 3 :
                player.printPlayersProperty();
                whatToDo(player, askWhatToDo(player));
                break;
            case 4 :
                transfer(player);
                whatToDo(player, askWhatToDo(player));
                break;
            case 5 :
                System.out.println("You can buy only one player in this round");
                checkForLeagues(player);
                whatToDo(player, askWhatToDo(player));
                break;
            case 6 :
                sellSomeone(player);
                whatToDo(player, askWhatToDo(player));
                break;
            case 7 :
                player.printPlayersProperty();
                int x = askToPledge(player);
                player.pledge(player, x);
                whatToDo(player, askWhatToDo(player));
                break;
            case 8 :
                player.printPledged();
                int z = askToBuyPledged(player);
                player.buyPledged(z);
                whatToDo(player, askWhatToDo(player));
                break;
            case 9 :
                System.out.println(player.getName() + ", thank you for the game");
                nonboughtClubs.addAll(player.clubs);
                nonboughtMedia.addAll(player.medias);
                nonboughtFifaUefa.addAll(player.fifaUefas);
                player.decreaseMoney(20000);
                break;
            default:
                System.out.println(player.getName() + ", please enter valid number");
                whatToDo(player, askWhatToDo(player));
                break;
        }
    }

    public void auctionClub(Club club) {
        System.out.println("We have an auction for " + club.getName());
        int x = 0;
        int max = 0;
        String maxPlayer = "";
        while (x == 0) {
            System.out.println("Please make your offers");
            for (Player player : players) {
                System.out.println(player.getName() + ", how much do you offer?");
                int z = Integer.parseInt(reader.nextLine());
                if (z > max) {
                    max = z;
                    maxPlayer = player.getName();
                }
            }
            System.out.println("Maximum offer is " + max + " from " + maxPlayer + ", anyone wants to beat it? (Type 'yes' if so)");
            String a = reader.nextLine();
            if (!(a.equals("yes"))) {
                x = 1;
                for (Player player : players) {
                    if (player.getName().equals(maxPlayer)) {
                        player.decreaseMoney(max);
                        System.out.println(player.getName() + ", you have bought " + club.getName());
                        player.clubs.add(club);
                        int i = 0;
                        for (Club club1 : this.nonboughtClubs) {
                            if (club.getID() == club1.getID()) {
                                this.clubToDelete = nonboughtClubs.indexOf(club1);
                            }
                        }
                    }
                }
            }
        }
    }

    public void auctionMedia(Media media) {
        System.out.println("We have an auction for " + media.getName());
        int x = 0;
        int max = 0;
        String maxPlayer = "";
        while (x == 0) {
            System.out.println("Please make your offers");
            for (Player player : players) {
                System.out.println(player.getName() + ", how much do you offer?");
                int z = Integer.parseInt(reader.nextLine());
                if (z > max) {
                    max = z;
                    maxPlayer = player.getName();
                }
            }
            System.out.println("Maximum offer is " + max + " from " + maxPlayer + ", anyone wants to beat it? (Type 'yes' if so)");
            String a = reader.nextLine();
            if (!(a.equals("yes"))) {
                x = 1;
                for (Player player : players) {
                    if (player.getName().equals(maxPlayer)) {
                        player.decreaseMoney(max);
                        System.out.println(player.getName() + ", you have bought " + media.getName());
                        player.medias.add(media);
                        int i = 0;
                        for (Media media1 : this.nonboughtMedia) {
                            if (media.getID() == media1.getID()) {
                                this.mediaToDelete = nonboughtMedia.indexOf(media1);
                            }
                        }
                    }
                }
            }
        }
    }

    public void auctionFed(FifaUefa fed) {
        System.out.println("We have an auction for " + fed.getName());
        int x = 0;
        int max = 0;
        String maxPlayer = "";
        while (x == 0) {
            System.out.println("Please make your offers");
            for (Player player : players) {
                System.out.println(player.getName() + ", how much do you offer?");
                int z = Integer.parseInt(reader.nextLine());
                if (z > max) {
                    max = z;
                    maxPlayer = player.getName();
                }
            }
            System.out.println("Max offer is " + max + " from " + maxPlayer + ", anyone wants to beat it? (Type 'yes' if so)");
            String a = reader.nextLine();
            if (!(a.equals("yes"))) {
                x = 1;
                for (Player player : players) {
                    if (player.getName().equals(maxPlayer)) {
                        player.decreaseMoney(max);
                        System.out.println(player.getName() + ", you have bought " + fed.getName());
                        player.fifaUefas.add(fed);
                        int i = 0;
                        for (FifaUefa fed1 : this.nonboughtFifaUefa) {
                            if (fed.getID() == fed1.getID()) {
                                this.fedToDelete = nonboughtFifaUefa.indexOf(fed1);
                            }
                        }
                    }
                }
            }
        }
    }

    public int askToBuyPledged(Player player) {
        System.out.println(player.getName() + ", which mortgaged property you want to buy? (Type it's ID)");
        int i = Integer.parseInt(reader.nextLine());
        return i;
    }

    public int askToPledge(Player player) {
        System.out.println(player.getName() + ", which property you want to mortgage? (Type it's ID)");
        int i = Integer.parseInt(reader.nextLine());
        for (Club club : player.clubs) {
            if (club.getID() == i) {
                if (!(club.currentRent == club.rent)) {
                    return 0;
                }
            }
        }
        return i;
    }

    public void sellSomeone(Player player) {
        System.out.println(player.getName() + ", you can sell players from the following clubs:");
        for (Club club : player.clubs) {
            if (!(club.currentRent == club.rent)) {
                System.out.println(club.getID() + ": " + club.getName());
            }
        }
        System.out.println(player.getName() + ", player from which club you want to sale? (Type it's ID)");
        int i = Integer.parseInt(reader.nextLine());
        for (Club club : player.clubs) {
            if (club.getID() == i) {
                club.sellPlayer();
                player.decreaseMoney(club.playerPrice);
            }
        }
    }

    public void transfer(Player player) {
        System.out.println("You can sell only clubs with no players in it");
        System.out.println(player.getName() + ", which opponent do you want to offer? (Type his name)");
        String s = reader.nextLine();
        player.printPlayersProperty();
        for (Player player1 : players) {
            if (player1.getName().equals(s)) {
                System.out.println(player.getName() + ", how much money do you offer? (write negative amount to ask for money)");
                int i = Integer.parseInt(reader.nextLine());
                System.out.println(player.getName() + ", what property do you offer? (Type club, media, or fed)");
                String type = reader.nextLine();
                int k = 0;
                int l = 0;
                int m = 0;
                if (type.equals("club")) {
                    System.out.println("Please choose which club you want to offer (Type it's ID): ");
                    k = Integer.parseInt(reader.nextLine());
                }
                if (type.equals("media")) {
                    System.out.println("Please choose which media you want to offer (Type it's ID): ");
                    l = Integer.parseInt(reader.nextLine());
                }
                if (type.equals("fed")) {
                    System.out.println("Please choose which federation you want to offer (Type it's ID): ");
                    m = Integer.parseInt(reader.nextLine());
                }
                System.out.println("Here is the property of " + player1.getName());
                player1.printPlayersProperty();
                System.out.println(player.getName() + ", what property do you want to get? (club, media, or fed)");
                String typ = reader.nextLine();
                int q = 0;
                int w = 0;
                int e = 0;
                if (typ.equals("club")) {
                    System.out.println("Please choose which club you want (Type it's ID): ");
                    q = Integer.parseInt(reader.nextLine());
                }
                if (typ.equals("media")) {
                    System.out.println("Please choose which media you want (Type it's ID): ");
                    w = Integer.parseInt(reader.nextLine());
                }
                if (typ.equals("fed")) {
                    System.out.println("Please choose which federation you want (Type it's ID): ");
                    e = Integer.parseInt(reader.nextLine());
                }
                System.out.println(player1.getName() + ", do you agree (Type 'yes' if you agree)");
                String ans = reader.nextLine();
                if (ans.equals("yes")) {
                    player.increaseMoney(i);
                    player1.decreaseMoney(i);
                    int p = -1;
                    int o = -1;
                    if (k > 0) {
                        if (q > 0) {
                            for (Club club : player.clubs) {
                                if (club.getID() == k) {
                                    p = player.clubs.indexOf(club);
                                    player1.clubs.add(club);
                                }
                            }
                            for (Club club : player1.clubs) {
                                if (club.getID() == q) {
                                    o = player1.clubs.indexOf(club);
                                    player.clubs.add(club);
                                }
                            }
                            player1.clubs.remove(o);
                            player.clubs.remove(p);
                        }
                        if (w > 0) {
                            for (Club club : player.clubs) {
                                if (club.getID() == k) {
                                    p = player.clubs.indexOf(club);
                                    player1.clubs.add(club);
                                }
                            }
                            for (Media media : player1.medias) {
                                if (media.getID() == w) {
                                    o = player1.medias.indexOf(media);
                                    player.medias.add(media);
                                }
                            }
                            player.clubs.remove(p);
                            player1.medias.remove(o);
                        }
                        if (e > 0) {
                            for (Club club : player.clubs) {
                                if (club.getID() == k) {
                                    p = player.clubs.indexOf(club);
                                    player1.clubs.add(club);
                                }
                            }
                            for (FifaUefa fed : player1.fifaUefas) {
                                if (fed.getID() == e) {
                                    o = player1.fifaUefas.indexOf(fed);
                                    player.fifaUefas.add(fed);
                                }
                            }
                            player.clubs.remove(p);
                            player1.fifaUefas.remove(o);
                        }
                    }
                    if (l > 0) {
                        if (q > 0) {
                            for (Media media : player.medias) {
                                if (media.getID() == l) {
                                    p = player.medias.indexOf(media);
                                    player1.medias.add(media);
                                }
                            }
                            for (Club club : player1.clubs) {
                                if (club.getID() == q) {
                                    o = player1.clubs.indexOf(club);
                                    player.clubs.add(club);
                                }
                            }
                            player.medias.remove(p);
                            player1.clubs.remove(o);
                        }
                        if (w > 0) {
                            for (Media media : player.medias) {
                                if (media.getID() == l) {
                                    p = player.medias.indexOf(media);
                                    player1.medias.add(media);
                                }
                            }
                            for (Media media : player1.medias) {
                                if (media.getID() == w) {
                                    o = player1.medias.indexOf(media);
                                    player.medias.add(media);
                                }
                            }
                            player.medias.remove(p);
                            player1.medias.remove(o);
                        }
                        if (e > 0) {
                            for (Media media : player.medias) {
                                if (media.getID() == l) {
                                    p = player.medias.indexOf(media);
                                    player1.medias.add(media);
                                }
                            }
                            for (FifaUefa fed : player1.fifaUefas) {
                                if (fed.getID() == e) {
                                    o = player1.fifaUefas.indexOf(fed);
                                    player.fifaUefas.add(fed);
                                }
                            }
                            player.medias.remove(p);
                            player1.fifaUefas.remove(o);
                        }
                    }
                    if (m > 0) {
                        if (q > 0) {
                            for (FifaUefa fed : player.fifaUefas) {
                                if (fed.getID() == m) {
                                    p = player.fifaUefas.indexOf(fed);
                                    player1.fifaUefas.add(fed);
                                }
                            }
                            for (Club club : player1.clubs) {
                                if (club.getID() == q) {
                                    o = player1.clubs.indexOf(club);
                                    player.clubs.add(club);
                                }
                            }
                            player.fifaUefas.remove(p);
                            player1.clubs.remove(o);
                        }
                        if (w > 0) {
                            for (FifaUefa fed : player.fifaUefas) {
                                if (fed.getID() == m) {
                                    p = player.fifaUefas.indexOf(fed);
                                    player1.fifaUefas.add(fed);
                                }
                            }
                            for (Media media : player1.medias) {
                                if (media.getID() == w) {
                                    o = player1.medias.indexOf(media);
                                    player.medias.add(media);
                                }
                            }
                            player.fifaUefas.remove(p);
                            player1.medias.remove(o);
                        }
                        if (e > 0) {
                            for (FifaUefa fed : player.fifaUefas) {
                                if (fed.getID() == m) {
                                    p = player.fifaUefas.indexOf(fed);
                                    player1.fifaUefas.add(fed);
                                }
                            }
                            for (FifaUefa fed : player1.fifaUefas) {
                                if (fed.getID() == e) {
                                    o = player1.fifaUefas.indexOf(fed);
                                    player.fifaUefas.add(fed);
                                }
                            }
                            player.fifaUefas.remove(p);
                            player1.fifaUefas.remove(o);
                        }
                    }
                }
            }
        }
    }

    public void checkForLeagues(Player player) {
        List<Integer> availableToBuy = new ArrayList<Integer>();
        if (checkForEredivise(player)) {
            availableToBuy.add(1);
            availableToBuy.add(3);
        }
        if (checkForNOS(player)) {
            availableToBuy.add(6);
            availableToBuy.add(8);
            availableToBuy.add(9);
        }
        if (checkForLigue1(player)) {
            availableToBuy.add(11);
            availableToBuy.add(13);
            availableToBuy.add(14);
        }
        if (checkForBundesliga(player)) {
            availableToBuy.add(16);
            availableToBuy.add(18);
            availableToBuy.add(19);
        }
        if (checkForSerieA(player)) {
            availableToBuy.add(21);
            availableToBuy.add(23);
            availableToBuy.add(24);
        }
        if (checkForLaLiga(player)) {
            availableToBuy.add(26);
            availableToBuy.add(27);
            availableToBuy.add(29);
        }
        if (checkForEPL(player)) {
            availableToBuy.add(31);
            availableToBuy.add(32);
            availableToBuy.add(34);
        }
        if (checkForRPL(player)) {
            availableToBuy.add(37);
            availableToBuy.add(39);
        }
        if (!availableToBuy.isEmpty()) {
            System.out.println(player.getName() + ", you can buy player for following clubs:");
            for (Integer i : availableToBuy) {
                for (Club club : player.clubs) {
                    if (club.getID() == i) {
                        System.out.println(club.getID() + ": " + club.getName());
                    }
                }
            }
            System.out.println(player.getName() + ", which club you would like to strengthen? (Type it's ID)");
            String s = reader.nextLine();
            int i = Integer.parseInt(s);
            for (Club club : player.clubs) {
                if (club.getID() == i) {
                    if (!(club.currentRent == club.rentFW)) {
                        club.buyPlayer();
                        player.decreaseMoney(club.playerPrice);
                    }
                }
            }
        } else {
            System.out.println(player.getName() + ", you can not buy players now");
        }
    }

    public void whereAreYou(Player player) {
        int i = player.getPosition();
        System.out.println("You arrived at the field number " + i);
        switch (i) {
            case 0:
                break;
            case 1:
                isOnClubField(player, 1);
                break;
            case 2:
                chest(player);
                break;
            case 3:
                isOnClubField(player, 3);
                break;
            case 4:
                if (player.getMoney() > 2000) {
                    System.out.println(player.getName() + ", you have to pay 200 as taxes");
                    player.decreaseMoney(200);
                } else {
                    System.out.println(player.getName() + ", you have to pay 10% of your money as taxes, which is " + (player.getMoney()/10));
                    player.decreaseMoney((player.getMoney()/10));
                }
                break;
            case 5:
                isOnMediaField(player, 5);
                break;
            case 6:
                isOnClubField(player, 6);
                break;
            case 7:
                chance(player);
                break;
            case 8:
                isOnClubField(player, 8);
                break;
            case 9:
                isOnClubField(player, 9);
                break;
            case 10:
                System.out.println("You are just visiting prison");
                break;
            case 11:
                isOnClubField(player, 11);
                break;
            case 12:
                isOnFedField(player, 12);
                break;
            case 13:
                isOnClubField(player, 13);
                break;
            case 14:
                isOnClubField(player, 14);
                break;
            case 15:
                isOnMediaField(player, 15);
                break;
            case 16:
                isOnClubField(player, 16);
                break;
            case 17:
                chest(player);
                break;
            case 18:
                isOnClubField(player, 18);
                break;
            case 19:
                isOnClubField(player, 19);
                break;
            case 20:
                System.out.println("You are on free parking, chill");
                break;
            case 21:
                isOnClubField(player, 21);
                break;
            case 22:
                chance(player);
                break;
            case 23:
                isOnClubField(player, 23);
                break;
            case 24:
                isOnClubField(player, 24);
                break;
            case 25:
                isOnMediaField(player, 25);
                break;
            case 26:
                isOnClubField(player, 26);
                break;
            case 27:
                isOnClubField(player, 27);
                break;
            case 28:
                isOnFedField(player,28);
                break;
            case 29:
                isOnClubField(player,29);
                break;
            case 30:
                System.out.println(player.getName() + ", you are arrested, you skip next round");
                goToPrison(player);
                break;
            case 31:
                isOnClubField(player, 31);
                break;
            case 32:
                isOnClubField(player, 32);
                break;
            case 33:
                chest(player);
                break;
            case 34:
                isOnClubField(player, 34);
                break;
            case 35:
                isOnMediaField(player, 35);
                break;
            case 36:
                chance(player);
                break;
            case 37:
                isOnClubField(player, 37);
                break;
            case 38:
                System.out.println("You are rich, why don't you go buy yourself a boat for a 100");
                player.decreaseMoney(100);
                break;
            case 39:
                isOnClubField(player, 39);
                break;
        }
    }

    public void checkIfWentThroughZero (Player player) {
        if (player.getPosition() > 39) {
            player.move(-40);
            player.increaseMoney(200);
            System.out.println(player.getName() + ", you start new season, get extra 200");
            player.showBalance();
        }
    }

    public void move(Player player, int number) {
        player.move(number); //moves player by some number of fields
    }

    public void goToPrison(Player player) {
        player.goTo(player, 10); //moves to prison
        player.isInPrison(player);//tells that we are in prison and skip next round
    }

    public void throwOnce (Player player) {
        System.out.println(player.getName() + ", you are rolling the dices");
        Cube cube1 = new Cube();
        Cube cube2 = new Cube();
        int i1 = cube1.returnNumber();
        int i2 = cube2.returnNumber();
        System.out.println(player.getName() + ", the dices show following numbers: " + i1 + ", and " + i2);
        if (i1 == i2) {
            System.out.println(player.getName() + ", you got a double");
            player.increaseDoubleCubes();
        }
        currentCube = i1+i2;
    }

    public void checkForPrison(Player player) {
        switch (player.getWaitInPrison()) {
            case 1:
                System.out.println(player.getName() + ", you are in prison, you have to skip this round");
                player.isInPrison(player);
                break;
            case 2:
            case 3:
                System.out.println(player.getName() + ", you are in prison, but you can try hit double or buy out. Press 1 if you want" +
                        " to try double. Press 2 if you want to buy out");
                String sas = reader.nextLine();
                if (sas.equals("1")) {
                    throwOnce(player);
                    if (player.getDoubleCubes() == 1) {
                        System.out.println(player.getName() + ", congrats you hit double and you are out of prison");
                        player.freeFromPrison();
                        player.waitInPrison = 0;
                        move(player, currentCube);
                        checkIfWentThroughZero(player);
                        whereAreYou(player);
                    } else {
                        System.out.println(player.getName() + ", you have to stay in prison");
                        player.isInPrison(player);
                    }
                }
                if (sas.equals("2")) {
                    if (player.freeRelease > 0) {
                        player.freeRelease = player.freeRelease - 1;
                    } else {
                        player.decreaseMoney(50);
                    }
                    player.freeFromPrison();
                }
                break;
            case 4:
                System.out.println(player.getName() + ", you are out of prison");
                player.freeFromPrison();
                player.decreaseMoney(50);
                break;
        }
    }

    public void isOnMediaField(Player player, int i) {
        int x = 0;
        for (Player player1 : players) {
            for (Media media : player1.medias) {
                if (media.getID() == i) {
                    if (player.getName() == player1.getName()) {
                        System.out.println(player.getName() + ", it is your media, you don't have to pay rent");
                    } else {
                        System.out.println("You are on " + media.getName() + " field");
                        System.out.println("This media is owned by " + player1.getName());
                        if (player1.medias.size() == 1) {
                            System.out.println(player.getName() + ", you have to pay " + player1.getName() + " some rent, which is " + media.rent);
                            player.decreaseMoney(media.rent);
                            player1.increaseMoney(media.rent);
                        }
                        if (player1.medias.size() == 2) {
                            System.out.println(player.getName() + ", you have to pay " + player1.getName() + " some rent, which is " + (2*media.rent));
                            player.decreaseMoney(2*media.rent);
                            player1.increaseMoney(2*media.rent);
                        }
                        if (player1.medias.size() == 3) {
                            System.out.println(player.getName() + ", you have to pay " + player1.getName() + " some rent, which is " + (4*media.rent));
                            player.decreaseMoney(4*media.rent);
                            player1.increaseMoney(4*media.rent);
                        }
                        if (player1.medias.size() == 4) {
                            System.out.println(player.getName() + ", you have to pay " + player1.getName() + " some rent, which is " + (8*media.rent));
                            player.decreaseMoney(8*media.rent);
                            player1.increaseMoney(8*media.rent);
                        }
                    }
                    x++;
                }
            }
        }
        if (x == 0) {
            for (Media media : nonboughtMedia) {
                if (media.getID() == i) {
                    System.out.println("You are on " + media.getName() + " field");
                    System.out.println(player.getName() + ", your balance is " + player.getMoney());
                    System.out.println("This media is available to buy. It's price is " + media.price +
                            ". Press 1 to buy it. Press 2 to decline and start an auction");
                    String s = reader.nextLine();
                    if (s.equals("1")) {
                        this.mediaToDelete = buyMedia(player, media);
                        System.out.println(player.getName() + ", your balance is " + player.getMoney());
                    } else {
                        auctionMedia(media);
                        break;
                    }
                }
            }
        }
    }

    public void isOnClubField(Player player, int i) {
        int x = 0;
        for (Player player1 : players) {
            for (Club playerclub : player1.clubs) {
                if (playerclub.getID() == i) {
                    if (player.getName() == player1.getName()) {
                        System.out.println(player.getName() + ", it is your club, you don't have to pay rent");
                    } else {
                        System.out.println("You are on " + playerclub.getName() + " field");
                        System.out.println("This club is owned by " + player1.getName());
                        System.out.println(player.getName() + ", you have to pay " + player1.getName() + " some rent, which is " + playerclub.currentRent);
                        player.decreaseMoney(playerclub.currentRent);
                        player1.increaseMoney(playerclub.currentRent);
                    }
                    x++;
                }
            }
        }
        if (x == 0) {
            for (Club club : nonboughtClubs) {
                if (club.getID() == i) {
                    System.out.println("You are on " + club.getName() + " field");
                    System.out.println(player.getName() + ", your balance is " + player.getMoney());
                    System.out.println("This club is available to buy. It's price is " + club.price + ". " +
                            "Press 1 to buy it. Press 2 to decline and start an auction");
                    String s = reader.nextLine();
                    if (s.equals("1")) {
                        this.clubToDelete = buyClub(player, club);
                        System.out.println(player.getName() + ", your balance is " + player.getMoney());
                    } else {
                        auctionClub(club);
                        break;
                    }
                }
            }
        }
    }

    public void isOnFedField(Player player, int i) {
        int x = 0;
        for (Player player1 : players) {
            for (FifaUefa fed : player1.fifaUefas) {
                if (fed.getID() == i) {
                    if (player.getName() == player1.getName()) {
                        System.out.println(player.getName() + ", it is your federation, you don't have to pay rent");
                    } else {
                        System.out.println("You are on " + fed.getName() + " field");
                        System.out.println("This federation is owned by " + player1.getName());
                        if (player1.fifaUefas.size() == 1) {
                            System.out.println(player.getName() + ", you have to pay " + player1.getName() + " some rent, which is " + (currentCube * 4));
                            player.decreaseMoney(currentCube*4);
                            player1.increaseMoney(currentCube*4);
                        }
                        if (player1.fifaUefas.size() == 2) {
                            System.out.println(player.getName() + ", you have to pay " + player1.getName() + " some rent, which is " + (currentCube * 10));
                            player.decreaseMoney(currentCube*10);
                            player1.increaseMoney(currentCube*10);
                        }
                    }
                    x++;
                }
            }
        }
        if (x == 0) {
            for (FifaUefa fed : nonboughtFifaUefa) {
                if (fed.getID() == i) {
                    System.out.println("You are on " + fed.getName() + " field");
                    System.out.println(player.getName() + ", your balance is " + player.getMoney());
                    System.out.println("This federation is available to buy. Its price is " + fed.price + "." +
                            " Press 1 to buy it. Press 2 to decline and start an auction");
                    String s = reader.nextLine();
                    if (s.equals("1")) {
                        this.fedToDelete = buyFed(player, fed);
                        System.out.println(player.getName() + ", your balance is " + player.getMoney());
                    } else {
                        auctionFed(fed);
                        break;
                    }
                }
            }
        }
    }

    public void checkBalance() {
        int i = -1;
        for (Player player : players) {
            if (player.getMoney() < 0) {
                System.out.println(player.getName() + ", you don't have money, you lose the game");
                i = players.indexOf(player);
            }
        }
        if (i > (-1)) {
            deletePlayer(i, players);
        }
    }

    public boolean checkForEredivise(Player player) {
        int i = 0;
        for (Club club : player.clubs) {
            if (club.getID() == 1) {
                i++;
            }
            if (club.getID() == 3) {
                i++;
            }
        }
        if (i == 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForNOS(Player player) {
        int i = 0;
        for (Club club : player.clubs) {
            if (club.getID() == 6) {
                i++;
            }
            if (club.getID() == 8) {
                i++;
            }
            if (club.getID() == 9) {
                i++;
            }
        }
        if (i == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForLigue1(Player player) {
        int i = 0;
        for (Club club : player.clubs) {
            if (club.getID() == 11) {
                i++;
            }
            if (club.getID() == 13) {
                i++;
            }
            if (club.getID() == 14) {
                i++;
            }
        }
        if (i == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForBundesliga(Player player) {
        int i = 0;
        for (Club club : player.clubs) {
            if (club.getID() == 16) {
                i++;
            }
            if (club.getID() == 18) {
                i++;
            }
            if (club.getID() == 19) {
                i++;
            }
        }
        if (i == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForSerieA(Player player) {
        int i = 0;
        for (Club club : player.clubs) {
            if (club.getID() == 21) {
                i++;
            }
            if (club.getID() == 23) {
                i++;
            }
            if (club.getID() == 24) {
                i++;
            }
        }
        if (i == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForLaLiga(Player player) {
        int i = 0;
        for (Club club : player.clubs) {
            if (club.getID() == 26) {
                i++;
            }
            if (club.getID() == 27) {
                i++;
            }
            if (club.getID() == 29) {
                i++;
            }
        }
        if (i == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForEPL(Player player) {
        int i = 0;
        for (Club club : player.clubs) {
            if (club.getID() == 31) {
                i++;
            }
            if (club.getID() == 32) {
                i++;
            }
            if (club.getID() == 34) {
                i++;
            }
        }
        if (i == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForRPL(Player player) {
        int i = 0;
        for (Club club : player.clubs) {
            if (club.getID() == 37) {
                i++;
            }
            if (club.getID() == 39) {
                i++;
            }
        }
        if (i == 2) {
            return true;
        } else {
            return false;
        }
    }

    public void deletePlayer(int player, List<Player> players) {
        players.remove(player);
    }

    public void checkEndGame() {
        if (players.size() == 1) {
            endGame();
        }
    }

    public void endGame() {
        System.out.println(players.get(0).getName() + ", congratulations, you win the game!");
        System.exit(1);
    }

    public int buyClub(Player player, Club club) {
        System.out.println(player.getName() + ", you have bought a " + club.getName());
        player.decreaseMoney(club.price);
        player.clubs.add(club);
        int i = 0;
        for (Club club1 : this.nonboughtClubs) {
            if (club.getID() == club1.getID()) {
                i = nonboughtClubs.indexOf(club1);
            }
        }
        return i;
    }

    public int buyMedia(Player player, Media media) {
        System.out.println(player.getName() + ", you have bought a " + media.getName());
        player.decreaseMoney(media.price);
        player.medias.add(media);
        int i = 0;
        for (Media media1 : this.nonboughtMedia) {
            if (media.getID() == media1.getID()) {
                i = nonboughtMedia.indexOf(media1);
            }
        }
        return i;
    }

    public int buyFed(Player player, FifaUefa fed) {
        System.out.println(player.getName() + ", you have bought a " + fed.getName());
        player.decreaseMoney(fed.price);
        player.fifaUefas.add(fed);
        int i = 0;
        for (FifaUefa fed1 : this.nonboughtFifaUefa) {
            if (fed.getID() == fed1.getID()) {
                i = nonboughtFifaUefa.indexOf(fed1);
            }
        }
        return i;
    }

    public void chest(Player player) {
        System.out.println(player.getName() + ", you have got an email from your accountant, please check it");
        Random rand = new Random();
        int i = rand.nextInt(15) + 1;
        switch (i) {
            case 1:
                System.out.println(player.getName() + ", one of your clubs have won the Champions League, get 200 of prize");
                player.increaseMoney(200);
                break;
            case 2:
                System.out.println(player.getName() + ", one of your clubs have won the Europa League, get 100 of prize");
                player.increaseMoney(100);
                break;
            case 3:
                System.out.println(player.getName() + ", you sold your useless player to Arsenal, get 10 of transfer fee");
                player.increaseMoney(10);
                break;
            case 4:
                System.out.println(player.getName() + ", one of your clubs have won championship, get 100 of prize");
                player.increaseMoney(100);
                break;
            case 5:
            case 13:
            case 6:
                System.out.println(player.getName() + ", you signed a new sponsor, get 25");
                player.increaseMoney(25);
                break;
            case 7:
                System.out.println(player.getName() + ", one of your clubs went to china to play some matches, get 50 of marketing income");
                player.increaseMoney(50);
                break;
            case 8:
                System.out.println(player.getName() + ", scout of one of your clubs stole young players from your opponents' clubs, get 10 from each of them");
                for (Player player1 : players) {
                    if (!(player == player1)) {
                        player.increaseMoney(10);
                        player1.decreaseMoney(10);
                    }
                }
                break;
            case 9:
                System.out.println(player.getName() + ", you get one free release");
                player.freeRelease = player.freeRelease + 1;
                break;
            case 10:
                System.out.println(player.getName() + ", you've been arrested for FFP");
                goToPrison(player);
                break;
            case 11:
                System.out.println(player.getName() + ", one of your clubs has lost in Champions League qualifying, lose 100");
                player.decreaseMoney(100);
                break;
            case 12:
                chance(player);
                break;
            case 14:
                System.out.println(player.getName() + ", you paid to referee for a penalty in an important game, too bad your player didn't score it, lose 50");
                player.decreaseMoney(50);
                break;
            case 15:
                System.out.println(player.getName() + ", new season begins, go to square number 0, also get 200 for it");
                player.goTo(player, 0);
                player.increaseMoney(200);
                break;
        }
    }

    public void chance(Player player) {
        System.out.println(player.getName() + ", you get a chance:");
        Random rand = new Random();
        int i = rand.nextInt(16) + 1;
        switch (i) {
            case 1:
                System.out.println(player.getName() + ", go to Spartak field");
                player.goTo(player, 39);
                whereAreYou(player);
                break;
            case 2:
                System.out.println(player.getName() + ", go to Juventus field");
                if (player.getPosition() > 24) {
                    System.out.println("You start the new season, get 200");
                    player.increaseMoney(200);
                }
                player.goTo(player, 24);
                whereAreYou(player);
                break;
            case 3:
                System.out.println(player.getName() + ", you have to pay taxes, 25 per each player, 100 per each forward");
                int x = 0;
                int z = 0;
                for (Club club : player.clubs) {
                    x = x + club.boughtPlayers;
                    z = z + club.boughtForwards;
                }
                player.decreaseMoney((z*100) + (x*25));
                System.out.println(player.getName() + ", in total you lose " + (z + x));
                break;
            case 4:
                System.out.println(player.getName() + ", pay 150 for stadium expansion");
                player.decreaseMoney(150);
                break;
            case 5:
                System.out.println(player.getName() + ", new season begins, you get 200, go to field 0");
                player.goTo(player, 0);
                player.increaseMoney(200);
                break;
            case 6:
                System.out.println(player.getName() + ", one of your sponsors left you, lose 15");
                player.decreaseMoney(15);
                break;
            case 7:
                System.out.println(player.getName() + ", go to Marca field");
                if (player.getPosition() > 15) {
                    System.out.println("You start the new season, get 200");
                    player.increaseMoney(200);
                }
                player.goTo(player, 15);
                whereAreYou(player);
                break;
            case 8:
                System.out.println(player.getName() + ", you get 100 from your stocks dividends");
                player.increaseMoney(100);
                break;
            case 9:
                System.out.println(player.getName() + ", get one free release from prison");
                player.freeRelease = player.freeRelease + 1;
                break;
            case 10:
                System.out.println(player.getName() + ", you sign a new TV contract , get 150");
                player.increaseMoney(150);
                break;
            case 11:
                System.out.println(player.getName() + ", one of your players of one of your clubs wins Fortnite World Cup and shares 50 with you");
                player.increaseMoney(50);
                break;
            case 12:
                System.out.println(player.getName() + ", go to Sporting field");
                if (player.getPosition() > 6) {
                    System.out.println("You start the new season, get 200");
                    player.increaseMoney(200);
                }
                player.goTo(player, 6);
                whereAreYou(player);
                break;
            case 13:
                System.out.println(player.getName() + ", you have to pay taxes, 25 per each player, 100 per each forward");
                int c = 0;
                int v = 0;
                for (Club club : player.clubs) {
                    c = c + club.boughtPlayers;
                    v = v + club.boughtForwards;
                }
                player.decreaseMoney((v*115) + (c*40));
                System.out.println("In total you lose " + (c + v));
                break;
            case 14:
                System.out.println(player.getName() + ", you've been arrested for FFP");
                goToPrison(player);
                break;
            case 15:
                System.out.println(player.getName() + ", you paid referee for a penalty in an important game, too bad your player didn't score it, lose 50");
                player.decreaseMoney(50);
                break;
            case 16:
                System.out.println(player.getName() + ", go 3 squares back");
                player.move(-3);
                whereAreYou(player);
                break;
        }
    }

    public void addFederations(ArrayList<FifaUefa> federations) {
        federations.add(new FifaUefa(12,150, "UEFA"));
        federations.add(new FifaUefa(28, 150, "FIFA"));
    }

    public void addMedia(ArrayList<Media> medias) {
        medias.add(new Media(5, 200,25, "Goal.com"));
        medias.add(new Media(15,200,25, "Marca"));
        medias.add(new Media(25,200,25,"FourFourTwo"));
        medias.add(new Media(35,200,25,"TeamTalk"));
    }

    public void addClubs(ArrayList<Club> clubs) {
        clubs.add(new Club(1, 60, 2, 10, 30, 90, 160, 250, "PSV",50));
        clubs.add(new Club(3, 60, 4, 20, 60, 180, 320, 450, "Ajax",50));
        clubs.add(new Club(6, 100, 6, 30, 90, 270, 400, 550, "Sporting",50));
        clubs.add(new Club(9, 120, 8, 40, 100, 300, 450, 600,"Benfica",50));
        clubs.add(new Club(8, 100, 6, 30, 90, 270, 400, 550,"Porto",50));
        clubs.add(new Club(11, 140, 10,50,150,450,625,750, "Lyon",100));
        clubs.add(new Club(13, 140, 10, 50, 150, 450, 625, 750, "Marselle",100));
        clubs.add(new Club(14, 160, 12, 60, 180, 500, 700, 900, "PSG",100));
        clubs.add(new Club(16,180,14,70,200,550,750,950, "Red Bull Leipzig",100));
        clubs.add(new Club(18,180,14,70,200,550,750,950, "Borussia",100));
        clubs.add(new Club(19,200,16,80,220,600,800,1000, "Bayern",100));
        clubs.add(new Club(21,220,18,90,250,700,875,1050, "Lazio",150));
        clubs.add(new Club(23,220,18,90,250,700,875,1050, "Inter",150));
        clubs.add(new Club(24,240,20,100,300,750,925,1100, "Juventus",150));
        clubs.add(new Club(26,260,22,110,330,800,975,1150, "Atletico",150));
        clubs.add(new Club(27,260,22,110,330,800,975,1150, "Real Madrid",150));
        clubs.add(new Club(29,280,24,120,360,850,1025,1200, "Barcelona",150));
        clubs.add(new Club(31, 300,26,130,390,900,1100,1275, "ManUtd",200));
        clubs.add(new Club(32,300,26,130,390,900,1100,1275, "ManCity",200));
        clubs.add(new Club(34,320,28,150,450,1000,1200,1400,"Chelsea",200));
        clubs.add(new Club(37, 350,35,175,500,1100,1300,1500, "CSKA",200));
        clubs.add(new Club(39,400,50,200,600,1400,1700,2000, "Spartak",200));
    }

    public void printNonBoughtClubs(ArrayList<Club> clubs) {
        if (clubs.isEmpty()) {
            System.out.println("There are no available clubs left");
        } else {
            System.out.println("Non bought clubs are:");
            for (Club club : clubs) {
                System.out.println("Name of the club: " + club.getName() + ", from the field number " + club.getID());
            }
        }
    }

    public void printNonBoughtMedia(ArrayList<Media> medias) {
        if (medias.isEmpty()) {
            System.out.println("There are no available medias left");
        } else {
                System.out.println("Non bought media are:");
                for (Media media : medias) {
                    System.out.println("Name of media: " + media.getName() + ", from the field number " + media.getID());
            }
        }
    }

    public void printNonBoughtFederations(ArrayList<FifaUefa> federations) {
        if (federations.isEmpty()) {
            System.out.println("There are no federations left to buy");
        } else {
            System.out.println("Non bought feds are");
            for (FifaUefa fed : federations) {
                System.out.println("Name of fed: " + fed.getName() + ", from the field number " + fed.getID());
            }
        }
    }

    public void printNonBought() {
        printNonBoughtClubs(nonboughtClubs);
        printNonBoughtMedia(nonboughtMedia);
        printNonBoughtFederations(nonboughtFifaUefa);
    }

}
