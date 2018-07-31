package com.endrawan.dlingua.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextPaint;
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
import com.endrawan.dlingua.Classes.TranslateItem;
import com.endrawan.dlingua.QuestionActivity;
import com.endrawan.dlingua.R;

/**
 * Endrawan made this on 22/01/2017.
 */
public class OtherLangFragment extends Fragment {

    TextView TVsentence;
    EditText ETanswer;
    Button Bcheck;
    private String Sentence = "Selamat Pagi";
    private String RealAnswer = "Wilujeng Enjing";
    QuestionActivity activity;
    RoundCornerProgressBar mProgress;
    int currentPosition;

    // Bubble notification
    BubbleLayout bubbleLayout;
    PopupWindow popupWindow;
    TextView TVmessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_otherlang, container, false);

        TVsentence = (TextView) v.findViewById(R.id.TVsentences);
        ETanswer = (EditText) v.findViewById(R.id.ETanswer);
        Bcheck = (Button) v.findViewById(R.id.Bcheck);
        mProgress = (RoundCornerProgressBar) v.findViewById(R.id.mProgress);

        bubbleLayout = (BubbleLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.popup_simple, null);
        TVmessage = (TextView) bubbleLayout.findViewById(R.id.message);
        popupWindow = BubblePopupHelper.create(getActivity(), bubbleLayout);

        // Set question
        activity = (QuestionActivity) getActivity();
        TranslateItem currentQuestion = activity.getQuestionTranslate();
        Sentence = currentQuestion.getQuestion();
        RealAnswer = currentQuestion.getAnswer();

        // Set progress
        currentPosition = activity.getCurrentSpanQuestion();
        mProgress.setMax(activity.getQuestionSpan());
        mProgress.setProgress(currentPosition - 1);

        TVsentence.setText(Sentence);

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
                if(Bcheck.getText().toString().equals(getResources().getString(R.string.checkAnswer))){
                    checkThisAnswer(view);
                }
                else{
                    activity.replaceFragment();
                }
            }
        });

        return v;
    }

    private boolean checkAnswer() {
        if (RealAnswer.equalsIgnoreCase(ETanswer.getText().toString())) {
            activity.increaseRight();
            return true;
        } else {
            activity.increaseWrong();
            return false;
        }
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
        bubbleLayout.setArrowPosition((Bcheck.getWidth()/2) - (bubbleLayout.getArrowWidth() / 2));
        popupWindow.setWidth(Bcheck.getWidth());
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - 172);
        Bcheck.setText("LANJUTKAN");
        mProgress.setProgress(currentPosition);
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
}
