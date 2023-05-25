package com.example.quizmeapp.Utils;

import static com.example.quizmeapp.MainActivity.JSON;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.quizmeapp.MainActivity;
import com.example.quizmeapp.Models.Quiz;

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

public class Helper {

    public static void showErrorDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error")
                .setMessage(msg + "\nPlease try again.")
                .setNegativeButton("Close",null)
                .show();
    }

    public static void showSuccessDialog(Context context, String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Success")
                .setMessage(msg)
                .setNegativeButton("Close", listener)
                .show();
    }

    public static void generateQuestions(Context context,String articleText){
        ((MainActivity)context).startProgressDialog();
        //msg.setText("Loading...");

        String prompt = articleText;
        prompt += "Test my knowledge of this text with trivia questions. Start easy and get progressively harder. The questions should have 4 options. The answer will be the index of the correct option. Provide an RFC8259 compliant JSON response following this format without deviation.\n";
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
                    JSONArray questions = new JSONArray(text);
                    q.setQuestionJSON(questions.toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                q.save();
            }
        });
    }
}
