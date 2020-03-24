
public class Team {
    String name;
    Player player1;
    Player player2;
    int fourHitsFault;
    int serviceOrderFault;

    public Team(String name, String player1, String player2) {
        this.name = name;
        this.player1 = new Player(player1);
        this.player2 = new Player(player2);
        fourHitsFault = 0;
        serviceOrderFault = 0;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer(int num) {
        if (num == 1) {
            return player1;
        } else {
            return player2;
        }
    }

    public void addFault(String fault) {
        if (fault.equals("fourHitsFault")) {
            fourHitsFault++;
        } else {
            serviceOrderFault++;
        }
    }

    public void subFault(String fault) {
        if (fault.equals("fourHitsFault")) {
            fourHitsFault--;
        } else {
            serviceOrderFault--;
        }
    }

    public int getFault(String fault) {
        if (fault.equals("fourHitsFault")) {
            return fourHitsFault;
        } else {
            return serviceOrderFault;
        }
    }




}