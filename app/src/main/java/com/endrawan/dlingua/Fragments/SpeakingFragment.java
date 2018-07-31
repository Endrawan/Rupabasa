package com.endrawan.dlingua.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.endrawan.dlingua.Classes.ListenSpeakItem;
import com.endrawan.dlingua.QuestionActivity;
import com.endrawan.dlingua.R;

import java.util.ArrayList;
import java.util.Locale;

import at.markushi.ui.CircleButton;

/**
 * Endrawan made this on 22/01/2017.
 */
public class SpeakingFragment extends Fragment {

    CircleButton Bspeak, Blisten;
    Button Bcheck;
    TextView TVsentences, TVcannotMic;
    RoundCornerProgressBar mProgress;

    QuestionActivity activity;
    TextToSpeech tts;
    private final Locale LOCALE_LANGUAGE = new Locale("ind", "IDN");

    String sentences = "Halo";
    int currentPosition;

    // Bubble notification
    BubbleLayout bubbleLayout;
    PopupWindow popupWindow;
    TextView TVmessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_speaking, container, false);

        Bspeak = (CircleButton) v.findViewById(R.id.Bspeak);
        Blisten = (CircleButton) v.findViewById(R.id.BhearSentences);
        TVsentences = (TextView) v.findViewById(R.id.TVsentences);
        TVcannotMic = (TextView) v.findViewById(R.id.cannotMic);
        mProgress = (RoundCornerProgressBar) v.findViewById(R.id.mProgress);
        Bcheck = (Button) v.findViewById(R.id.Bcheck);

        bubbleLayout = (BubbleLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.popup_simple, null);
        TVmessage = (TextView) bubbleLayout.findViewById(R.id.message);
        popupWindow = BubblePopupHelper.create(getActivity(), bubbleLayout);

        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(LOCALE_LANGUAGE);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getActivity(), "This Language is not supported",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Initialization Failed!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set the question
        activity = (QuestionActivity) getActivity();
        ListenSpeakItem currentQuestion = activity.getQuestionListenSpeak();
        sentences = currentQuestion.getSentences();

        // Set progress
        currentPosition = activity.getCurrentSpanQuestion();
        mProgress.setMax(activity.getQuestionSpan());
        mProgress.setProgress(currentPosition - 1);

        TVsentences.setText(sentences);

        Bspeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });
        Blisten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen(sentences);
            }
        });
        TVcannotMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Silahkan klik lanjut untuk melanjutkan",
                        Toast.LENGTH_SHORT).show();
                changeButtonActive();
                mProgress.setProgress(currentPosition);
            }
        });
        Bcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.replaceFragment();
            }
        });

        return v;
    }

    private void speak() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, LOCALE_LANGUAGE);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Katakan sesuatu");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(), "Your device doesn't support Speech Recognition",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String inSpeech = res.get(0);
            checkThisAnswer(inSpeech);
            //recognition(inSpeech);
        }
    }

    private boolean recognition(String text) {
        //Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        if (text.equalsIgnoreCase(sentences)) {
            //Toast.makeText(getActivity(), "Benar Cuk!", Toast.LENGTH_SHORT).show();
            activity.increaseRight();
            return true;
        } else {
            //Toast.makeText(getActivity(), "Salah Cuk", Toast.LENGTH_SHORT).show();
            activity.increaseWrong();
            return false;
        }
    }

    private void listen(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void changeButtonActive() {
        Bcheck.setEnabled(true);
        Bcheck.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorButtonCheck));
        Bcheck.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButtonTextActive));
        Bcheck.setText("Lanjut");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tts.shutdown();
    }

    private void checkThisAnswer(String answer) {
        if (recognition(answer)) {
            TVmessage.setText("Anda Benar!");
            bubbleLayout.setBubbleColor(ContextCompat.getColor(getActivity(),
                    R.color.colorButtonCheck));
        } else {
            TVmessage.setText("Anda Salah!");
            bubbleLayout.setBubbleColor(ContextCompat.getColor(getActivity(),R.color.colorRed));
        }
        changeButtonActive();
        int[] location = new int[2];
        Bcheck.getLocationInWindow(location);
        //bubbleLayout.setArrowDirection(ArrowDirection.BOTTOM);
        bubbleLayout.setArrowPosition((Bcheck.getWidth()/2) - (bubbleLayout.getArrowWidth() / 2));
        popupWindow.setWidth(Bcheck.getWidth());
        popupWindow.showAtLocation(Bcheck, Gravity.NO_GRAVITY, location[0], location[1] - 172);
        Bcheck.setText("LANJUTKAN");
        mProgress.setProgress(currentPosition);
    }
}
