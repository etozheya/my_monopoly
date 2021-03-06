public class Club {

    public int id;
    public int price;
    public int rent;
    public int rentGK;
    public int rentGKDF;
    public int rentGKDFDM;
    public int rentGKDFDMAM;
    public int rentFW;
    public int currentRent;
    public String name;
    public int boughtPlayers;
    public int boughtForwards;
    public int playerPrice;

    public Club(int id, int price, int rent, int rentGK, int rentGKDF, int rentGKDFDM, int rentGKDFDMAM, int rentFW, String name, int playerPrice) {
        this.id = id;
        this.price = price;
        this.rent = rent;
        this.rentFW = rentFW;
        this.rentGK = rentGK;
        this.rentGKDF = rentGKDF;
        this.rentGKDFDM = rentGKDFDM;
        this.rentGKDFDMAM = rentGKDFDMAM;
        this.name = name;
        this.currentRent = rent;
        this.boughtPlayers = 0;
        this.boughtForwards = 0;
        this.playerPrice = playerPrice;
    }

    public void buyPlayer() {
        int x = this.currentRent;
        if (x == this.rent) {
            this.currentRent = this.rentGK;
            System.out.println("You have bought a goalkeeper for " + this.id);
        }
        if (x == this.rentGK) {
            this.currentRent = this.rentGKDF;
            System.out.println("You have bought a defender for " + this.id);
        }
        if (x == this.rentGKDF) {
            this.currentRent = this.rentGKDFDM;
            System.out.println("You have bought a defencive midfielder for " + this.id);
        }
        if (x == this.rentGKDFDM) {
            this.currentRent = this.rentGKDFDMAM;
            System.out.println("You have bought an attacking midfielder for " + this.id);
        }
        if (x == this.rentGKDFDMAM) {
            this.currentRent = this.rentFW;
            System.out.println("You have bought a forward for " + this.id);
        }
    }

    public void sellPlayer() {
        int x = this.currentRent;
        if (x == this.rentFW) {
            this.currentRent = this.rentGKDFDMAM;
            System.out.println("You have sold a forward for " + this.id);
        }
        if (x == this.rentGK) {
            this.currentRent = this.rent;
            System.out.println("You have bought a goalkeeper for " + this.id);
        }
        if (x == this.rentGKDF) {
            this.currentRent = this.rentGK;
            System.out.println("You have sold a defender for " + this.id);
        }
        if (x == this.rentGKDFDM) {
            this.currentRent = this.rentGKDF;
            System.out.println("You have sold a defensive midfielder for " + this.id);
        }
        if (x == this.rentGKDFDMAM) {
            this.currentRent = this.rentGKDFDM;
            System.out.println("You have sold an attacking midfielder for " + this.id);
        }
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Name of the club: " + getName() + ", its id: " + getID();
    }
}
