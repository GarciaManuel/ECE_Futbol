package com.example.ece_futbol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView pointsTeamA;
    Button assistedHitTeamA;
    Button doubleContactTeamA;
    Button catchLiftTeamA;
    Button footTeamA;
    Button netTouchTeamA;

    TextView pointsTeamB;
    Button assistedHitTeamB;
    Button doubleContactTeamB;
    Button catchLiftTeamB;
    Button footTeamB;
    Button netTouchTeamB;

    String [] faultsPlayer;
    String [] faultsPlayerText;
    ToggleButton toggle;

    private  static final String STATE_PLAYERA = "playerA";
    private  static final String STATE_PLAYERB = "playerB";
    private  static final String STATE_TEAM = "team";
    private  static final String STATE_MATCH = "match";
    private  static final String STATE_SET = "set";

    String playerTeamA;
    String playerTeamB;
    String currentMatch;
    String currentTeam;
    String currentSet;

    Button fourHits;
    Button serviceOrder;

    MatchServer matchServer;

    DatabaseHelper db;
    boolean localStorage;
    MatchGame mg;
    Team tA;
    Team tB;
    Player p1A;
    Player p2A;
    Player p1B;
    Player p2B;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        faultsPlayer = new String[]{"assistedHit", "doubleContact", "catchLift", "foot", "netTouch"};
        faultsPlayerText = new String[5];
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            faultsPlayerText[0] = getResources().getString(R.string.assistedHit);
            faultsPlayerText[1] = getResources().getString(R.string.doubleContact);
            faultsPlayerText[2] = getResources().getString(R.string.catchLift);
            faultsPlayerText[3] = getResources().getString(R.string.foot);
            faultsPlayerText[4] = getResources().getString(R.string.netTouch);
        } else {
            faultsPlayerText[0] = getResources().getString(R.string.assistedHitLan);
            faultsPlayerText[1] = getResources().getString(R.string.doubleContactLan);
            faultsPlayerText[2] = getResources().getString(R.string.catchLiftLan);
            faultsPlayerText[3] = getResources().getString(R.string.footLan);
            faultsPlayerText[4] = getResources().getString(R.string.netTouchLan);
        }

        playerTeamA = "0";
        playerTeamB = "0";
        currentMatch = "0";
        currentTeam = "0";
        currentSet = "0";

        mg = new MatchGame();
        tA = new Team();
        tB = new Team();
        p1A = new Player();
        p2A = new Player();
        p1B = new Player();
        p2B = new Player();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentMatch = Long.toString(extras.getLong("currentMatch"));
            localStorage = Boolean.parseBoolean(extras.getString("localStorage"));
        }

        pointsTeamA = findViewById(R.id.pointsTeamA);
        assistedHitTeamA = findViewById(R.id.assistedHitTeamA);
        doubleContactTeamA = findViewById(R.id.doubleContactTeamA);
        catchLiftTeamA = findViewById(R.id.catchLiftTeamA);
        footTeamA = findViewById(R.id.footTeamA);
        netTouchTeamA = findViewById(R.id.netTouchTeamA);

        pointsTeamB = findViewById(R.id.pointsTeamB);
        assistedHitTeamB = findViewById(R.id.assistedHitTeamB);
        doubleContactTeamB = findViewById(R.id.doubleContactTeamB);
        catchLiftTeamB = findViewById(R.id.catchLiftTeamB);
        footTeamB = findViewById(R.id.footTeamB);
        netTouchTeamB = findViewById(R.id.netTouchTeamB);

        fourHits = findViewById(R.id.fourHits);
        serviceOrder = findViewById(R.id.serviceOrder);

        if (savedInstanceState != null) {
            playerTeamA = savedInstanceState.getString(STATE_PLAYERA, "0");
            playerTeamB = savedInstanceState.getString(STATE_PLAYERB, "0");
            currentMatch = savedInstanceState.getString(STATE_MATCH, "0");
            currentTeam = savedInstanceState.getString(STATE_TEAM, "0");
            currentSet = savedInstanceState.getString(STATE_SET, "0");
        }

        db = new DatabaseHelper(getApplicationContext());
        initLocalDatabase();

        initPoints('A', "0");
        initPoints('A', "1");
        initMatchServer("pointsTeamA", "M", currentMatch, "gamePoints", Boolean.toString(false), "0", currentSet);
        initPoints('B', "0");
        initPoints('B', "1");
        initMatchServer("pointsTeamB", "M", currentMatch, "gamePoints", Boolean.toString(false), "1", currentSet);

        for (String fault: faultsPlayer) {
            initFaultsPlayer(fault, 'A');
            initFaultsPlayer(fault, 'B');
        }

        initFaultsTeam("fourHits");
        initFaultsTeam("serviceOrder");

        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        initMatchServer("toggle", "M", currentMatch, "gameSets", Boolean.toString(false), currentSet);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (localStorage) {
                    setMatchGame(isChecked);
                }
                else if (isChecked) {
                    initMatchServer("toggle", "M", currentMatch, "gameSets", Boolean.toString(true), "1", currentSet);
                } else {
                    initMatchServer("toggle", "M", currentMatch, "gameSets", Boolean.toString(true), "0", currentSet);
                }
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.matches_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.num_sets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(Integer.parseInt(currentSet));
        spinner.setOnItemSelectedListener(this);
    }

    public void onPlayerTeamA(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            return;
        }
        switch (view.getId()) {
            case R.id.player1TeamA:
                playerTeamA = "0";
                break;
            case R.id.player2TeamA:
                playerTeamA = "1";
                break;
        }
        for (String fault: faultsPlayer) {
            initMatchServer(fault + "TeamA", "P", currentMatch, fault, Boolean.toString(false), "0", playerTeamA);
        }
    }

    public void onPlayerTeamB(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            return;
        }
        switch (view.getId()) {
            case R.id.player1TeamB:
                playerTeamB = "0";
                break;
            case R.id.player2TeamB:
                playerTeamB = "1";
                break;
        }
        for (String fault: faultsPlayer) {
            initMatchServer(fault + "TeamB", "P", currentMatch, fault, Boolean.toString(false), "1", playerTeamB);
        }
    }

    public void onTeams(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            return;
        }
        switch (view.getId()) {
            case R.id.teamARadio:
                currentTeam = "0";
                break;
            case R.id.teamBRadio:
                currentTeam = "1";
                break;
        }
        initMatchServer("fourHits", "T", currentMatch, "fourHits", Boolean.toString(false), currentTeam);
        initMatchServer("serviceOrder", "T", currentMatch, "serviceOrder", Boolean.toString(false), currentTeam);
    }

    private void initPoints(final char teamChar, final String action) {
        final String currentTeamPos = (teamChar == 'A') ? "0" : "1";

        // Points TextView
        final String textIdName = "pointsTeam" + teamChar;
        final int textId = getResources().getIdentifier(textIdName, "id", getPackageName());
        final TextView textPoints = (TextView) findViewById(textId);

        // Add/Sub button
        final String objectId = ((action.equals("1")) ? "add" : "sub") + "Team" + teamChar;
        final int num = (action.equals("1")) ? 1 : -1;
        int resId = getResources().getIdentifier(objectId, "id", getPackageName());
        Button bPoints = (Button) findViewById(resId);
        bPoints.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateValue(Integer.parseInt(textPoints.getText().toString()) + num, textIdName);
                        initMatchServer(textIdName, "M", currentMatch, "gamePoints", Boolean.toString(true), action, currentTeamPos, currentSet);
                    }
                });
    }

    private void initFaultsPlayer(final String attribute, final char teamChar) {
        final String currentTeamPos = (teamChar == 'A') ? "0" : "1";
        String currentPlayerInit = (teamChar == 'A') ? playerTeamA : playerTeamB;
        final String objectId = attribute + "Team" + teamChar;
        final int resId = getResources().getIdentifier(objectId, "id", getPackageName());
        final Button bFault = (Button) findViewById(resId);

        bFault.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentPlayer = (teamChar == 'A') ? playerTeamA : playerTeamB;
                        int pos = ((Button) v).getText().toString().indexOf(":");
                        int value = Integer.parseInt(((Button) v).getText().toString().substring(pos + 2));
                        updateValue(value + 1, objectId);
                        initMatchServer(objectId, "P", currentMatch, attribute, Boolean.toString(true), "1", currentTeamPos, currentPlayer);
                    }
                }
        );
        bFault.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        String currentPlayer = (teamChar == 'A') ? playerTeamA : playerTeamB;
                        int pos = ((Button) v).getText().toString().indexOf(":");
                        final int value = Integer.parseInt(((Button) v).getText().toString().substring(pos + 2));
                        updateValue(value - 1, objectId);
                        initMatchServer(objectId, "P", currentMatch, attribute, Boolean.toString(true), "0", currentTeamPos, currentPlayer);
                        return true;
                    }
                }
        );
        initMatchServer(objectId, "P", currentMatch, attribute, Boolean.toString(false), currentTeamPos, currentPlayerInit);
    }

    private void initFaultsTeam(final String attribute) {
        int resId = getResources().getIdentifier(attribute, "id", getPackageName());
        Button bFault = (Button) findViewById(resId);

        bFault.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = ((Button) v).getText().toString().indexOf(":");
                        final int value = Integer.parseInt(((Button) v).getText().toString().substring(pos + 2));
                        updateValue(value + 1, attribute);
                        initMatchServer(attribute, "T", currentMatch, attribute, Boolean.toString(true), "1", currentTeam);
                    }
                }
        );
        bFault.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = ((Button) v).getText().toString().indexOf(":");
                        final int value = Integer.parseInt(((Button) v).getText().toString().substring(pos + 2));
                        updateValue(value - 1, attribute);
                        initMatchServer(attribute, "T", currentMatch, attribute, Boolean.toString(true), "0", currentTeam);
                        return true;
                    }
                }
        );
        initMatchServer(attribute, "T", currentMatch, attribute, Boolean.toString(false), currentTeam);
    }

    private void initMatchServer(String...inputString) {
        if (localStorage && inputString[4].equals("false")) {
           getLocalValue(inputString[0]);
        } else if (!localStorage){
            matchServer = new MatchServer();
            matchServer.execute(inputString);
        }
    }

    private void updateValue(int value, String objectId) {
        if (value < 0) {
            return;
        } else if (localStorage) {
            setLocalValue(value, objectId);
        }

        if (objectId.equals("pointsTeamA")) {
            pointsTeamA.setText(Integer.toString(value));
        }  else if (objectId.equals("assistedHitTeamA")) {
            assistedHitTeamA.setText(faultsPlayerText[0] + ": " + value);
        } else if (objectId.equals("doubleContactTeamA")) {
            doubleContactTeamA.setText(faultsPlayerText[1] + ": " + value);
        } else if (objectId.equals("catchLiftTeamA")) {
            catchLiftTeamA.setText(faultsPlayerText[2] + ": " + value);
        } else if (objectId.equals("footTeamA")) {
            footTeamA.setText(faultsPlayerText[3] + ": " + value);
        } else if (objectId.equals("netTouchTeamA")) {
            netTouchTeamA.setText(faultsPlayerText[4] + ": " + value);
        } else if (objectId.equals("pointsTeamB")) {
            pointsTeamB.setText(Integer.toString(value));
        } else if (objectId.equals("assistedHitTeamB")) {
            assistedHitTeamB.setText(faultsPlayerText[0] + ": " + value);
        } else if (objectId.equals("doubleContactTeamB")) {
            doubleContactTeamB.setText(faultsPlayerText[1] + ": " + value);
        } else if (objectId.equals("catchLiftTeamB")) {
            catchLiftTeamB.setText(faultsPlayerText[2] + ": " + value);
        } else if (objectId.equals("footTeamB")) {
            footTeamB.setText(faultsPlayerText[3] + ": " + value);
        } else if (objectId.equals("netTouchTeamB")) {
            netTouchTeamB.setText(faultsPlayerText[4] + ": " + value);
        } else if (objectId.equals("fourHits")) {
            fourHits.setText(getResources().getString(R.string.fourHits) + ": " + value);
        } else if (objectId.equals("serviceOrder")) {
            serviceOrder.setText(getResources().getString(R.string.serviceOrder) + ": " + value);
        } else if (objectId.equals("toggle")) {
            if (value == 0) {
                toggle.setChecked(false);
            } else {
                toggle.setChecked(true);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentSet = Integer.toString(position);
        initMatchServer("pointsTeamA", "M", currentMatch, "gamePoints", Boolean.toString(false), "0", currentSet);
        initMatchServer("pointsTeamB", "M", currentMatch, "gamePoints", Boolean.toString(false), "1", currentSet);
        initMatchServer("toggle", "M", currentMatch, "gameSets", Boolean.toString(false), currentSet);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    // <----------- LocalStorage functions ----------->
    private String getTeamPoints(Team t) {
        int value;
        switch (Integer.parseInt(currentSet)) {
            case 0:
                value = t.getGameSets0();
                break;
            case 1:
                value = t.getGameSets1();
                break;
            case 2:
                value = t.getGameSets2();
                break;
            case 3:
                value = t.getGameSets3();
                break;
            case 4:
                value = t.getGameSets4();
                break;
            default :
                value = 0;
        }

        return Integer.toString(value);
    }

    private String setTeamPoints(Team t, int value) {
        switch (Integer.parseInt(currentSet)) {
            case 0:
                t.setGameSets0(value);
                break;
            case 1:
                t.setGameSets1(value);
                break;
            case 2:
                t.setGameSets2(value);
                break;
            case 3:
                t.setGameSets3(value);
                break;
            case 4:
                t.setGameSets4(value);
                break;
            default :
        }

        return Integer.toString(value);
    }

    private int getMatchWinner() {
        switch (Integer.parseInt(currentSet)) {
            case 0:
                return mg.getGameSets0();
            case 1:
                return mg.getGameSets1();
            case 2:
                return mg.getGameSets2();
            case 3:
                return mg.getGameSets3();
            case 4:
                return mg.getGameSets4();
            default :
                return -1;
        }
    }

    void setMatchGame(boolean isChecked) {
        int value = isChecked ? 1 : 0;
        if (currentSet.equals("0")) {
            mg.setGameSets0(value);
        } else if (currentSet.equals("1")) {
            mg.setGameSets1(value);
        }else if (currentSet.equals("2")) {
            mg.setGameSets2(value);
        }else if (currentSet.equals("3")) {
            mg.setGameSets3(value);
        }else if (currentSet.equals("4")) {
            mg.setGameSets4(value);
        }
    }

    private void getLocalValue(String objectId) {
        Player pA = playerTeamA.equals("0") ? p1A : p2A;
        Player pB = playerTeamB.equals("0") ? p1B : p2B;
        Team t = currentTeam.equals("0") ? tA : tB;
        if (objectId.equals("pointsTeamA")) {
            pointsTeamA.setText(getTeamPoints(tA));
        }  else if (objectId.equals("assistedHitTeamA")) {
            assistedHitTeamA.setText(faultsPlayerText[0] + ": " + pA.getAssistedHit());
        } else if (objectId.equals("doubleContactTeamA")) {
            doubleContactTeamA.setText(faultsPlayerText[1] + ": " + pA.getDoubleContact());
        } else if (objectId.equals("catchLiftTeamA")) {
            catchLiftTeamA.setText(faultsPlayerText[2] + ": " + pA.getCatchLift());
        } else if (objectId.equals("footTeamA")) {
            footTeamA.setText(faultsPlayerText[3] + ": " + pA.getFoot());
        } else if (objectId.equals("netTouchTeamA")) {
            netTouchTeamA.setText(faultsPlayerText[4] + ": " + pA.getNetTouch());
        } else if (objectId.equals("pointsTeamB")) {
            pointsTeamB.setText(getTeamPoints(tB));
        } else if (objectId.equals("assistedHitTeamB")) {
            assistedHitTeamB.setText(faultsPlayerText[0] + ": " + pB.getAssistedHit());
        } else if (objectId.equals("doubleContactTeamB")) {
            doubleContactTeamB.setText(faultsPlayerText[1] + ": " + pB.getDoubleContact());
        } else if (objectId.equals("catchLiftTeamB")) {
            catchLiftTeamB.setText(faultsPlayerText[2] + ": " + pB.getCatchLift());
        } else if (objectId.equals("footTeamB")) {
            footTeamB.setText(faultsPlayerText[3] + ": " + pB.getFoot());
        } else if (objectId.equals("netTouchTeamB")) {
            netTouchTeamB.setText(faultsPlayerText[4] + ": " + pB.getNetTouch());
        } else if (objectId.equals("fourHits")) {
            fourHits.setText(getResources().getString(R.string.fourHits) + ": " + t.getFourHits());
        } else if (objectId.equals("serviceOrder")) {
            serviceOrder.setText(getResources().getString(R.string.serviceOrder) + ": " + t.getServiceOrder());
        } else if (objectId.equals("toggle")) {
            if (getMatchWinner() <= 0) {
                toggle.setChecked(false);
            } else {
                toggle.setChecked(true);
            }
        }
    }

    private void setLocalValue(int value, String objectId) {
        Player pA = playerTeamA.equals("0") ? p1A : p2A;
        Player pB = playerTeamB.equals("0") ? p1B : p2B;
        Team t = currentTeam.equals("0") ? tA : tB;
        if (objectId.equals("pointsTeamA")) {
            setTeamPoints(tA, value);
        }  else if (objectId.equals("assistedHitTeamA")) {
            pA.setAssistedHit(value);
        } else if (objectId.equals("doubleContactTeamA")) {
            pA.setDoubleContact(value);
        } else if (objectId.equals("catchLiftTeamA")) {
            pA.setCatchLift(value);
        } else if (objectId.equals("footTeamA")) {
            pA.setFoot(value);
        } else if (objectId.equals("netTouchTeamA")) {
            pA.setNetTouch(value);
        } else if (objectId.equals("pointsTeamB")) {
            setTeamPoints(tB, value);
        }  else if (objectId.equals("assistedHitTeamB")) {
            pB.setAssistedHit(value);
        } else if (objectId.equals("doubleContactTeamB")) {
            pB.setDoubleContact(value);
        } else if (objectId.equals("catchLiftTeamB")) {
            pB.setCatchLift(value);
        } else if (objectId.equals("footTeamB")) {
            pB.setFoot(value);
        } else if (objectId.equals("netTouchTeamB")) {
            pB.setNetTouch(value);
        } else if (objectId.equals("fourHits")) {
            t.setFourHits(value);
        } else if (objectId.equals("serviceOrder")) {
            t.setServiceOrder(value);
        }
    }

    void initLocalDatabase() {
        if (!localStorage) {
            return;
        }
        mg = db.getMatchGame(Integer.parseInt(currentMatch));
        tA = db.getTeam(mg.getTeamAName());
        tB = db.getTeam(mg.getTeamBName());
        p1A = db.getPlayer(tA.getPlayer1());
        p2A = db.getPlayer(tA.getPlayer2());
        p1B = db.getPlayer(tB.getPlayer1());
        p2B = db.getPlayer(tB.getPlayer2());
    }

    void updateLocalStorage() {
        db.updateMatchGame(mg);
        db.updateTeam(tA);
        db.updateTeam(tB);
        db.updatePlayer(p1A);
        db.updatePlayer(p2A);
        db.updatePlayer(p1B);
        db.updatePlayer(p2B);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (localStorage) {
            updateLocalStorage();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_PLAYERA, playerTeamA);
        outState.putString(STATE_PLAYERB, playerTeamB);
        outState.putString(STATE_MATCH, currentMatch);
        outState.putString(STATE_TEAM, currentTeam);
        outState.putString(STATE_SET, currentSet);

        if (localStorage) {
            updateLocalStorage();
        }
        db.closeDB();
    }

    // <----------- ExternalStorage Server functions ----------->
    private  class MatchServer extends AsyncTask<String, Void, Void> {
        private int value;
        private String objectId;
        @Override
        protected Void doInBackground(String...inputStrings) {
            objectId = inputStrings[0];
            char objectType = inputStrings[1].charAt(0);
            int matchNum = Integer.parseInt(inputStrings[2]);
            String attribute = inputStrings[3];
            boolean modify = Boolean.parseBoolean(inputStrings[4]);

            if (modify) {
                setAttribute(objectType, matchNum, attribute, inputStrings);
            } else {
                getAttribute(objectType, matchNum, attribute, inputStrings);
            }
           return null;
        }

        private void setAttribute(char objectType, int matchNum, String attribute, String[] inputStrings) {
            value = -2;
            try {
                Socket s = new Socket("10.0.2.2", 9876);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                OutputStream out = s.getOutputStream();
                dos.writeChar(objectType);
                dos.writeInt(matchNum);
                dos.writeUTF(attribute);
                dos.writeBoolean(true);
                dos.writeInt(Integer.parseInt(inputStrings[5]));
                dos.writeInt(Integer.parseInt(inputStrings[6]));
                if (!attribute.equals("gameSets") && objectType != 'T') {
                    dos.writeInt(Integer.parseInt(inputStrings[7]));
                }
                out.close();
                s.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void getAttribute(char objectType, int matchNum, String attribute, String[] inputStrings) {
            try {
                Socket s = new Socket("10.0.2.2", 9876);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                DataInputStream dis = new DataInputStream((s.getInputStream()));
                OutputStream out = s.getOutputStream();
                dos.writeChar(objectType);
                dos.writeInt(matchNum);
                dos.writeUTF(attribute);
                dos.writeBoolean(false);
                if (!attribute.equals("date")) {
                    dos.writeInt(Integer.parseInt(inputStrings[5]));
                    if (!attribute.equals("gameSets") && objectType != 'T') {
                        dos.writeInt(Integer.parseInt(inputStrings[6]));
                    }
                }
                if (attribute.equals("date") || attribute.equals("name")) {
                } else {
                    value = dis.readInt();
                }
                out.close();
                s.close();
        } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                value = -2;
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            if (value != -2) {
                updateValue(value, objectId);
            }
        }
    }
}
