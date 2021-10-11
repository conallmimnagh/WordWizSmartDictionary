package com.example.mltextreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Worddef> savedWord = new ArrayList<>();

    public RecyclerViewAdapter() {

    }

    public RecyclerViewAdapter(ArrayList<Worddef> savedWord) {
        this.savedWord = savedWord;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_word_card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView word = holder.word;
        TextView wordDef = holder.definition;

        word.setText(savedWord.get(position).getWord());
        wordDef.setText(savedWord.get(position).getDef());
    }

    @Override
    public int getItemCount() {
        return savedWord.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView word;
        TextView definition;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.word = (TextView)itemView.findViewById(R.id.word);
            this.definition = (TextView)itemView.findViewById(R.id.wordDef);
        }
    }
}
