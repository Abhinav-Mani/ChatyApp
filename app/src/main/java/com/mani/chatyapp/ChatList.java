package com.mani.chatyapp;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import java.nio.BufferUnderflowException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button signout;
    private OnFragmentInteractionListener mListener;

    public ChatList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatList.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatList newInstance(String param1, String param2) {
        ChatList fragment = new ChatList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_chat_list, container, false);
        FloatingActionButton addContact=v.findViewById(R.id.addcontacts);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.add_contact);
                final EditText username=dialog.findViewById(R.id.username);
                Button add=dialog.findViewById(R.id.adduser);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String user=username.getText().toString().trim();
                        dialog.dismiss();
                        firebaseDatabase.getReference("users").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("TOJ")){
                                    String email=mAuth.getCurrentUser().getEmail();
                                    String currentuser=email.substring(0,email.indexOf("@"));
                                    Long tsLong = System.currentTimeMillis()/1000;
                                    String ts = tsLong.toString();
                                    DatabaseReference mref=firebaseDatabase.getReference("users").child(user).child("Contacts");
                                    mref.child(currentuser).setValue(ts);
                                    mref=firebaseDatabase.getReference("users").child(currentuser).child("Contacts");
                                    mref.child(user).setValue(ts);
                                }
                                else
                                    Toast.makeText(getContext(),user+" doesn't Exists",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });
                dialog.show();
            }
        });
        RecyclerView recyclerView=v.findViewById(R.id.contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<String> names=new ArrayList<String>();

        //names.add("to be added soon");
        final ContactListAdapter contactListAdapter=new ContactListAdapter(names);
        recyclerView.setAdapter(contactListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        String st=mAuth.getCurrentUser().getEmail().toString();
        Log.e("ak47","started"+st.substring(0,st.indexOf("@")));
        DatabaseReference databaseReference=firebaseDatabase.getReference("users").child(st.substring(0,st.indexOf("@"))).child("Contacts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){


                    Log.e("ak47",dataSnapshot1.getKey());
                    contactListAdapter.add(dataSnapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
