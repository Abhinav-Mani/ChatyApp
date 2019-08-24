package com.mani.chatyapp;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.SingleContactHolder> {
    void add(String s)
    {
        if(!names.contains(s)) {
            names.add(s);
            notifyItemInserted(names.size() - 1);
        }
    }

    @NonNull
    ArrayList<String> names=new ArrayList<>();
    ContactListAdapter(ArrayList<String> names)
    {
        this.names=names;
    }
    @Override
    public SingleContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contacts_single,viewGroup,false);
        SingleContactHolder singleContactHolder=new SingleContactHolder(view);
        return singleContactHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final SingleContactHolder singleContactHolder, int i) {
        singleContactHolder.textView.setText(names.get(i));

        final int j=i;
        singleContactHolder.single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ak47",MainActivity.currentchatperson+"<-set prev");
                Log.e("ak47", "onClick: "+names.get(j));
                MainActivity.currentchatperson=names.get(j);
                Log.e("ak47",MainActivity.currentchatperson);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ChatBox chatBox=new ChatBox();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,chatBox).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class SingleContactHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        RelativeLayout single;
        public SingleContactHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.nameofcontact);
            single=itemView.findViewById(R.id.contact_list_single);
        }
    }
}
