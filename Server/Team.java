public class Team {
    String name;
    Player player1;
    Player player2;
    int fourHits;
    int serviceOrder;

    public Team(String name, String player1, String player2) {
        this.name = name;
        this.player1 = new Player(player1);
        this.player2 = new Player(player2);
        fourHits = 0;
        serviceOrder = 0;
    }

    public String getName() {
        return name;
    }

    public int getBestPlayer() {
        return (player1.getAllFaults() <= player2.getAllFaults()) ? 0 : 1;
    }

    public Player getPlayer(int num) {
        if (num == 0) {
            return player1;
        } else {
            return player2;
        }
    }

    public void addFault(String fault) {
        if (fault.equals("fourHits")) {
            fourHits++;
        } else if (fault.equals("serviceOrder")) {
            serviceOrder++;
        }
    }

    public void subFault(String fault) {
        if (fault.equals("fourHits")) {
            fourHits--;
        } else if (fault.equals("serviceOrder")) {
            serviceOrder--;
        }
    }

    public int getFault(String fault) {
        if (fault.equals("fourHits")) {
            return fourHits;
        } else  if (fault.equals("serviceOrder")) {
            return serviceOrder;
        } else {
            return  0;
        }
    }




}