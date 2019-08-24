package com.mani.chatyapp;


import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mani.chatyapp.Messenging.ChatAdapter;
import com.mani.chatyapp.Messenging.Messege;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatBox extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference2;
    FirebaseAuth mAuth;
    public ChatBox() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth=FirebaseAuth.getInstance();
        View view=inflater.inflate(R.layout.fragment_chat_box, container, false);
        RecyclerView recyclerView=view.findViewById(R.id.chatbox);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final ArrayList<Messege> list=new ArrayList<Messege>();
        //list.add(new Messege("this is sent messege",100000,true));
        //list.add(new Messege("this is recived messege",10001,false));
        final ChatAdapter adapter=new ChatAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        String email=mAuth.getCurrentUser().getEmail().toString();
        final String loginuser=email.substring(0,email.indexOf('@'));
        final String currentchat=MainActivity.currentchatperson;
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users").child(loginuser).child("Messege").child(currentchat);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Messege messege=new Messege(dataSnapshot1.getValue().toString(),Long.valueOf(dataSnapshot1.getKey()),true);
                    adapter.add(messege);
                    Log.d("ak47",dataSnapshot1.getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"SOMETHING WENT WRONG",Toast.LENGTH_LONG).show();
                Log.e("ak47", "onCancelled: " );

            }
        });
        databaseReference2=firebaseDatabase.getReference("users").child(currentchat).child("Messege").child(loginuser);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Messege messege=new Messege(dataSnapshot1.getValue().toString(),Long.valueOf(dataSnapshot1.getKey()),false);
                    adapter.add(messege);
                    Log.d("ak47",dataSnapshot1.getValue().toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"SOMETHING WENT WRONG",Toast.LENGTH_LONG).show();
                Log.e("ak47", "onCancelled: " );
            }
        });
        Button button=view.findViewById(R.id.sendMessege);
        final EditText editText=view.findViewById(R.id.MessgeContent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mess=editText.getText().toString().trim();
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                firebaseDatabase.getReference("users").child(loginuser).child("Messege").child(currentchat).child(ts).setValue(mess);
            }
        });
        return view;
    }

}
