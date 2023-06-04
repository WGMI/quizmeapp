package com.example.quizmeapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmeapp.MainActivity;
import com.example.quizmeapp.Models.Adapters.QuizAdapter;
import com.example.quizmeapp.Models.Quiz;
import com.example.quizmeapp.R;
import com.example.quizmeapp.Utils.Helper;
import com.reactiveandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    Context context;
    LinearLayout cont_layout;
    TextView home_prompt,continue_progress,cont_name,cont_desc,cont_number;
    RecyclerView quizzes;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        context = getActivity();
        quizzes = view.findViewById(R.id.quizzes);

        home_prompt = view.findViewById(R.id.home_prompt);
        continue_progress = view.findViewById(R.id.continue_progress);
        cont_name = view.findViewById(R.id.cont_name);
        cont_desc = view.findViewById(R.id.cont_desc);
        cont_number = view.findViewById(R.id.cont_number);

        cont_layout = view.findViewById(R.id.cont_layout);
        cont_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                //((MainActivity)context).load(new Quiz,bundle,R.id.home);
            }
        });

        promptFirstQuiz();

        view.findViewById(R.id.new_quiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).load(new NewQuiz(),null,R.id.home);
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        quizzes.setLayoutManager(manager);
        quizzes.setAdapter(new QuizAdapter(context,getQuizzes()));

        return view;
    }

    private void promptFirstQuiz() {
        int quizCount = Select.from(Quiz.class).count();
        if(quizCount == 0){
            cont_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    String sample = Helper.getSample();
                    bundle.putString("sample",sample);
                    ((MainActivity)context).load(new NewQuiz(),bundle,R.id.home);
                }
            });
        }else{
            Quiz quiz = Select.from(Quiz.class).orderBy("id desc").fetchSingle();//Fetch last
            home_prompt.setText("Continue your last quiz");
            cont_name.setText(quiz.getName());
            cont_desc.setText(quiz.getDescription());
            cont_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("id",quiz.getId());
                    ((MainActivity)context).load(new QuizFragment(),bundle,R.id.home);
                }
            });
        }
    }

    private List<Quiz> getQuizzes() {
        return Select.from(Quiz.class).fetch();
    }
}