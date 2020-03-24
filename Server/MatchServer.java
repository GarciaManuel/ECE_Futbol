import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Match;
import Team;
import Player;

class MatchServerRunnable implements Runnable {
    private Socket sock;

    public MatchServerRunnable(Socket s) {
        sock = s;
    }

    @Override
    public void run() {

        try {
            DataInputStream dis = new DataInputStream(sock.getInputStream());

            // read op1, op2 and the opreation to make
            char objectType = dis.readChar();
            int matchNum = dis.readInt();
            String attribute = dis.readUTF();
            Boolean modifyValue = dis.readBoolean();
            
            if (!modifyValue) {
                DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
                int resInt;
                String resString;

                if (attribute.equals("date")) {
                    resString = MatchServer.getDate(matchNum);
                } else if (attribute.equals("gameSets")) {
                    int gameSet = dis.readInt();
                    resInt = MatchServer.getGameSet(matchNum, gameSet);
                } else {
                    int teamNum = dis.readInt();
                    if (objectType == 'T') {
                        if (attribute.equals("name")) {
                            resString = MatchServer.getTeamAttr(matchNum, teamNum);
                        } else {
                            resInt = MatchServer.getTeamAttr(matchNum, teamNum, attribute);
                        }
                    } else {
                        int playerNum = dis.readInt();
                        if (attribute.equals("name")) {
                            resString = MatchServer.getPlayerAttr(matchNum, teamNum, playerNum);
                        } else {
                            resInt = MatchServer.getPlayerAttr(matchNum, teamNum, playerNum, attribute);
                        }
                    }
                }
                if (attribute.equals("date") || attribute.equals("name")) {
                    dos.writeInt(resString);
                } else {
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
                    if (objectType == 'T') {
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

    Match games[5];


    public static String getDate(int matchNum) throws Exception {
        return games[matchNum].getDate();
    }

    public static int getGameSet(int matchNum, int gameSet) throws Exception {
        return games[matchNum].getGameSet(gameSet);
    }

    public static String getTeamAttr(int matchNum, int teamNum) throws Exception {
        return games[matchNum].getTeam(teamNum).getName();
    }

    public static int getTeamAttr(int matchNum, int teamNum, String attribute) throws Exception {
        return games[matchNum].getTeam(teamNum).getFault(attribute);
    }

    public static String getPlayerAttr(int matchNum, int teamNum, int playerNum) throws Exception {
        return games[matchNum].getTeam(teamNum).getPlayer(playerNum).getName();
    }

    public static int getPlayerAttr(int matchNum, int teamNum, int playerNum, String attribute) throws Exception {  
        return games[matchNum].getTeam(teamNum).getPlayer(playerNum).getFault(attribute);
    }

    public static void setGameSet(int matchNum, int gameSet, int attributeValue) throws Exception {
        games[matchNum].setGameSets(gameSet,attributeValue);
    }

    public static void setTeamAttr(int matchNum, int teamNum, String attribute, int attributeValue) throws Exception {
        if (attributeValue == 1) {
            games[matchNum].getTeam(teamNum).addFault(attribute);
        } else {
            games[matchNum].getTeam(teamNum).subFault(attribute);
        }
    }

    public static void setPlayerAttr(int matchNum, int teamNum, int playerNum, String attribute, int attributeValue) throws Exception {
        if (attributeValue == 1) {
            games[matchNum].getTeam(teamNum).getPlayer(playerNum).addFault(attribute);
        } else {
            games[matchNum].getTeam(teamNum).getPlayer(playerNum).subFault(attribute);
        }
    }


    public static void main(String[] args) throws Exception {

        games[0] = new Match("Monday", new Team("ECE", "Barbara", "Laura"), new Team("CUHK", "James", "Jenna"));
        games[1] = new Match("Tuesday", new Team("Tec", "Andrea", "Rebeca"), new Team("CiteU", "Stein", "Diego"));
        games[2] = new Match("Wednesday", new Team("CUHK", "James", "Jenna"), new Team("Tec", "Barbara", "Laura"));
        games[3] = new Match("Thursday", new Team("ECE", "Barbara", "Laura"), new Team("Tec", "Andrea", "Rebeca"));
        games[4] = new Match("Friday", new Team("CiteU", "Stein", "Diego"), new Team("CUHK", "James", "Jenna"));

        // Example of a distant calculator
        ServerSocket sock = new ServerSocket(9876);
        while (true) { // infinite loop
            Socket comm = sock.accept();
            System.out.println("connection established");

            new Thread(new MyCalculusRunnable(comm)).start();

        }

    }

}