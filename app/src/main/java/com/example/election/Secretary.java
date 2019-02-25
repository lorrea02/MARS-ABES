package com.example.election;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class Secretary extends AppCompatActivity {

    ArrayList<Candidate> candidates;
    ListView lv;
    String data="";
    TextView tvDisplay;
    Button btnConfirm, btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        candidates = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lvCandidates);
        tvDisplay = findViewById(R.id.tvDisplay4);
        btnConfirm = findViewById(R.id.btnConfirm3);
        btnSkip = findViewById(R.id.btnSkip3);


        try {

            URL url = new URL("https://marsabesapi.000webhostapp.com/MARS-ABES/readSecretary.php");

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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Button btnCand = (Button) view.findViewById(R.id.btnCandidate);
                view.requestFocusFromTouch();
                tvDisplay.setText(btnCand.getText().toString());
            }
        });



        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvDisplay.getText().toString().equals("[Please Select a Candidate]"))
                    Toast.makeText(Secretary.this, "Please either select a candidate first or skip.", Toast.LENGTH_LONG).show();
                else {
                    Intent confirmationIntent = new Intent(Secretary.this, Confirmation.class);
                    Intent iin = getIntent();
                    Bundle b = iin.getExtras();
                    Bundle b2 = b.getBundle("bundle");
                    b2.putString("Secretary", tvDisplay.getText().toString());
                    confirmationIntent.putExtra("bundle", b2);
                    startActivity(confirmationIntent);
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent confirmationIntent = new Intent(Secretary.this, Confirmation.class);
                Intent iin = getIntent();
                Bundle b = iin.getExtras();
                Bundle b2 = b.getBundle("bundle");
                b2.putString("Secretary", "--");
                confirmationIntent.putExtra("bundle", b2);
                startActivity(confirmationIntent);
            }
        });
    }
}
