//import Team.java;

public class Match {
    String date;
    Team teamA;
    Team teamB;
    int gameSets[];
    int points[][];

    public Match(String date, Team teamA, Team teamB) {
        gameSets = new int[]{2,2,2,2,2};
        points = new int[][]{{0,0},{0,0},{0,0},{0,0},{0,0}};
        this.date = date;
        this.teamA = teamA;
        this.teamB = teamB;
    }

    public String getDate() {
        return date;
    }

    public int getWinner() {
        int winA = 0;
        int winB = 0;
        for (int set: gameSets) {
            if (set == 0) {
                winA++;
            } else if (set == 1) {
                winB++;
            }
        }
        return (winA >= winB) ? 0 : 1;
    }

    public Team getTeam(int num) {
        if (num == 0) {
            return teamA;
        } else {
            return teamB;
        }
    }

    public void setPoints(int match, int team, int value) {
        if (value == 1) {
            points[match][team]++;
        } else if (points[match][team] != 0) {
            points[match][team]--;
        }
    }

    public int getPoints(int match, int team) {
        return points[match][team];
    }

    public void setGameSets(int match, int team) {
        gameSets[match] = team;
    }

    public int getGameSet(int match) {
        return gameSets[match];
    }

}
