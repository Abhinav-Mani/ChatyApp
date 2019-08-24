package com.mani.chatyapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;


public class SignUp extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth mAuth;
    EditText email,password;
    TextView tv;
    Button button;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SignUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUp newInstance(String param1, String param2) {
        SignUp fragment = new SignUp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mAuth=FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        email=view.findViewById(R.id.email);
        password=view.findViewById(R.id.password);
        tv=view.findViewById(R.id.gotosignin);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                SignIn signIn=new SignIn();
                fragmentTransaction.replace(R.id.frame,signIn);
                fragmentTransaction.commit();
            }
        });

        button=view.findViewById(R.id.signup);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ak47", "onClick: ");
                String Email=email.getText().toString().trim();
                String Password=password.getText().toString().trim();
                if(TextUtils.isEmpty(Email))
                {
                    Toast.makeText(getContext(),"Email Required",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(Password))
                {
                    Toast.makeText(getContext(),"Password Required",Toast.LENGTH_LONG).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("users");
                                Toast.makeText(getContext(), "SignUp Successfull", Toast.LENGTH_LONG).show();
                                Long tsLong = System.currentTimeMillis()/1000;
                                String ts = tsLong.toString();
                                myRef.child(mAuth.getCurrentUser().getEmail().substring(0,mAuth.getCurrentUser().getEmail().indexOf('@'))).child("TOJ").setValue(ts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getContext(), "Write Successfull", Toast.LENGTH_LONG).show();
                                            FragmentManager fragmentManager=getFragmentManager();
                                            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                                            ChatList chatList=new ChatList();
                                            fragmentTransaction.replace(R.id.frame,chatList);
                                            fragmentTransaction.commit();
                                        }
                                    }
                                });



                            } else {
                                Toast.makeText(getContext(), "SignUP unsuccessfull", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        return view;
    }


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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
