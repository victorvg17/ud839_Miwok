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
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    private MediaPlayer miwokPlayer;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        mAudioManager = (AudioManager)this.getSystemService(this.AUDIO_SERVICE);
        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("Vater", "Father", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("Mutter", "Mother", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("Sohn", "Son", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("Tochter", "Daughter", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("Bruder", "Brother", R.drawable.family_older_brother, R.raw.family_older_brother));

        words.add(new Word("Schwester", "Sister", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("Neffe", "Nephew", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("Nichte", "Niece", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("Opa", "Grandfather", R.drawable.family_grandfather, R.raw.family_grandfather));
        words.add(new Word("Oma", "Grandmother", R.drawable.family_grandmother, R.raw.family_grandmother));

        // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The
        // adapter knows how to create layouts for each item in the list, using the
        // simple_list_item_1.xml layout resource defined in the Android framework.
        // This list item layout contains a single {@link TextView}, which the adapter will set to
        // display a single word.
        WordAdapter itemsAdapter = new WordAdapter(this, words, R.color.category_family);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list_family);

        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
        // {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in
        // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word clickedWord = words.get(i);

                //freeing up any resource allocated for MediaPlayer before starting with new audio file
                releaseMediaPlayer();

                int request = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                                    AudioManager.STREAM_MUSIC,
                                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (request == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    miwokPlayer = MediaPlayer.create(FamilyActivity.this, clickedWord.getAudioResourceId());

                    miwokPlayer.start();
                    //Set OnCompletionListener for freeing up resources once the media file is played.
                    miwokPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            releaseMediaPlayer();
                        }
                    });
                }
            }
        });
    }

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.v("PhrasesActivity", "Current AudioFocus state" + focusChange);
                    miwokPlayer.start();
                    break;
            }

            switch (focusChange){
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.v("PhrasesActivity", "Current AudioFocus state" + focusChange);
                    //miwokPlayer.stop();
                    releaseMediaPlayer();
                    break;
            }

            switch (focusChange){
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.v("PhrasesActivity", "Current AudioFocus state" + focusChange);
                    miwokPlayer.pause();
                    miwokPlayer.seekTo(0);
                    break;
            }

            switch (focusChange){
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.v("PhrasesActivity", "Current AudioFocus state" + focusChange);
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
