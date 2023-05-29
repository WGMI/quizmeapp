package com.example.quizmeapp.Utils;

import static com.example.quizmeapp.MainActivity.JSON;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.quizmeapp.Fragments.Home;
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
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

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

    public static void slideAndDisappear(final TextView textView, int slideDuration, int delay, int fadeOutDuration) {
        // Slide in animation
        ObjectAnimator slideInAnimation = ObjectAnimator.ofFloat(textView, "translationX", -textView.getWidth(), 0);
        slideInAnimation.setDuration(slideDuration);

        // Fade out animation
        ObjectAnimator fadeOutAnimation = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f);
        fadeOutAnimation.setDuration(fadeOutDuration);

        // Chain the animations
        slideInAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fadeOutAnimation.start();
                    }
                }, delay);
            }
        });

        // Start the animations
        slideInAnimation.start();
    }
}
