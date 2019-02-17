package com.example.election;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FetchData extends AppCompatActivity {
    TextView tvName, tvMemberID, tvAddress, tvError;
    String name, memberID, address, voterID, voted, data = "";
    Button voteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        tvError = findViewById(R.id.tvError);
        tvName = findViewById(R.id.tvName);
        tvMemberID = findViewById(R.id.tvMemberID);
        tvAddress = findViewById(R.id.tvAddress);
        voteBtn = findViewById(R.id.voteBtn);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            voterID = (String) b.get("voterID");
        }
        try {
            // check if voted


            URL url = new URL("https://marsabesapi.000webhostapp.com/MARS-ABES/readone.php?voterID=" + voterID);

            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
            data = data.trim();
            JSONObject json = new JSONObject(data);
            name = json.getString("name");
            address = json.getString("address");
            memberID = json.getString("memberID");
            voted = json.getString("voted");

            tvName.setText(name);
            tvAddress.setText(address);
            tvMemberID.setText(memberID);




            voteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(voted.equals("n")){
                        tvError.setText("");
                        Intent votingIntent = new Intent(FetchData.this, Voting.class);
                        votingIntent.putExtra("voterID", voterID);
                        votingIntent.putExtra("name", name);
                        votingIntent.putExtra("address", address);
                        votingIntent.putExtra("memberID", memberID);
                        startActivity(votingIntent);
                    }else{
                        tvError.setText("You have already voted!");
                    }
                }
            });




        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
