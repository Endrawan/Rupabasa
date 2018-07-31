package com.endrawan.dlingua;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.endrawan.dlingua.Classes.ListenSpeakItem;
import com.endrawan.dlingua.Classes.TranslateItem;
import com.endrawan.dlingua.Fragments.ListeningFragment;
import com.endrawan.dlingua.Fragments.MyLangFragment;
import com.endrawan.dlingua.Fragments.OtherLangFragment;
import com.endrawan.dlingua.Fragments.SpeakingFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    FragmentManager fr;
    Fragment mFragment;

    Random r = new Random();
    String subCourseKey;
    int randomNumber = -1, totalQuestion, rightAnswer = 0, wrongAnswer = 0;
    private int QUESTION_SPAN;
    int currentSpanQuestion = 1;

    // Question List
    List<Object> QuestionList = new ArrayList<>();
    List<Boolean> QuestionDone = new ArrayList<>();

    List<ListenSpeakItem> ListeningList = new ArrayList<>();
    List<ListenSpeakItem> SpeakingList = new ArrayList<>();
    List<TranslateItem> toMyLanguageList = new ArrayList<>();
    List<TranslateItem> toOtherLanguageList = new ArrayList<>();

    int ListeningDone = 0, SpeakingDone = 0, toMyLangDone = 0, toOtherLangDone = 0;
    List<List> itemList = new ArrayList<>();

    // Question Object
    ListenSpeakItem questionListenSpeak;
    TranslateItem questionTranslate;

    // Firebase
    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference ListeningRef = mFirebaseDatabase.getReference("Questions/Listening/");
    final DatabaseReference SpeakingRef = mFirebaseDatabase.getReference("Questions/Speaking/");
    final DatabaseReference toMyLanguageRef = mFirebaseDatabase
            .getReference("Questions/toMyLanguage/");
    final DatabaseReference toOtherLanguage = mFirebaseDatabase
            .getReference("Questions/toOtherLanguage/");

    final DatabaseReference AllReferences[] = {ListeningRef, SpeakingRef,
            toMyLanguageRef, toOtherLanguage};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Initialization
        itemList.add(ListeningList);
        itemList.add(SpeakingList);
        itemList.add(toMyLanguageList);
        itemList.add(toOtherLanguageList);

        // Find the view(s)
        fr = getSupportFragmentManager();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            subCourseKey = extras.getString("subcourseKey");
        }

        // Get the question(s)
        for (int x = 0; x < AllReferences.length; x++) {
            final int finalX = x;
            AllReferences[x].child(subCourseKey)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        getItemSnap(dataSnapshot, finalX);
                        setFirstFragment(finalX);
                    } else {
                        setFirstFragment(finalX);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(QuestionActivity.this, "Error : " + databaseError,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Getter
    public ListenSpeakItem getQuestionListenSpeak() {
        return questionListenSpeak;
    }

    public TranslateItem getQuestionTranslate() {
        return questionTranslate;
    }

    public void increaseRight() {
        rightAnswer++;
    }

    public void increaseWrong() {
        wrongAnswer++;
    }

    public int getQuestionSpan() {
        return QUESTION_SPAN;
    }

    public int getCurrentSpanQuestion() {
        return currentSpanQuestion;
    }

    private void setFirstFragment(int index) {
        if (index + 1 == AllReferences.length) {
            QUESTION_SPAN = QuestionList.size();
            for(int x = 0; x < QuestionList.size(); x++) {
                QuestionDone.add(false);
            }
            randomAndSet();
            fr.beginTransaction().add(R.id.questionView, mFragment).commit();
            Toast.makeText(getApplicationContext(), "Ini work 2", Toast.LENGTH_SHORT).show();
        }
    }

    public void randomFragment() {
        switch (randomNumber) {
            case 0:
                mFragment = new ListeningFragment();
                break;
            case 1:
                mFragment = new SpeakingFragment();
                break;
            case 2:
                mFragment = new MyLangFragment();
                break;
            case 3:
                mFragment = new OtherLangFragment();
                break;
            default:
                break;
        }
    }

    private void setQuestion() {
        int randQuestion;
        randomNumber++;
        //randomNumber = r.nextInt(4);
        switch (randomNumber) {
            case 0:
                if (ListeningList.size() == 0 || ListeningDone == ListeningList.size()) {
                    //randomAndSet();
                    setQuestion();
                } else {
                    randQuestion = r.nextInt(ListeningList.size());
                    questionListenSpeak = (ListenSpeakItem) QuestionList.get(randQuestion);
                }
                break;
            case 1:
                if (SpeakingList.size() == 0 || SpeakingDone == SpeakingList.size()) {
                    setQuestion();
                } else {
                    randQuestion = r.nextInt(SpeakingList.size());
                    randQuestion += ListeningList.size();
                    questionListenSpeak = (ListenSpeakItem) QuestionList.get(randQuestion);
                }
                break;
            case 2:
                if (toMyLanguageList.size() == 0 || toMyLangDone == toMyLanguageList.size()) {
                    setQuestion();
                } else {
                    randQuestion = r.nextInt(toMyLanguageList.size());
                    randQuestion += ListeningList.size() + SpeakingList.size();
                    questionTranslate = (TranslateItem) QuestionList.get(randQuestion);
                }
                break;
            case 3:
                if (toOtherLanguageList.size() == 0 ||
                        toOtherLangDone == toOtherLanguageList.size()) {
                    setQuestion();
                } else {
                    randQuestion = r.nextInt(toOtherLanguageList.size());
                    randQuestion += ListeningList.size() + SpeakingList.size() + toMyLanguageList.size();
                    questionTranslate = (TranslateItem) QuestionList.get(randQuestion);
                }
                break;
            default:
                randomNumber = -1;
                setQuestion();
                break;
        }
    }

    public void replaceFragment() {
        if (currentSpanQuestion >= QUESTION_SPAN) {
            Intent i = new Intent(QuestionActivity.this, ResultActivity.class);
            i.putExtra("totalQuestion", QUESTION_SPAN);
            i.putExtra("rightAnswer", rightAnswer);
            i.putExtra("wrongAnswer", wrongAnswer);
            startActivity(i);
            finish();
        } else {
            randomAndSet();
            fr.beginTransaction().replace(R.id.questionView, mFragment).commit();
            currentSpanQuestion++;
        }
    }

    public void getItemSnap(DataSnapshot snaps, int index) {
        for (DataSnapshot postSnapshot : snaps.getChildren()) {
            switch (index) {
                case 0:
                case 1:
                    ListenSpeakItem mListenSpeakItem = postSnapshot.getValue(ListenSpeakItem.class);
                    itemList.get(index).add(mListenSpeakItem);
                    QuestionList.add(mListenSpeakItem);
                    break;
                case 2:
                case 3:
                    TranslateItem mTranslateItem = postSnapshot.getValue(TranslateItem.class);
                    itemList.get(index).add(mTranslateItem);
                    QuestionList.add(mTranslateItem);
                    break;
            }
        }
    }

    private void randomAndSet() {
        setQuestion();
        randomFragment();
    }

    private boolean checkisDone(int randomNumber, List<Integer> listDone) {
        for(int x = 0; x < listDone.size(); x++) {
            if (randomNumber == listDone.get(x)) {
                return true;
            }
        }
        return false;
    }

}
