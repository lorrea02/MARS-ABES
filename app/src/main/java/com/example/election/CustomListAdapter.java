package com.example.election;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Candidate> {


    ArrayList<Candidate> candidates;
    Context context;
    int resource;



    public CustomListAdapter(Context context, int resource, ArrayList<Candidate> candidates) {
        super(context, resource, candidates);
        this.candidates = candidates;
        this.context = context;
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.candidate_layout, null, true);

        }
        Candidate candidate = getItem(position);


        Button btnCandidate = (Button) convertView.findViewById(R.id.btnCandidate);
        btnCandidate.setText(candidate.getName());

        ImageView imgCandidate = (ImageView) convertView.findViewById(R.id.imgCandidate);
        Drawable myDrawable = context.getResources().getDrawable(R.drawable.ic_action_name);
        imgCandidate.setImageDrawable(myDrawable);



        return convertView;
    }
}
