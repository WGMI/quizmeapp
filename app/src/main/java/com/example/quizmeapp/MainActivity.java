package com.example.quizmeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.quizmeapp.Fragments.Home;
import com.example.quizmeapp.Models.Quiz;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reactiveandroid.query.Select;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    Context context;
    ProgressDialog progressDialog;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public OkHttpClient client;

    static String TAG = "tag_home";

    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addQuiz();

        context = this;
        client = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .build();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        preferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);

        load(new Home(),null,R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    load(new Home(),null,0);
                    bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                } else if (itemId == R.id.settings) {
                    /*load(new Categories(),null,0);
                    bottomNavigationView.getMenu().findItem(R.id.categories).setChecked(true);*/
                } else if (itemId == R.id.account) {
                    /*load(new Cart(),null,0);
                    bottomNavigationView.getMenu().findItem(R.id.cart).setChecked(true);*/
                }

                return false;
            }
        });

        /*promt = findViewById(R.id.prompt);
        generate = findViewById(R.id.run);
        msg = findViewById(R.id.msg);*/
    }

    private void addQuiz() {
        /*if(Select.from(Quiz.class).count() == 0){
            String article = ""
            Quiz quiz = new Quiz("Wangari Mathai", article, new SimpleDateFormat("dd, MMM, y", Locale.getDefault()).format(Calendar.getInstance().getTime()), description, questionJSON);
        }*/
    }

    public void startProgressDialog(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void stopProgressDialog(){
        progressDialog.dismiss();
    }

    public void load(Fragment fragment, Bundle bundle, int selection) {
        if(bundle != null){
            fragment.setArguments(bundle);
        }

        //bottomNavigationView.setSelectedItemId(R.id.home);
        if(selection != 0){
            bottomNavigationView.setSelectedItemId(selection);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(null).commit();
    }

}