package com.example.quizmeapp.Fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.quizmeapp.MainActivity;
import com.example.quizmeapp.Models.Question;
import com.example.quizmeapp.Models.Quiz;
import com.example.quizmeapp.R;
import com.example.quizmeapp.Utils.Helper;
import com.reactiveandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context context;
    Quiz quiz;
    TextView counter,correct,question,choice1,choice2,choice3,choice4;

    List<Question> questions;
    Question currentQuestion;

    int score,count,questionCounter;
    boolean answered;

    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Quiz.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();
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
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        context = getActivity();
        Bundle bundle = getArguments();
        quiz = Select.from(Quiz.class).where("id=?",bundle.getLong("id")).fetchSingle();
        questions = listQuestions(quiz.getQuestionJSON());
        count = questions.size();

        counter = view.findViewById(R.id.counter);
        correct = view.findViewById(R.id.correct);
        question = view.findViewById(R.id.question);
        choice1 = view.findViewById(R.id.choice1);
        choice2 = view.findViewById(R.id.choice2);
        choice3 = view.findViewById(R.id.choice3);
        choice4 = view.findViewById(R.id.choice4);

        nextQuestion();
        return view;
    }

    private List<Question> listQuestions(String questionJSON) {
        List<Question> qs = new ArrayList<>();
        try {
            JSONArray questionsArray = new JSONArray(questionJSON);
            for(int i=0;i<questionsArray.length();i++){
                JSONObject o = questionsArray.getJSONObject(i);
                JSONArray optionArr = o.getJSONArray("O");

                String[] options = new String[optionArr.length()];
                for (int x=0;x<optionArr.length();x++) {
                    options[x] = optionArr.getString(x);
                }

                Question q = new Question(o.getString("Q"),options,o.getInt("A"));
                qs.add(q);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return qs;
    }

    private void nextQuestion() {
        choice1.setTextColor(Color.parseColor("#000000"));
        choice2.setTextColor(Color.parseColor("#000000"));
        choice3.setTextColor(Color.parseColor("#000000"));
        choice4.setTextColor(Color.parseColor("#000000"));

        if(questionCounter < count){
            currentQuestion = questions.get(questionCounter);

            question.setText(currentQuestion.getQuestion());
            choice1.setText(currentQuestion.getOptionArray()[0]);
            choice2.setText(currentQuestion.getOptionArray()[1]);
            choice3.setText(currentQuestion.getOptionArray()[2]);
            choice4.setText(currentQuestion.getOptionArray()[3]);

            choice1.setOnClickListener(answer);
            choice2.setOnClickListener(answer);
            choice3.setOnClickListener(answer);
            choice4.setOnClickListener(answer);

            questionCounter++;
            counter.setText(questionCounter + "/" + count);
            answered = false;
        }else{
            finishQuiz();
        }
    }

    View.OnClickListener answer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int answerIndex = -1;
            switch (v.getId()){
                case R.id.choice1:
                    answerIndex = 0;
                    break;
                case R.id.choice2:
                    answerIndex = 1;
                    break;
                case R.id.choice3:
                    answerIndex = 2;
                    break;
                case R.id.choice4:
                    answerIndex = 3;
                    break;
            }

            if(!answered){
                checkAnswer(answerIndex);
            }else{
                nextQuestion();
            }
        }
    };

    private void checkAnswer(int index) {
        Log.d("TAG", "checkAnswer: " + questionCounter);
        answered = true;

        choice1.setTextColor(Color.RED);
        choice2.setTextColor(Color.RED);
        choice3.setTextColor(Color.RED);
        choice4.setTextColor(Color.RED);

        switch (currentQuestion.getAnswer()){
            case 0:
                choice1.setTextColor(Color.GREEN);
                break;
            case 1:
                choice2.setTextColor(Color.GREEN);
                break;
            case 2:
                choice3.setTextColor(Color.GREEN);
                break;
            case 3:
                choice4.setTextColor(Color.GREEN);
                break;
        }

        if(index == currentQuestion.getAnswer()){
            score++;
            showAnimation(true);
        }else{
            showAnimation(false);
        }


    }

    private void showAnimation(boolean iscorrect) {
        if(iscorrect){
            correct.setText("Correct");
            correct.setBackground(context.getDrawable(R.drawable.simple_rounded_bg_green));
        }else{
            correct.setText("Incorrect");
            correct.setBackground(context.getDrawable(R.drawable.simple_rounded_bg_red));
        }
        correct.setVisibility(View.VISIBLE);
        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(correct, "alpha", 0f, 1f);
        fadeInAnimator.setDuration(1000);
        fadeInAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                correct.setVisibility(View.GONE);

                if(questionCounter < count){
                    nextQuestion();
                }else{
                    Helper.showSuccessDialog(context,"Good Job", "Finished Quiz", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((MainActivity)context).load(new Home(),null,0);
                        }
                    });
                }
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
        fadeInAnimator.start();
    }

    private void finishQuiz() {
        ((MainActivity)context).load(new Home(),null,0);
    }
}