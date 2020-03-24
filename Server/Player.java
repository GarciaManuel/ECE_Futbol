public class Player {
    String name;
    int assistedHitFault;
    int doubleContactFault;
    int catchLiftFault;
    int footFault;
    int netTouchFault;

    public Player(String name) {
        this.name = name;
        assistedHitFault = 0;
        doubleContactFault = 0;
        catchLiftFault = 0;
        footFault = 0;
        netTouchFault = 0;
    }

    public void addFault(String fault) {
        if (fault.equals("assistedHitFault")) {
            assistedHitFault++;
        }
        else if (fault.equals("doubleContactFault")) {
            doubleContactFault++;
        }
        else if (fault.equals("catchLiftFault")) {
            catchLiftFault++;
        }
        else if (fault.equals("footFault")) {
            footFault++;
        } else {
            netTouchFault++;
        }
    }

    public void subFault(String fault) {
        if (fault.equals("assistedHitFault")) {
            assistedHitFault--;
        }
        else if (fault.equals("doubleContactFault")) {
            doubleContactFault--;
        }
        else if (fault.equals("catchLiftFault")) {
            catchLiftFault--;
        }
        else if (fault.equals("footFault")) {
            footFault--;
        } else {
            netTouchFault--;
        }
    }

    public int getFault(String fault) {
        if (fault.equals("assistedHitFault")) {
            return assistedHitFault;
        }
        else if (fault.equals("doubleContactFault")) {
            return doubleContactFault;
        }
        else if (fault.equals("catchLiftFault")) {
            return catchLiftFault;
        }
        else if (fault.equals("footFault")) {
            return footFault;
        } else {
            return netTouchFault;
        }
    }    



}
