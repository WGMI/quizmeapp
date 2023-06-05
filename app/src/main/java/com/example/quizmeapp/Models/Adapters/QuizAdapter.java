package com.example.quizmeapp.Models.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmeapp.Fragments.QuizFragment;
import com.example.quizmeapp.MainActivity;
import com.example.quizmeapp.Models.Quiz;
import com.example.quizmeapp.R;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    Context context;
    List<Quiz> quizzes;

    public QuizAdapter(Context context, List<Quiz> quizzes) {
        this.context = context;
        this.quizzes = quizzes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_quiz,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz quiz = quizzes.get(position);
        holder.name.setText(quiz.getName());
        holder.date.setText(quiz.getDate());
        holder.desc.setText(quiz.getDescription());
        double score = ((double) quiz.getScore()/quiz.getQuestionCount()) * 100;//((quiz.getScore() / quiz.getQuestionCount()) * 100);
        Log.d("TAG", score +"");
        /*String scoreStrig = (quiz.getScore() > 0) ? ((quiz.getScore() / quiz.getQuestionCount()) * 100) + "" : "New";
        */
        holder.progress.setText(score+"%");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("id",quiz.getId());
                ((MainActivity)context).load(new QuizFragment(),bundle,R.id.home);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView date,name,desc,progress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layout = itemView.findViewById(R.id.layout);
            this.date = itemView.findViewById(R.id.date);
            this.name = itemView.findViewById(R.id.name);
            this.desc = itemView.findViewById(R.id.desc);
            this.progress = itemView.findViewById(R.id.progress);
        }
    }
}

