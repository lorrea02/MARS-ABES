package com.example.election;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class Voting extends AppCompatActivity {

    ArrayList<Candidate> candidates;
    ListView lv;
    String data="";

    TextView tvSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        tvSelected = findViewById(R.id.tvSelected);


        candidates = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lvCandidates);

        try {
            // check if voted
            // You have already voted!

            URL url = new URL("https://marsabesapi.000webhostapp.com/MARS-ABES/readCandidates.php");

            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
            data = data.trim();
            JSONObject retJson = new JSONObject(data);
            JSONArray jsonArray = retJson.getJSONArray("records");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject candidateItem = jsonArray.getJSONObject(i);
                candidates.add(new Candidate(
                        Integer.parseInt(candidateItem.getString("candidateID")),
                        candidateItem.getString("name"),
                        candidateItem.getString("position"),
                        Integer.parseInt(candidateItem.getString("currentVotes"))
                ));
            }




        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomListAdapter adapter = new CustomListAdapter(
                getApplicationContext(), R.layout.candidate_layout, candidates
        );

        lv.setAdapter(adapter);

    }
}
