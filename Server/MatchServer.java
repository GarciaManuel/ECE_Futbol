import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MatchServerRunnable implements Runnable {
    private Socket sock;

    public MatchServerRunnable(Socket s) {
        sock = s;
    }

    @Override
    public void run() {

        try {
            DataInputStream dis = new DataInputStream(sock.getInputStream());

            List<String> faultsPlayer = new ArrayList<>(Arrays.asList("assistedHit", "doubleContact", "catchLift", "foot", "netTouch"));
            List<String> faultsTeam = new ArrayList<>(Arrays.asList("fourHits", "serviceOrder"));
            char objectType = dis.readChar();
            int matchNum = dis.readInt();
            String attribute = dis.readUTF();
            Boolean modifyValue = dis.readBoolean();

            if (!modifyValue) {
                DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
                int resInt = -1;
                String resString = "";

                if (attribute.equals("date")) {
                    resString = MatchServer.getDate(matchNum);
                } else if (attribute.equals("allMatches")) {
                    for(String match : MatchServer.getMatches()) {
                        dos.writeUTF(match);
                    }
                } else if (attribute.equals("gameSets")) {
                    int gameSet = dis.readInt();
                    resInt = MatchServer.getGameSet(matchNum, gameSet);
                } else if (attribute.equals("allPoints")) {
                    for(int i = 0; i < 2; i++) {
                        for (int j = 0; j < 5; j++) {
                            dos.writeInt(MatchServer.getPoints(matchNum, j, i));
                        }
                    }
                } else if (attribute.equals("winnerTeam")) {
                    resInt = MatchServer.getWinnerTeam(matchNum);
                } else {
                    int teamNum = dis.readInt();
                    if (objectType == 'M') {
                        int gameSet = dis.readInt();
                        resInt = MatchServer.getPoints(matchNum, gameSet, teamNum);
                    }
                    else if (objectType == 'T') {
                        if (attribute.equals("name")) {
                            resString = MatchServer.getTeamAttr(matchNum, teamNum);
                        } else if (faultsTeam.contains(attribute)){
                            resInt = MatchServer.getTeamAttr(matchNum, teamNum, attribute);
                        } else if (attribute.equals("bestPlayer")) {
                            resInt = MatchServer.getBestPlayer(matchNum, teamNum);
                        } else if (attribute.equals("allFaults")) {
                            for(int i = 0; i < 2; i++) {
                                for (String fault : faultsPlayer) {
                                    dos.writeInt(MatchServer.getPlayerAttr(matchNum, teamNum, i, fault));
                                }
                            }
                        }
                    } else {
                        int playerNum = dis.readInt();
                        if (attribute.equals("name")) {
                            resString = MatchServer.getPlayerAttr(matchNum, teamNum, playerNum);
                        } else if (faultsPlayer.contains(attribute)){
                            resInt = MatchServer.getPlayerAttr(matchNum, teamNum, playerNum, attribute);
                        }
                    }
                }
                if (!resString.equals("")) {
                    dos.writeUTF(resString);
                } else if (resInt != -1){
                    dos.writeInt(resInt);
                }
                dos.close();
            } else {
                int attributeValue = dis.readInt();
                if (attribute.equals("gameSets")) {
                    int gameSet = dis.readInt();
                    MatchServer.setGameSet(matchNum, gameSet, attributeValue);
                } else {
                    int teamNum = dis.readInt();
                    if (objectType == 'M') {
                        int gameSet = dis.readInt();
                        MatchServer.setPoints(matchNum, gameSet, teamNum, attributeValue);
                    } else if (objectType == 'T') {
                        MatchServer.setTeamAttr(matchNum, teamNum, attribute, attributeValue);
                    } else {
                        int playerNum = dis.readInt();
                        MatchServer.setPlayerAttr(matchNum, teamNum, playerNum, attribute, attributeValue);
                    }
                }
            }
            dis.close();
            sock.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


public class MatchServer {
    private static Match games[];

    public static String getDate(int matchNum) throws Exception {
        System.out.println("getDate for match " + matchNum);
        System.out.println(games[matchNum].getDate());
        return games[matchNum].getDate();
    }

    public static String[] getMatches() {
        String matches[] = new String[5];
        for(int i = 0; i < 5; i++) {
            matches[i] = games[i].getTeam(0).getName() + " vs. " +
                    games[i].getTeam(1).getName();
        }
        return matches;
    }

    public static int getWinnerTeam(int matchNum) throws Exception {
//        System.out.println("winner team for match " + matchNum);
//        System.out.println();
        return  games[matchNum].getWinner();
    }

    public static int getBestPlayer(int matchNum, int teamNum) throws Exception {
        System.out.println("best player  team for match " + matchNum);
        System.out.println();
        return  games[matchNum].getTeam(teamNum).getBestPlayer();
    }

    public static int getGameSet(int matchNum, int gameSet) throws Exception {
        System.out.println("getGameSet for match " + matchNum + " in game set " + gameSet);
        System.out.println(games[matchNum].getGameSet(gameSet));
        return games[matchNum].getGameSet(gameSet);
    }

    public static int getPoints(int matchNum, int gameSet, int teamNum) throws Exception {
        System.out.println("getPoints for match " + matchNum + " in game set " + gameSet + " for team " + teamNum);
        System.out.println(games[matchNum].getPoints(gameSet, teamNum));
        return games[matchNum].getPoints(gameSet, teamNum);
    }

    public static String getTeamAttr(int matchNum, int teamNum) throws Exception {
        System.out.println("getTeamName for match " + matchNum + " for team " + teamNum);
        System.out.println(games[matchNum].getTeam(teamNum).getName());
        return games[matchNum].getTeam(teamNum).getName();
    }

    public static int getTeamAttr(int matchNum, int teamNum, String attribute) throws Exception {
        System.out.println("get " + attribute + " for match " + matchNum + " for team " + teamNum);
        System.out.println(games[matchNum].getTeam(teamNum).getFault(attribute));
        return games[matchNum].getTeam(teamNum).getFault(attribute);
    }

    public static String getPlayerAttr(int matchNum, int teamNum, int playerNum) throws Exception {
        System.out.println("getPlayerName for match " + matchNum + " for team " + teamNum + "for player " + playerNum);
        System.out.println(games[matchNum].getTeam(teamNum).getPlayer(playerNum).getName());
        return games[matchNum].getTeam(teamNum).getPlayer(playerNum).getName();
    }

    public static int getPlayerAttr(int matchNum, int teamNum, int playerNum, String attribute) throws Exception {
        System.out.println("get " + attribute + " for match " + matchNum + " for team " + teamNum + "for player " + playerNum);
        System.out.println(games[matchNum].getTeam(teamNum).getPlayer(playerNum).getFault(attribute));
        return games[matchNum].getTeam(teamNum).getPlayer(playerNum).getFault(attribute);
    }

    public static void setGameSet(int matchNum, int gameSet, int attributeValue) throws Exception {
        System.out.println("setGameSet for match " + matchNum + " in game set " + gameSet + " value " + attributeValue);
        games[matchNum].setGameSets(gameSet,attributeValue);
    }

    public static void setPoints(int matchNum, int gameSet, int teamNum, int attributeValue) throws Exception {
        System.out.println("setPoints for match " + matchNum + " in game set " + gameSet + " for team " + teamNum + " value " + attributeValue);
        games[matchNum].setPoints(gameSet, teamNum, attributeValue);
    }

    public static void setTeamAttr(int matchNum, int teamNum, String attribute, int attributeValue) throws Exception {
        System.out.println("set " + attribute + " for match " + matchNum + " for team " + teamNum + " value " + attributeValue);
        if (attributeValue == 1) {
            games[matchNum].getTeam(teamNum).addFault(attribute);
        } else {
            games[matchNum].getTeam(teamNum).subFault(attribute);
        }
    }

    public static void setPlayerAttr(int matchNum, int teamNum, int playerNum, String attribute, int attributeValue) throws Exception {
        System.out.println("set " + attribute + " for match " + matchNum + " for team " + teamNum + "for player " + playerNum + " value " + attributeValue);
        if (attributeValue == 1) {
            games[matchNum].getTeam(teamNum).getPlayer(playerNum).addFault(attribute);
        } else {
            games[matchNum].getTeam(teamNum).getPlayer(playerNum).subFault(attribute);
        }
    }


    public static void main(String[] args) throws Exception {
        games = new Match[5];
        games[0] = new Match("Monday", new Team("ECE", "Barbara", "Laura"), new Team("CUHK", "James", "Jenna"));
        games[1] = new Match("Tuesday", new Team("Tec", "Andrea", "Rebeca"), new Team("CiteU", "Stein", "Diego"));
        games[2] = new Match("Wednesday", new Team("CUHK", "James", "Jenna"), new Team("Tec", "Barbara", "Laura"));
        games[3] = new Match("Thursday", new Team("ECE", "Barbara", "Laura"), new Team("Tec", "Andrea", "Rebeca"));
        games[4] = new Match("Friday", new Team("CiteU", "Stein", "Diego"), new Team("CUHK", "James", "Jenna"));

        // Example of a distant calculator
        ServerSocket sock = new ServerSocket(9876);
        System.out.println("start running server");
        while (true) { // infinite loop
            Socket comm = sock.accept();
            System.out.println("connection established");

            new Thread(new MatchServerRunnable(comm)).start();

        }

    }

}