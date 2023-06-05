package com.example.quizmeapp.Fragments;

import static com.example.quizmeapp.MainActivity.JSON;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.quizmeapp.MainActivity;
import com.example.quizmeapp.Models.Quiz;
import com.example.quizmeapp.R;
import com.example.quizmeapp.Utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewQuiz#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewQuiz extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context context;
    EditText article;
    String articleText;

    public NewQuiz() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewQuiz.
     */
    // TODO: Rename and change types and number of parameters
    public static NewQuiz newInstance(String param1, String param2) {
        NewQuiz fragment = new NewQuiz();
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
        View view = inflater.inflate(R.layout.fragment_new_quiz, container, false);
        context = getActivity();

        article = view.findViewById(R.id.article);
        Button generate = view.findViewById(R.id.generate);
        Button camera = view.findViewById(R.id.camera);
        Button clear = view.findViewById(R.id.clear);

        Bundle bundle = getArguments();

        if(bundle != null && bundle.getString("sample") != null){
            Helper.showSuccessDialog(context,"Sample Article","This is an article about the Kenyan environmental hero Wangari Maathai.",null);
            String sample = bundle.getString("sample");
            article.setText(sample);
        }

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleText = article.getText().toString();
                generateQuestions(context,articleText);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                article.setText("");
            }
        });

        return view;
    }

    public void generateQuestions(Context context,String articleText){
        ((MainActivity)context).startProgressDialog();
        //msg.setText("Loading...");

        String prompt = articleText;
        int numberOfQuestions = 10;
        prompt += "Test my knowledge of this text with trivia questions. Give me either " + numberOfQuestions + " or as many questions as there are distinct facts in the text. Whichever is fewer. Start easy and get progressively harder. The questions should have 4 options. The answer will be the index of the correct option. It should be zero indexed. Provide an RFC8259 compliant JSON response following this format without deviation.\n";
        prompt += "[\n" +
                "    {\n" +
                "        \"Q\": \"This is a question\",\n" +
                "        \"O\": [\n" +
                "            \"This is an option\",\n" +
                "            \"This is another option\",\n" +
                "            \"This is a third option\",\n" +
                "            \"This is a fourth option\"\n" +
                "        ],\n" +
                "        \"A\": 2\n" +
                "    }\n" +
                "]";
        Log.d("TAG", "generateQuestions: " + prompt);

        JSONObject params = new JSONObject();
        try {
            params.put("model", "text-davinci-003");
            params.put("prompt", prompt);
            params.put("temperature", 0.7);
            params.put("max_tokens", 1024);
            params.put("top_p", 1);
            params.put("frequency_penalty", 0);
            params.put("presence_penalty", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(params.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();
        ((MainActivity)context).client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ((MainActivity)context).stopProgressDialog();
                ((MainActivity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        Helper.showErrorDialog(context,"");
                    }
                });
                Log.d("TAG", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ((MainActivity)context).stopProgressDialog();
                Quiz q = new Quiz();
                q.setArticle(articleText);
                q.setDate(new SimpleDateFormat("dd, MMM, y", Locale.getDefault()).format(Calendar.getInstance().getTime()));
                q.setName("Test for " + q.getDate());
                String resp = response.body().string();
                try {
                    JSONObject res = new JSONObject(resp);
                    JSONArray a = res.getJSONArray("choices");
                    JSONObject j = a.getJSONObject(0);
                    String text = j.get("text").toString();
                    Log.d("TAG", "onResponse: " + text);
                    q.setQuestionJSON(text);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                q.save();
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ((MainActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        builder.setTitle("Success")
                                .setMessage("Start the quiz now?")
                                .setNegativeButton("Start", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("id",q.getId());
                                        ((MainActivity)context).load(new QuizFragment(),bundle,0);
                                    }
                                })
                                .setPositiveButton("Home", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((MainActivity)context).load(new Home(),null,0);
                                    }
                                })
                                .show();
                    }
                });

            }
        });
    }

    private void openCamera() {

    }

}