/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.naman14.timber.nowplaying;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.Config;
import com.naman14.timber.MusicPlayer;
import com.naman14.timber.MusicService;
import com.naman14.timber.R;
import com.naman14.timber.activities.BaseActivity;
import com.naman14.timber.adapters.BaseQueueAdapter;
import com.naman14.timber.adapters.SlidingQueueAdapter;
import com.naman14.timber.dataloaders.QueueLoader;
import com.naman14.timber.listeners.MusicStateListener;
import com.naman14.timber.timely.TimelyView;
import com.naman14.timber.utils.Helpers;
import com.naman14.timber.utils.NavigationUtils;
import com.naman14.timber.utils.PreferencesUtility;
import com.naman14.timber.utils.SlideTrackSwitcher;
import com.naman14.timber.utils.TimberUtils;
import com.naman14.timber.widgets.CircularSeekBar;
import com.naman14.timber.widgets.DividerItemDecoration;
import com.naman14.timber.widgets.PlayPauseButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.security.InvalidParameterException;

public class BaseNowplayingFragment extends Fragment implements MusicStateListener {

    private MaterialIconView previous, next;
    private View playPauseWrapper;

    private String ateKey;

    private BaseQueueAdapter mAdapter;
    private SlidingQueueAdapter slidingQueueAdapter;

    private int[] timeArr = new int[]{0, 0, 0, 0, 0};
    private boolean duetoplaypause = false;

    public ImageView shuffle, repeat;
    public int accentColor;
    public RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ateKey = Helpers.getATEKey(getActivity());
        accentColor = Config.accentColor(getActivity(), ateKey);

        shuffle = (ImageView) view.findViewById(R.id.shuffle);
        repeat = (ImageView) view.findViewById(R.id.repeat);
        next = (MaterialIconView) view.findViewById(R.id.next);
        previous = (MaterialIconView) view.findViewById(R.id.previous);
        mPlayPause = (PlayPauseButton) view.findViewById(R.id.playpause);
        playPauseFloating = (FloatingActionButton) view.findViewById(R.id.playpausefloating);
        playPauseWrapper = view.findViewById(R.id.playpausewrapper);

        playPauseHandler = new PlayPauseHandler(mPlayPause, playPauseWrapper, playPauseFloating, recyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.now_playing, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_go_to_album:
                NavigationUtils.goToAlbum(getContext(), MusicPlayer.getCurrentAlbumId());
                break;
            case R.id.menu_go_to_artist:
                NavigationUtils.goToArtist(getContext(), MusicPlayer.getCurrentArtistId());
                break;
            case R.id.action_lyrics:
                NavigationUtils.goToLyrics(getContext());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        BaseNowplayingUpdater.pause(true)
    }

    @Override
    public void onResume() {
        super.onResume();
        BaseNowplayingUpdater.pause(false)
        BaseNowplayingUIHandler.onResumeUpdate();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("dark_theme", false)) {
            ATE.apply(this, "dark_theme");
        } else {
            ATE.apply(this, "light_theme");
        }
    }

    private void setSongDetails() {
        BaseNowplayingUIHandler.updateSongDetails();

        if (recyclerView != null)
            setQueueSongs();

        BaseNowplayingUIHandler.setSeekBarListener();

        if (next != null) {
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MusicPlayer.next();
                            notifyPlayingDrawableChange();
                        }
                    }, 200);

                }
            });
        }
        if (previous != null) {
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MusicPlayer.previous(getActivity(), false);
                            notifyPlayingDrawableChange();
                        }
                    }, 200);

                }
            });
        }

        if (playPauseWrapper != null)
            playPauseWrapper.setOnClickListener(mButtonListener);

        if (PlayPauseHandler.playPauseFloating != null)
            PlayPauseHandler.playPauseFloating.setOnClickListener(mFLoatingButtonListener);

        updateShuffleState();
        updateRepeatState();

    }

    public void updateShuffleState() {
        if (shuffle != null && getActivity() != null) {
            MaterialDrawableBuilder builder = MaterialDrawableBuilder.with(getActivity())
                    .setIcon(MaterialDrawableBuilder.IconValue.SHUFFLE)
                    .setSizeDp(30);

            if (getActivity() != null) {
                if (MusicPlayer.getShuffleMode() == 0) {
                    builder.setColor(Config.textColorPrimary(getActivity(), ateKey));
                } else builder.setColor(Config.accentColor(getActivity(), ateKey));
            }

            shuffle.setImageDrawable(builder.build());
            shuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicPlayer.cycleShuffle();
                    updateShuffleState();
                    updateRepeatState();
                }
            });
        }
    }

    public void updateRepeatState() {
        if (repeat != null && getActivity() != null) {
            MaterialDrawableBuilder builder = MaterialDrawableBuilder.with(getActivity())
                    .setSizeDp(30);

                if (MusicPlayer.getRepeatMode() == MusicService.REPEAT_NONE) {
                    builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT);
                    builder.setColor(Config.textColorPrimary(getActivity(), ateKey));
                } else if (MusicPlayer.getRepeatMode() == MusicService.REPEAT_CURRENT) {
                    builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
                    builder.setColor(Config.accentColor(getActivity(), ateKey));
                } else if (MusicPlayer.getRepeatMode() == MusicService.REPEAT_ALL) {
                    builder.setColor(Config.accentColor(getActivity(), ateKey));
                    builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT);
                }


            repeat.setImageDrawable(builder.build());
            repeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicPlayer.cycleRepeat();
                    updateRepeatState();
                    updateShuffleState();
                }
            });
        }
    }



    public void setQueueSongs() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //load queue songs in asynctask
        if (getActivity() != null)
            new loadQueueSongs().execute("");

    }

    public void notifyPlayingDrawableChange() {
        int position = MusicPlayer.getQueuePosition();
        BaseQueueAdapter.currentlyPlayingPosition = position;
    }

    public void restartLoader() {

    }

    public void onPlaylistChanged() {

    }

    public void onMetaChanged() {
        BaseNowplayingUIHandler.updateSongDetails();

        if (recyclerView != null && recyclerView.getAdapter() != null)
            recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void setMusicStateListener() {
        ((BaseActivity) getActivity()).setMusicStateListenerListener(this);
    }

    public void doAlbumArtStuff(Bitmap loadedImage) {

    }

    protected void initGestures(View v) {
        if (PreferencesUtility.getInstance(v.getContext()).isGesturesEnabled()) {
            new SlideTrackSwitcher() {
                @Override
                public void onSwipeBottom() {
                    getActivity().finish();
                }
            }.attach(v);
        }
    }

    private class loadQueueSongs extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (getActivity() != null) {
                mAdapter = new BaseQueueAdapter((AppCompatActivity) getActivity(), QueueLoader.getQueueSongs(getActivity()));
                return "Executed";
            } else return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                recyclerView.setAdapter(mAdapter);
                if (getActivity() != null)
                    recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
                recyclerView.scrollToPosition(MusicPlayer.getQueuePosition() - 1);
            }

        }

        @Override
        protected void onPreExecute() {
        }
    }
}
