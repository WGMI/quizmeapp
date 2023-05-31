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

    public static void showSuccessDialog(Context context, String title, String msg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
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

    public static String getSample() {
        return "Wangari Maathai was the founder of the Green Belt Movement and the 2004 Nobel Peace Prize Laureate. She authored four books: The Green Belt Movement; Unbowed: A Memoir; The Challenge for Africa; and Replenishing the Earth. As well as having been featured in a number of books, she and the Green Belt Movement were the subject of a documentary film, Taking Root: the Vision of Wangari Maathai (Marlboro Productions, 2008). Wangari Muta Maathai was born in Nyeri, a rural area of Kenya (Africa), in 1940. She obtained a degree in Biological Sciences from Mount St. Scholastica College in Atchison, Kansas (1964), a Master of Science degree from the University of Pittsburgh (1966), and pursued doctoral studies in Germany and the University of Nairobi, before obtaining a Ph.D. (1971) from the University of Nairobi, where she also taught veterinary anatomy. The first woman in East and Central Africa to earn a doctorate degree, Professor Maathai became chair of the Department of Veterinary Anatomy and an associate professor in 1976 and 1977 respectively. In both cases, she was the first woman to attain those positions in the region. Professor Maathai was active in the National Council of Women of Kenya (1976–1987) and was its chairman (1981–1987). In 1976, while she was serving in the National Council of Women, Professor Maathai introduced the idea of community-based tree planting. She continued to develop this idea into a broad-based grassroots organisation, the Green Belt Movement (GBM), whose main focus is poverty reduction and environmental conservation through tree planting. Professor Maathai was internationally acknowledged for her struggle for democracy, human rights, and environmental conservation, and served on the board of many organisations. She addressed the UN on a number of occasions and spoke on behalf of women at special sessions of the General Assembly during the five-year review of the Earth Summit. She served on the Commission for Global Governance and the Commission on the Future. Professor Maathai represented the Tetu constituency in Kenya’s parliament (2002–2007), and served as Assistant Minister for Environment and Natural Resources in Kenya’s ninth parliament (2003–2007). In 2005, she was appointed Goodwill Ambassador to the Congo Basin Forest Ecosystem by the eleven Heads of State in the Congo region. The following year, 2006, she founded the Nobel Women’s Initiative with her sister laureates Jody Williams, Shirin Ebadi, Rigoberta Menchú Tum, Betty Williams, and Mairead Corrigan. In 2007, Professor Maathai was invited to be co-chair of the Congo Basin Fund, an initiative by the British and the Norwegian governments to help protect the Congo forests. In recognition of her deep commitment to the environment, the United Nations (UN) Secretary-General named Professor Maathai a UN Messenger of Peace in December 2009, with a focus on the environment and climate change. In 2010 she was appointed to the Millennium Development Goals Advocacy Group: a panel of political leaders, business people and activists established with the aim to galvanise worldwide support for the achievement of the Millennium Development Goals (MDGs). Also in 2010, Professor Maathai became a trustee of the Karura Forest Environmental Education Trust, established to safeguard the public land for whose protection she had fought for almost twenty years. That same year, in partnership with the University of Nairobi, she founded the Wangari Maathai Institute for Peace and Environmental Studies (WMI). The WMI will bring together academic research—e.g. in land use, forestry, agriculture, resource-based conflicts, and peace studies—with the Green Belt Movement approach and members of the organisation. Professor Maathai died on 25 September 2011 at the age of 71 after a battle with ovarian cancer. Memorial ceremonies were held in Kenya, New York, San Francisco, and London";
    }
}
