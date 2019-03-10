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
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.candidate_layout, null, true);

        }
        final Candidate candidate = getItem(position);


        Button btnCandidate = (Button) convertView.findViewById(R.id.btnCandidate);
        btnCandidate.setText(candidate.getName());
        btnCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView mList = ((ListView) parent);
                mList.performItemClick(v, position, position);
                mList.getChildAt(position - mList.getFirstVisiblePosition()).setSelected(true);
            }
        });

        ImageView imgCandidate = (ImageView) convertView.findViewById(R.id.imgCandidate);
        Drawable myDrawable = context.getResources().getDrawable(R.drawable.ic_action_name);
        if (candidate.getName().trim().equalsIgnoreCase("Richard E. Avena"))
            myDrawable = context.getResources().getDrawable(R.mipmap.bod1);
        else if (candidate.getName().trim().equalsIgnoreCase("Aurora E. Castillo"))
            myDrawable = context.getResources().getDrawable(R.mipmap.bod2);
        else if (candidate.getName().trim().equalsIgnoreCase("Aurelio A. Jose Jr"))
            myDrawable = context.getResources().getDrawable(R.mipmap.bod3);
        else if (candidate.getName().trim().equalsIgnoreCase("Lydia M. Mangubat"))
            myDrawable = context.getResources().getDrawable(R.mipmap.bod4);
        else if (candidate.getName().trim().equalsIgnoreCase("John Paul G. Villar"))
            myDrawable = context.getResources().getDrawable(R.mipmap.bod5);
        else if (candidate.getName().trim().equalsIgnoreCase("Merly J. Jacoba"))
            myDrawable = context.getResources().getDrawable(R.mipmap.audit1);
        else if (candidate.getName().trim().equalsIgnoreCase("Ruth D. Lim"))
            myDrawable = context.getResources().getDrawable(R.mipmap.audit2);
        else if (candidate.getName().trim().equalsIgnoreCase("Manuel G. Bautista"))
            myDrawable = context.getResources().getDrawable(R.mipmap.elec1);
        else if (candidate.getName().trim().equalsIgnoreCase("Michelle C. Cruz"))
            myDrawable = context.getResources().getDrawable(R.mipmap.elec2);
        else if (candidate.getName().trim().equalsIgnoreCase("Nancy P. Magno"))
            myDrawable = context.getResources().getDrawable(R.mipmap.elec3);

        imgCandidate.setImageDrawable(myDrawable);
        return convertView;
    }


}
