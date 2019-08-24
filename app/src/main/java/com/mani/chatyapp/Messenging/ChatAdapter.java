package com.mani.chatyapp.Messenging;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mani.chatyapp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.SingleMessege>{
    ArrayList<Messege> arrayList=new ArrayList<>();
    public void add(Messege m)
    {
        if(!arrayList.contains(m))
        {
            arrayList.add(m);
            Collections.sort(arrayList);
            notifyDataSetChanged();

        }
    }
    public ChatAdapter(ArrayList<Messege> list)
    {
        this.arrayList=list;
    }
    @NonNull
    @Override
    public SingleMessege onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_messege,viewGroup,false);
        SingleMessege singleMessege=new SingleMessege(view);
        return singleMessege;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleMessege singleMessege, int i) {
        if(arrayList.get(i).sent)
        {
            singleMessege.rec.setVisibility(View.GONE);
            singleMessege.sent.setText(arrayList.get(i).messege);
        }
        else
        {
            singleMessege.sent.setVisibility(View.GONE);
            singleMessege.rec.setText(arrayList.get(i).messege);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SingleMessege extends RecyclerView.ViewHolder
    {
        TextView sent,rec;
        public SingleMessege(@NonNull View itemView) {
            super(itemView);
            sent=itemView.findViewById(R.id.sentmess);
            rec=itemView.findViewById(R.id.resmes);
        }
    }
}
