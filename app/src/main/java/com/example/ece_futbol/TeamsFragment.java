package com.example.ece_futbol;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamsFragment extends Fragment {
    private static final String ARG_MATCH_NUM = "matchNum";
//    private static String currentMatch;

    private String currentMatch;

    private OnFragmentInteractionListener mListener;

    private TextView nameTeamA;
    private TextView nameTeamB;
    private TextView fourHitsTeamA;
    private TextView fourHitsTeamB;
    private TextView serviceOrderTeamB;
    private TextView serviceOrderTeamA;

    private GraphView graph;

    private MatchServer matchServer;

    public TeamsFragment() {
        // Required empty public constructor
    }

    public static TeamsFragment newInstance(String param1) {
        TeamsFragment fragment = new TeamsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MATCH_NUM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentMatch = getArguments().getString(ARG_MATCH_NUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_teams, container, false);
       nameTeamA = view.findViewById(R.id.teamAName);
       nameTeamB = view.findViewById(R.id.teamBName);
       fourHitsTeamA = view.findViewById(R.id.fourHitsTeamA);
       fourHitsTeamB = view.findViewById(R.id.fourHitsTeamB);
       serviceOrderTeamA = view.findViewById(R.id.serviceOrderTeamA);
       serviceOrderTeamB = view.findViewById(R.id.serviceOrderTeamB);

       graph = (GraphView) view.findViewById(R.id.graph);
       graph.setVisibility(View.VISIBLE);

       for (int i = 0; i < 2; i++) {
           char teamChar = (i == 0) ? 'A' : 'B';
           initMatchServer("nameTeam" + teamChar, "T", currentMatch, "name", Boolean.toString(false), Integer.toString(i));
           initMatchServer("fourHitsTeam"  + teamChar, "T", currentMatch, "fourHits", Boolean.toString(false), Integer.toString(i));
           initMatchServer("serviceOrderTeam" + teamChar, "T", currentMatch, "serviceOrder", Boolean.toString(false), Integer.toString(i));
       }
       initMatchServer("", "M", currentMatch, "allPoints", Boolean.toString(false));

        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    private void initMatchServer(String...inputString) {
        matchServer = new TeamsFragment.MatchServer();
        matchServer.execute(inputString);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction();
    }

    private void setGraph(int[] scores) {
        try {
            LineGraphSeries < DataPoint > teamA = new LineGraphSeries< >(new DataPoint[] {
                    new DataPoint(0, 0),
                    new DataPoint(1, scores[0]),
                    new DataPoint(2, scores[1]),
                    new DataPoint(3, scores[2]),
                    new DataPoint(4, scores[3]),
                    new DataPoint(5, scores[4])
            });
            LineGraphSeries < DataPoint > teamB = new LineGraphSeries< >(new DataPoint[] {
                    new DataPoint(0, 0),
                    new DataPoint(1, scores[5]),
                    new DataPoint(2, scores[6]),
                    new DataPoint(3, scores[7]),
                    new DataPoint(4, scores[8]),
                    new DataPoint(5, scores[9]),
            });
            teamA.setTitle("TeamA");
            teamB.setTitle("TeamB");
            teamA.setColor(getResources().getColor(R.color.blue));
            teamB.setColor(getResources().getColor(R.color.red));
            graph.addSeries(teamA);
            graph.addSeries(teamB);
            graph.getViewport().setMaxX(5);
            graph.setTitle("Points per Set");
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            graph.getGridLabelRenderer().setHorizontalAxisTitle("Sets");
            graph.getGridLabelRenderer().setVerticalAxisTitle("Points");

        } catch (IllegalArgumentException e) {
//            Toast.makeText(TeamsFragment.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateValue(int value, String objectId) {
        if (objectId.equals("fourHitsTeamA")) {
            fourHitsTeamA.setText(Integer.toString(value));
        } else if (objectId.equals("serviceOrderTeamA")) {
            serviceOrderTeamA.setText(Integer.toString(value));
        } else if (objectId.equals("fourHitsTeamB")) {
            fourHitsTeamB.setText(Integer.toString(value));
        } else if (objectId.equals("serviceOrderTeamB")) {
            serviceOrderTeamB.setText(Integer.toString(value));
        }
    }

    private void updateValue(String value, String objectId) {
        if (objectId.equals("nameTeamA")) {
            nameTeamA.setText(value);
        } else if (objectId.equals("nameTeamB")) {
            nameTeamB.setText(value);
        }
    }

    private  class MatchServer extends AsyncTask<String, Void, Void> {
        private int value;
        private String name;
        private String objectId;
        private String attribute;
        private int scores[];

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

                if (attribute.equals("name")) {
                    dos.writeInt(Integer.parseInt(inputStrings[5]));
                    name = dis.readUTF();
                } else if (attribute.equals("fourHits") || attribute.equals("serviceOrder")) {
                    dos.writeInt(Integer.parseInt(inputStrings[5]));
                    value = dis.readInt();
                } else {
                    scores = new int[]{dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt(),
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
            } else if (attribute.equals("allPoints")) {
                setGraph(scores);
            } else {
                updateValue(value, objectId);
            }
        }
    }
}
