package com.google.engedu.ghost;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class Ghost2Activity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    String userScoreLabel = "Your score : ";
    String compScoreLabel = "Computer score : ";
    Integer user_score=0,comp_score=0;
    TextView ghostTv, label, score;
    Button challengebtn,restartbtn;
    String score_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2ghost);

        //Link views using fv
        score = (TextView)findViewById(R.id.score);
        ghostTv = (TextView) findViewById(R.id.ghostText);
        label = (TextView) findViewById(R.id.gameStatus);
        challengebtn = (Button)findViewById(R.id.challengeButton);
        restartbtn = (Button)findViewById(R.id.restartButton);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }


    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        challengebtn.setEnabled(true);
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        // Do computer turn stuff then make it the user's turn again

        //fetch the existing word
        String word = ghostTv.getText().toString();

        //if the word in completed(valid word)
        if (dictionary.isWord(word) && word.length() >= 4) {
            Log.d("TAG", "computerTurn: true");
            label.setText("You Won");
            user_score++;
            score_label = userScoreLabel+String.valueOf(user_score)+"    "+compScoreLabel+String.valueOf(comp_score);
            score.setText(score_label);
            challengebtn.setEnabled(false);

        }
        //else get the existing word and check for its prefix
        else {

            String longerWord = dictionary.getAnyWordStartingWith(word);

            //if the word with the existing word as prefix exists then add the
            //next character to the existing word
            if (longerWord != null) {
                char nextChar = longerWord.charAt(word.length());
                word += nextChar;
                ghostTv.setText(word);
                label.setText(USER_TURN);
            } else {

                //if no word exists with the entered word as a prefix then tell the user that he can't make up words which are not in dictionary
                label.setText("you can't bluff this , you lost");
                challengebtn.setEnabled(false);
            }
        }


        userTurn = true;
       // label.setText(USER_TURN);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        Log.d("TAG", "onKeyUp: "+keyCode+" "+ (char) event.getUnicodeChar());

        char keyPressed = (char) event.getUnicodeChar();

        if (Character.isLetter(keyPressed)) {
            String existingWord = ghostTv.getText().toString();
            existingWord += keyPressed;
            ghostTv.setText(existingWord);


            //call the computer turn
            computerTurn();
            //check for the validity
//            if (dictionary.isWord(existingWord)) {
//                label.setText("VALID WORD");
//            } else
//                label.setText("INVALID WORD");
            return true;
        } else

            return super.onKeyUp(keyCode, event);
    }

    public void challenge(View view) {

        //get the existing word
        String currentWord = ghostTv.getText().toString();

        //if the word is a valid word then the challenge is successful
        if (currentWord.length() > 3 && dictionary.isWord(currentWord)) {
            label.setText("You Won");
            user_score++;
            score_label = userScoreLabel+String.valueOf(user_score)+"    "+compScoreLabel+String.valueOf(comp_score);
            score.setText(score_label);
            challengebtn.setEnabled(false);
        } else  {
            // if a word can be formed with the fragment as prefix, declare victory for the computer and display a possible word
            String anotherWord = dictionary.getAnyWordStartingWith(currentWord);
            if (anotherWord != null) {
                label.setText("Computer Won");
                comp_score++;
                score_label = userScoreLabel+String.valueOf(user_score)+"    "+compScoreLabel+String.valueOf(comp_score);
                score.setText(score_label);
                ghostTv.setText(anotherWord);
                challengebtn.setEnabled(false);
            }
            //If a word cannot be formed with the fragment, declare victory for the user
            else {
                label.setText("you won, computer lost this game");
                user_score++;
                score_label = userScoreLabel+String.valueOf(user_score)+"    "+compScoreLabel+String.valueOf(comp_score);
                score.setText(score_label);
                challengebtn.setEnabled(false);
            }
        }


    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.setting) {
            Intent i=new Intent(this,SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
