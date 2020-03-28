package com.example.ece_futbol;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class PlayersFragment extends Fragment {
    private static final String ARG_MATCH_NUM = "matchNum";
    private static final String ARG_LOCAL_STORAGE = "localStorage";
    private String currentMatch;
    private boolean localStorage;

    private TextView player1TeamAName;
    private TextView player2TeamAName;
    private TextView player1TeamBName;
    private TextView player2TeamBName;
    private ImageView winP1TA;
    private ImageView winP2TA;
    private ImageView winP1TB;
    private ImageView winP2TB;

    private BarChart teamABarChart;
    private BarChart teamBBarChart;

    private OnFragmentInteractionListener mListener;

    private MatchServer matchServer;

    private DatabaseHelper db;
    private Player p1A;
    private Player p2A;
    private Player p1B;
    private Player p2B;

    public PlayersFragment() {
        // Required empty public constructor
    }

    public static PlayersFragment newInstance(String param1, boolean param2) {
        PlayersFragment fragment = new PlayersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MATCH_NUM, param1);
        args.putBoolean(ARG_LOCAL_STORAGE, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentMatch = getArguments().getString(ARG_MATCH_NUM);
            localStorage = getArguments().getBoolean(ARG_LOCAL_STORAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_players, container, false);

        player1TeamAName = view.findViewById(R.id.player1TeamAName);
        player2TeamAName = view.findViewById(R.id.player2TeamAName);
        player1TeamBName = view.findViewById(R.id.player1TeamBName);
        player2TeamBName = view.findViewById(R.id.player2TeamBName);
        winP1TA = view.findViewById(R.id.winP1TA);
        winP2TA = view.findViewById(R.id.winP2TA);
        winP1TB = view.findViewById(R.id.winP1TB);
        winP2TB = view.findViewById(R.id.winP2TB);
        teamABarChart = (BarChart) view.findViewById(R.id.teamABarChart);
        teamBBarChart = (BarChart) view.findViewById(R.id.teamBBarChart);

        db = new DatabaseHelper(getActivity().getApplicationContext());
        initLocalDatabase();

        for (int i = 0; i < 2; i++) {
            initMatchServer(Integer.toString(i), "T", currentMatch, "allFaults", Boolean.toString(false),Integer.toString(i));
            initMatchServer(Integer.toString(i), "T", currentMatch, "bestPlayer", Boolean.toString(false),Integer.toString(i));
            initMatchServer(i + "0", "P", currentMatch, "name", Boolean.toString(false), Integer.toString(i), "0");
            initMatchServer(i + "1", "P", currentMatch, "name", Boolean.toString(false), Integer.toString(i), "1");
        }
        return view;
    }

    private void initMatchServer(String...inputString) {
        if (localStorage) {
            getLocalValue(inputString[0], inputString[3]);
        } else {
            matchServer = new PlayersFragment.MatchServer();
            matchServer.execute(inputString);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setGraph(int[] values, String objectId) {
        List<BarEntry> bargroup1 = new ArrayList<>();
        bargroup1.add(new BarEntry(values[0], 0));
        bargroup1.add(new BarEntry(values[1], 1));
        bargroup1.add(new BarEntry(values[2], 2));
        bargroup1.add(new BarEntry(values[3], 3));
        bargroup1.add(new BarEntry(values[4], 4));

        List<BarEntry> bargroup2 = new ArrayList<>();
        bargroup2.add(new BarEntry(values[5], 0));
        bargroup2.add(new BarEntry(values[6], 1));
        bargroup2.add(new BarEntry(values[7], 2));
        bargroup2.add(new BarEntry(values[8], 3));
        bargroup2.add(new BarEntry(values[9], 4));

        BarDataSet barDataSet1 = new BarDataSet(bargroup1, "Player 1");
//barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

// creating dataset for Bar Group 2
        BarDataSet barDataSet2 = new BarDataSet(bargroup2, "Player 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        List<String> labels = new ArrayList<String>();
        labels.add(getResources().getString(R.string.assistedHit));
        labels.add(getResources().getString(R.string.doubleContact));
        labels.add(getResources().getString(R.string.catchLift));
        labels.add(getResources().getString(R.string.foot));
        labels.add(getResources().getString(R.string.netTouch));

        List<IBarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        BarData data = new BarData(labels, dataSets);
        if (objectId.equals("0")) {
            teamABarChart.setData(data);
            teamABarChart.invalidate();
            teamABarChart.refreshDrawableState();
        } else if (objectId.equals("1")) {
           teamBBarChart.setData(data);
           teamBBarChart.invalidate();
           teamBBarChart.refreshDrawableState();
        }
    }

    public interface OnFragmentInteractionListener {}

    private void updateValue(String value, String objectId) {
        if (objectId.equals("00")) {
            player1TeamAName.setText(value);
        } else if (objectId.equals("01")) {
            player2TeamAName.setText(value);
        } else if (objectId.equals("10")) {
            player1TeamBName.setText(value);
        } else if (objectId.equals("11")) {
            player2TeamBName.setText(value);
        }
    }

    private void setCrown(int value, String objectId) {
        if (objectId.equals("0") && value == 1) {
            winP1TA.setImageDrawable(null);
        } else if (objectId.equals("0") && value == 0) {
            winP2TA.setImageDrawable(null);
        } else if (objectId.equals("1") && value == 1) {
            winP1TB.setImageDrawable(null);
        } else if (objectId.equals("1") && value == 0) {
            winP2TB.setImageDrawable(null);
        }
    }

    // <----------- LocalStorage functions ----------->
    private void initLocalDatabase() {
        if (!localStorage) {
            return;
        }
        MatchGame mg = db.getMatchGame(Integer.parseInt(currentMatch));
        Team tA = db.getTeam(mg.getTeamAName());
        Team tB = db.getTeam(mg.getTeamBName());
        p1A = db.getPlayer(tA.getPlayer1());
        p2A = db.getPlayer(tA.getPlayer2());
        p1B = db.getPlayer(tB.getPlayer1());
        p2B = db.getPlayer(tB.getPlayer2());
    }

    private int getBestPlayer(Player p1, Player p2){
        int winp1 = p1.getAssistedHit() + p1.getDoubleContact() + p1.getCatchLift() + p1.getFoot() + p1.getNetTouch();
        int winp2 = p2.getAssistedHit() + p2.getDoubleContact() + p2.getCatchLift() + p2.getFoot() + p2.getNetTouch();
        return (winp1 <= winp2) ? 0 : 1;
    }

    private void getLocalValue(String objectId, String attribute) {
        if (objectId.equals("00")) {
            player1TeamAName.setText(p1A.getName());
        } else if (objectId.equals("01")) {
            player2TeamAName.setText(p2A.getName());
        } else if (objectId.equals("10")) {
            player1TeamBName.setText(p1B.getName());
        } else if (objectId.equals("11")) {
            player2TeamBName.setText(p2B.getName());
        } else if (objectId.equals("0") && attribute.equals("bestPlayer")){
            setCrown(getBestPlayer(p1A, p2A), objectId);
        } else if (objectId.equals("1") && attribute.equals("bestPlayer")){
            setCrown(getBestPlayer(p1B, p2B), objectId);
        } else if (objectId.equals("0") && attribute.equals("allFaults")){
            int scores[] = new int []{p1A.getAssistedHit(), p1A.getDoubleContact(), p1A.getCatchLift(),
            p1A.getFoot(), p1A.getNetTouch(), p2A.getAssistedHit(), p2A.getDoubleContact(),
            p2A.getCatchLift(), p2A.getFoot(), p2A.getNetTouch()};
            setGraph(scores, objectId);
        } else if (objectId.equals("1") && attribute.equals("allFaults")){
            int scores[] = new int []{p1B.getAssistedHit(), p1B.getDoubleContact(), p1B.getCatchLift(),
                    p1B.getFoot(), p1B.getNetTouch(), p2B.getAssistedHit(), p2B.getDoubleContact(),
                    p2B.getCatchLift(), p2B.getFoot(), p2B.getNetTouch()};
            setGraph(scores, objectId);
        }
    }

    // <----------- ExternalStorage Server functions ----------->
    private class MatchServer extends AsyncTask<String, Void, Void> {
        private int value;
        private String name;
        private String objectId;
        private String attribute;
        private int values[];

        @Override
        protected Void doInBackground(String... inputStrings) {
            objectId = inputStrings[0];
            char objectType = inputStrings[1].charAt(0);
            int matchNum = Integer.parseInt(inputStrings[2]);
            attribute = inputStrings[3];
            getAttribute(objectType, matchNum, inputStrings);
            return null;
        }

        private void getAttribute(char objectType, int matchNum, String[] inputStrings) {
            try {
                Socket s = new Socket("10.0.2.2", 9876);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                DataInputStream dis = new DataInputStream((s.getInputStream()));
                OutputStream out = s.getOutputStream();
                dos.writeChar(objectType);
                dos.writeInt(matchNum);
                dos.writeUTF(attribute);
                dos.writeBoolean(false);
                dos.writeInt(Integer.parseInt(inputStrings[5]));

                if (attribute.equals("name")) {
                    dos.writeInt(Integer.parseInt(inputStrings[6]));
                    name = dis.readUTF();
                } else if (attribute.equals("bestPlayer")) {
                    value = dis.readInt();
                } else if (attribute.equals("allFaults")) {
                    values = new int[]{dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt(),
                            dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt()};
                }
                out.close();
                s.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                value = 0;
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            if (attribute.equals("name")) {
                updateValue(name, objectId);
            } else if (attribute.equals("allFaults")) {
                setGraph(values, objectId);
            } else if (attribute.equals("bestPlayer")) {
                setCrown(value, objectId);
            }
        }
    }
}
