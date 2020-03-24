public class Match {
    String date;
    Team teamA;
    Team teamB;
    int gameSets[5];

    public Match(String date, Team teamA, Team teamB) {
        this.date = date;
        this.teamA = teamA;
        this.teamB = teamB;
    }
    
    public Team getTeam() {
        if (num == 1) {
            return teamA;
        } else {
            return teamB;
        }
    }    

    public void setGameSets(int match, int team) {
        gameSets[match] = team;
    }   

    public int getGameSet(int match) {
        return gameSets[match];
    }

}
