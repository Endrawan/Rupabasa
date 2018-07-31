package com.endrawan.dlingua.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.endrawan.dlingua.Classes.ListenSpeakItem;
import com.endrawan.dlingua.QuestionActivity;
import com.endrawan.dlingua.R;

import java.util.Locale;

import at.markushi.ui.CircleButton;

/**
 * Endrawan made this on 20/01/2017.
 */
public class ListeningFragment extends Fragment {

    private TextToSpeech tts;
    private final Locale LOCALE_LANGUAGE = new Locale("ind", "IDN");

    int currentPostion;

    EditText ETanswer;
    CircleButton Bspeak;
    Button Bcheck;
    QuestionActivity activity;
    String Sentences = "Halo";
    RoundCornerProgressBar mProgress;

    BubbleLayout bubbleLayout;
    PopupWindow popupWindow;
    TextView TVmessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listening, container, false);

        Bcheck = (Button) v.findViewById(R.id.Bcheck);
        Bspeak = (CircleButton) v.findViewById(R.id.btnSpeak);
        ETanswer = (EditText) v.findViewById(R.id.ETanswer);
        mProgress = (RoundCornerProgressBar) v.findViewById(R.id.mProgress);
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

        // set the question
        activity = (QuestionActivity) getActivity();
        ListenSpeakItem currentQuestion = activity.getQuestionListenSpeak();
        currentPostion = activity.getCurrentSpanQuestion();
        Sentences = currentQuestion.getSentences();
        mProgress.setMax(activity.getQuestionSpan());
        mProgress.setProgress(currentPostion - 1);

        Bspeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak(Sentences);
            }
        });

        ETanswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(ETanswer.getText().toString())) {
                    changeButtonDead();
                } else {
                    changeButtonActive();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Bcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Bcheck.getText().toString().equals(getResources()
                        .getString(R.string.checkAnswer))){
                    //boolean result = checkAnswer();
                    checkThisAnswer(view);
                }
                else{
                    activity.replaceFragment();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private boolean checkAnswer() {
        if (Sentences.equalsIgnoreCase(ETanswer.getText().toString())) {
            activity.increaseRight();
            return true;
        } else {
            activity.increaseWrong();
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tts.shutdown();
    }

    private void changeButtonDead() {
        Bcheck.setEnabled(false);
        Bcheck.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorButtonDead));
        Bcheck.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButtonTextDead));
    }

    private void changeButtonActive() {
        Bcheck.setEnabled(true);
        Bcheck.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorButtonCheck));
        Bcheck.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorButtonTextActive));
    }

    private void checkThisAnswer(View view) {
        if (checkAnswer()) {
            TVmessage.setText("Anda Benar!");
            bubbleLayout.setBubbleColor(ContextCompat.getColor(getActivity(),
                    R.color.colorButtonCheck));
        } else {
            TVmessage.setText("Anda Salah!");
            bubbleLayout.setBubbleColor(ContextCompat.getColor(getActivity(),R.color.colorRed));
        }
        int[] location = new int[2];
        view.getLocationInWindow(location);
        //bubbleLayout.setArrowDirection(ArrowDirection.BOTTOM);
        bubbleLayout.setArrowPosition((Bcheck.getWidth()/2) - (bubbleLayout.getArrowWidth() / 2));
        popupWindow.setWidth(Bcheck.getWidth());
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - 172);
        Bcheck.setText("LANJUTKAN");
        mProgress.setProgress(currentPostion);
    }
}
