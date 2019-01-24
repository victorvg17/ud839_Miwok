/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    private MediaPlayer miwokPlayer;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        //Initialize Audio Manager for using Audio Service
        mAudioManager = (AudioManager)this.getSystemService(this.AUDIO_SERVICE);
        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("ein", "one", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("zwei", "two", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("drei", "three", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("vier", "four", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("funf", "five", R.drawable.number_five, R.raw.number_five));

        words.add(new Word("sechs", "six", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seben", "seven", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("acht", "eight", R.drawable.number_eight, R.raw.number_seven));
        words.add(new Word("neun", "nine", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("zehn", "ten", R.drawable.number_ten, R.raw.number_ten));

        // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The
        // adapter knows how to create layouts for each item in the list, using the
        // simple_list_item_1.xml layout resource defined in the Android framework.
        // This list item layout contains a single {@link TextView}, which the adapter will set to
        // display a single word.
        WordAdapter itemsAdapter = new WordAdapter(this, words, R.color.category_numbers);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list_numbers);

        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
        // {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in
        // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
        listView.setAdapter(itemsAdapter);
        //Create mediaplayer instance for listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id){

                Word word_clicked = words.get(position);

                //freeing up any resource allocated for MediaPlayer before starting with new audio file
                releaseMediaPlayer();

                //play audio corresponding to the word
                int request = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                                    AudioManager.STREAM_MUSIC,
                                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(request == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    miwokPlayer = MediaPlayer.create(NumbersActivity.this, word_clicked.getAudioResourceId());
                    miwokPlayer.start();
                    //Set OnCompletionListener for freeing up resources once the media file is played.
                    miwokPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            releaseMediaPlayer();
                        }
                    });
                }

                Log.v("NumbersActivity", "CurrentWord: " + word_clicked.toString());

            }
        });
    }

   private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.v("NumbersActivity", "Current AudioFocus state" + focusChange);
                    miwokPlayer.start();
                    break;
            }

            switch (focusChange){
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.v("NumbersActivity", "Current AudioFocus state" + focusChange);
                    //miwokPlayer.stop();
                    releaseMediaPlayer();
                    break;
            }

            switch (focusChange){
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.v("NumbersActivity", "Current AudioFocus state" + focusChange);
                    miwokPlayer.pause();
                    miwokPlayer.seekTo(0);
                    break;
            }

            switch (focusChange){
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.v("NumbersActivity", "Current AudioFocus state" + focusChange);
                    miwokPlayer.pause();
                    miwokPlayer.seekTo(0);
                    break;
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (miwokPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            miwokPlayer.release();

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            miwokPlayer = null;
        }
    }

}
