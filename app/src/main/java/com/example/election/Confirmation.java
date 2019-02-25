package com.example.election;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class Confirmation extends AppCompatActivity {
    TextView tvPresident, tvVice, tvSecretary, tvPos4, tvPos5;
    String memberName, memberAddress, memberID, voted, presidentName, vicePresidentName, secretaryName, data = "";
    Button btnSubmit;
    Messenger mMessenger = null;
    boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        tvPresident = findViewById(R.id.tvPresident);
        tvVice = findViewById(R.id.tvVice);
        tvSecretary = findViewById(R.id.tvSecretary);
        btnSubmit = findViewById(R.id.btnSubmit);


        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        Bundle b2 = b.getBundle("bundle");
        memberID = (String) b2.get("memberID");
        memberName = (String) b2.get("name");
        memberAddress = (String) b2.get("address");
        presidentName = (String) b2.get("President");
        vicePresidentName = (String) b2.get("Vice President");
        secretaryName = (String) b2.get("Secretary");


        tvPresident.setText(presidentName);
        tvVice.setText(vicePresidentName);
        tvSecretary.setText(secretaryName);

        bindService();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // check if voted
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("memberID", memberID);
                    jsonObject.put("position", "president");
                    jsonObject.put("name", presidentName);

                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("memberID", memberID);
                    jsonObject2.put("position", "vice president");
                    jsonObject2.put("name", vicePresidentName);

                    JSONObject jsonObject3 = new JSONObject();
                    jsonObject3.put("memberID", memberID);
                    jsonObject3.put("position", "secretary");
                    jsonObject3.put("name", secretaryName);

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(jsonObject);
                    jsonArray.put(jsonObject2);
                    jsonArray.put(jsonObject3);

                    URL url = new URL("https://marsabesapi.000webhostapp.com/MARS-ABES/postVote.php");

                    HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonArray.toString());
                    writer.flush();
                    writer.close();
                    os.close();

                    String reply;
                    InputStream in = httpURLConnection.getInputStream();
                    StringBuffer sb = new StringBuffer();
                    try {
                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }
                        reply = sb.toString();
                    } finally {
                        in.close();
                    }

                    reply = reply.trim();
                    Log.d("postvote", reply);
                    JSONObject json = new JSONObject(reply);
                    String response = json.getString("message");
                    if (response.equals("Thank you for voting")) {


                        //printing goes here!!!

                        Date today = Calendar.getInstance().getTime();
                        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                        String dateNow = formatter.format(today);
                        String headerTxt = centerTxt("SAN JOSE DELMONTE BULACAN") + "\n" +
                                centerTxt("CREDIT COOPERATIVE") + "\n\n" +
                                centerTxt("ELECTIONS 2019") + "\n\n" +
                                centerTxt("VOTING RECEIPT") + "\n" +
                                "________________________________" + "\n\n" +
                                "" + memberName + "\n" +
                                "Member ID: " + memberID + "\n" +
                                printAdd("Address: " + memberAddress) + "\n" +
                                "________________________________" + "\n\n";

                        String ballot = centerTxt("PRESIDENT") + "\n" +
                                centerTxt(presidentName) + "\n\n" +
                                centerTxt("VICE PRESIDENT") + "\n" +
                                centerTxt(vicePresidentName) + "\n\n" +
                                centerTxt("SECRETARY") + "\n" +
                                centerTxt(secretaryName) + "\n\n";


                        String footerTxt = centerTxt("________________________________") + "\n Date: " + dateNow + "\n\n\n\n";

                        if (isBind) {
                            ArrayList message = new ArrayList<>();
                            message.add("" + headerTxt + ballot + footerTxt);
                            Message msg = Message.obtain();
                            msg.obj = message;
                            try {
                                mMessenger.send(msg);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(Confirmation.this, "Bind muna", Toast.LENGTH_LONG).show();
                        }


                        Toast.makeText(Confirmation.this, "Thank you for voting! Your voting receipt will be printed.", Toast.LENGTH_LONG).show();
                        Intent backToMain = new Intent(Confirmation.this, MainActivity.class);
                        backToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(backToMain);
                        finish();
                    } else {
                        Toast.makeText(Confirmation.this, "There was a problem in submitting your vote.\nPlease check your connection and restart the app. \nThank you", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessenger = null;
            isBind = false;
        }
    };

    @Override
    protected void onStop() {
        unbindService(mConnection);
        isBind = false;
        mMessenger = null;
        super.onStop();
    }

    public void bindService() {
        Intent intent = new Intent(Confirmation.this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public String centerTxt(String txt) {

        if (txt.length() < 32) {
            int spaces = 32 - txt.length();
            int firstHalf = spaces / 2;
            int secondHalf = spaces - firstHalf;
            for (int i = 0; i < firstHalf; i++) {
                txt = " " + txt;
            }
            for (int i = 0; i < secondHalf; i++) {
                txt = txt + " ";
            }
        }
        return txt;
    }

    public String printAdd(String txt) {
        Log.d("address", txt);
        if (txt.length() > 32) {
            int rows = txt.length() / 32;
            if(txt.length() % 32 != 0)
                rows++;
            String ret = "";

            for (int i = 0, ind = 0; i < rows; i++, ind += 32) {
                if (ind + 32 > txt.length())
                    ret += txt.substring(ind, txt.length()) + "\n";
                else
                    ret += txt.substring(ind, ind + 32) + "\n";
                Log.d("ret", ret);
            }
            return ret;
        } else
            return txt;

        //aerrol
        //0,4
        //
    }
}
