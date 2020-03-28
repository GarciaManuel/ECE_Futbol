
public class Player {
    String name;
    int assistedHit;
    int doubleContact;
    int catchLift;
    int foot;
    int netTouch;

    public Player(String name) {
        this.name = name;
        assistedHit = 0;
        doubleContact = 0;
        catchLift = 0;
        foot = 0;
        netTouch = 0;
    }

    public String getName() {
        return name;
    }

    public int getAllFaults() {
        return assistedHit + doubleContact + catchLift + foot + netTouch;
    }

    public void addFault(String fault) {
        if (fault.equals("assistedHit")) {
            assistedHit++;
        } else if (fault.equals("doubleContact")) {
            doubleContact++;
        } else if (fault.equals("catchLift")) {
            catchLift++;
        } else if (fault.equals("foot")) {
            foot++;
        } else if (fault.equals("netTouch")) {
            netTouch++;
        }
    }

    public void subFault(String fault) {
        if (fault.equals("assistedHit")) {
            assistedHit--;
        }
        else if (fault.equals("doubleContact")) {
            doubleContact--;
        }
        else if (fault.equals("catchLift")) {
            catchLift--;
        }
        else if (fault.equals("foot")) {
            foot--;
        } else if (fault.equals("netTouch")){
            netTouch--;
        }
    }

    public int getFault(String fault) {
        if (fault.equals("assistedHit")) {
            return assistedHit;
        }
        else if (fault.equals("doubleContact")) {
            return doubleContact;
        }
        else if (fault.equals("catchLift")) {
            return catchLift;
        }
        else if (fault.equals("foot")) {
            return foot;
        } else if (fault.equals("netTouch")) {
            return netTouch;
        } else {
            return 0;
        }
    }



}
