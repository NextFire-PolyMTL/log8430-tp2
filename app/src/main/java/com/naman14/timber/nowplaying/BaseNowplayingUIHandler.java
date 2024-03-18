package com.naman14.timber.nowplaying;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.naman14.timber.MusicPlayer;
import com.naman14.timber.R;
import com.naman14.timber.timely.TimelyView;
import com.naman14.timber.utils.Helpers;
import com.naman14.timber.utils.TimberUtils;

public class NowPlayingUIHandler {

    private ImageView albumart;
    private TextView songtitle, songalbum, songartist, songduration, elapsedtime;
    private SeekBar mProgress;
    private CircularSeekBar mCircularProgress;
    private TimelyView timelyView11, timelyView12, timelyView13, timelyView14, timelyView15;
    private TextView hourColon;
    private Handler mElapsedTimeHandler;

    //seekbar
    public Runnable mUpdateProgress = BaseNowplayingUpdater.getUpdateProgress(mProgress)

    //circular seekbar
    public Runnable mUpdateCircularProgress = BaseNowplayingUpdater.getUpdateCircularProgress(mCircularProgress)

    public Runnable mUpdateElapsedTime = new Runnable() {
        @Override
        public void run() {
            if (getActivity() != null) {
                String time = TimberUtils.makeShortTimeString(getActivity(), MusicPlayer.position() / 1000);
                if (time.length() < 5) {
                    timelyView11.setVisibility(View.GONE);
                    timelyView12.setVisibility(View.GONE);
                    hourColon.setVisibility(View.GONE);
                    tv13(time.charAt(0) - '0');
                    tv14(time.charAt(2) - '0');
                    tv15(time.charAt(3) - '0');
                } else if (time.length() == 5) {
                    timelyView12.setVisibility(View.VISIBLE);
                    tv12(time.charAt(0) - '0');
                    tv13(time.charAt(1) - '0');
                    tv14(time.charAt(3) - '0');
                    tv15(time.charAt(4) - '0');
                } else {
                    timelyView11.setVisibility(View.VISIBLE);
                    hourColon.setVisibility(View.VISIBLE);
                    tv11(time.charAt(0) - '0');
                    tv12(time.charAt(2) - '0');
                    tv13(time.charAt(3) - '0');
                    tv14(time.charAt(5) - '0');
                    tv15(time.charAt(6) - '0');
                }
                mElapsedTimeHandler.postDelayed(this, 600);
            }

        }
    };

    public NowPlayingUIHandler(Context context, View view) {
        // Initialize UI elements
        albumart = view.findViewById(R.id.album_art);
        songtitle = view.findViewById(R.id.song_title);
        songalbum = view.findViewById(R.id.song_album);
        songartist = view.findViewById(R.id.song_artist);
        songduration = view.findViewById(R.id.song_duration);
        elapsedtime = view.findViewById(R.id.song_elapsed_time);
        timelyView11 = view.findViewById(R.id.timelyView11);
        timelyView12 = view.findViewById(R.id.timelyView12);
        timelyView13 = view.findViewById(R.id.timelyView13);
        timelyView14 = view.findViewById(R.id.timelyView14);
        timelyView15 = view.findViewById(R.id.timelyView15);
        hourColon = view.findViewById(R.id.hour_colon);
        mProgress = view.findViewById(R.id.song_progress);
        mCircularProgress = view.findViewById(R.id.song_progress_circular);

        // Initialize UI elements with default values or listeners
        // (e.g., set up click listeners, seek bar change listeners, etc.)
        // Example:
        if (songartist != null) {
            songartist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle artist click
                }
            });
        }
    }

    public void updateUI() {
        // Update UI elements with current playback information
        // Example:
        if (songtitle != null && MusicPlayer.getTrackName() != null) {
            songtitle.setText(MusicPlayer.getTrackName());
            // Additional UI updates
        }
    }

    public void onResumeUpdate() {
        if (mProgress != null)
            mProgress.postDelayed(mUpdateProgress, 10);

        if (mCircularProgress != null)
            mCircularProgress.postDelayed(mUpdateCircularProgress, 10);
    }

    public void setSongDetails(View view) {

        albumart = (ImageView) view.findViewById(R.id.album_art);
        shuffle = (ImageView) view.findViewById(R.id.shuffle);
        repeat = (ImageView) view.findViewById(R.id.repeat);
        next = (MaterialIconView) view.findViewById(R.id.next);
        previous = (MaterialIconView) view.findViewById(R.id.previous);
        mPlayPause = (PlayPauseButton) view.findViewById(R.id.playpause);
        playPauseFloating = (FloatingActionButton) view.findViewById(R.id.playpausefloating);
        playPauseWrapper = view.findViewById(R.id.playpausewrapper);

        songtitle = (TextView) view.findViewById(R.id.song_title);
        songalbum = (TextView) view.findViewById(R.id.song_album);
        songartist = (TextView) view.findViewById(R.id.song_artist);
        songduration = (TextView) view.findViewById(R.id.song_duration);
        elapsedtime = (TextView) view.findViewById(R.id.song_elapsed_time);

        timelyView11 = (TimelyView) view.findViewById(R.id.timelyView11);
        timelyView12 = (TimelyView) view.findViewById(R.id.timelyView12);
        timelyView13 = (TimelyView) view.findViewById(R.id.timelyView13);
        timelyView14 = (TimelyView) view.findViewById(R.id.timelyView14);
        timelyView15 = (TimelyView) view.findViewById(R.id.timelyView15);
        hourColon = (TextView) view.findViewById(R.id.hour_colon);

        mProgress = (SeekBar) view.findViewById(R.id.song_progress);
        mCircularProgress = (CircularSeekBar) view.findViewById(R.id.song_progress_circular);

        recyclerView = (RecyclerView) view.findViewById(R.id.queue_recyclerview);


        songtitle.setSelected(true);


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("");
        }
        if (mPlayPause != null && getActivity() != null) {
            mPlayPause.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        }

        if (playPauseFloating != null) {
            playPauseDrawable.setColorFilter(TimberUtils.getBlackWhiteColor(accentColor), PorterDuff.Mode.MULTIPLY);
            playPauseFloating.setImageDrawable(playPauseDrawable);
            if (MusicPlayer.isPlaying())
                playPauseDrawable.transformToPause(false);
            else playPauseDrawable.transformToPlay(false);
        }

        if (mCircularProgress != null) {
            mCircularProgress.setCircleProgressColor(accentColor);
            mCircularProgress.setPointerColor(accentColor);
            mCircularProgress.setPointerHaloColor(accentColor);
        }

        if (timelyView11 != null) {
            String time = TimberUtils.makeShortTimeString(getActivity(), MusicPlayer.position() / 1000);
            if (time.length() < 5) {
                timelyView11.setVisibility(View.GONE);
                timelyView12.setVisibility(View.GONE);
                hourColon.setVisibility(View.GONE);

                changeDigit(timelyView13, time.charAt(0) - '0');
                changeDigit(timelyView14, time.charAt(2) - '0');
                changeDigit(timelyView15, time.charAt(3) - '0');

            } else if (time.length() == 5) {
                timelyView12.setVisibility(View.VISIBLE);
                changeDigit(timelyView12, time.charAt(0) - '0');
                changeDigit(timelyView13, time.charAt(1) - '0');
                changeDigit(timelyView14, time.charAt(3) - '0');
                changeDigit(timelyView15, time.charAt(4) - '0');
            } else {
                timelyView11.setVisibility(View.VISIBLE);
                hourColon.setVisibility(View.VISIBLE);
                changeDigit(timelyView11, time.charAt(0) - '0');
                changeDigit(timelyView12, time.charAt(2) - '0');
                changeDigit(timelyView13, time.charAt(3) - '0');
                changeDigit(timelyView14, time.charAt(5) - '0');
                changeDigit(timelyView15, time.charAt(6) - '0');
            }
        }

        setSongDetails();

    }

    public void updateSongDetails() {
        //do not reload image if it was a play/pause change
        if (!duetoplaypause) {
            if (albumart != null) {
                ImageLoader.getInstance().displayImage(TimberUtils.getAlbumArtUri(MusicPlayer.getCurrentAlbumId()).toString(), albumart,
                        new DisplayImageOptions.Builder().cacheInMemory(true)
                                .showImageOnFail(R.drawable.ic_empty_music2)
                                .build(), new SimpleImageLoadingListener() {

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                doAlbumArtStuff(loadedImage);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                Bitmap failedBitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.ic_empty_music2);
                                doAlbumArtStuff(failedBitmap);
                            }

                        });
            }
            if (songtitle != null && MusicPlayer.getTrackName() != null) {
                songtitle.setText(MusicPlayer.getTrackName());
                if(MusicPlayer.getTrackName().length() <= 23){
                    songtitle.setTextSize(25);
                }
                else if(MusicPlayer.getTrackName().length() >= 30){
                    songtitle.setTextSize(18);
                }
                else{
                    songtitle.setTextSize(18 + (MusicPlayer.getTrackName().length() - 24));
                }
                Log.v("BaseNowPlayingFrag", "Title Text Size: " + songtitle.getTextSize());
            }
            if (songartist != null) {
                songartist.setText(MusicPlayer.getArtistName());
                songartist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavigationUtils.goToArtist(getContext(), MusicPlayer.getCurrentArtistId());
                    }
                });
            }
            if (songalbum != null)
                songalbum.setText(MusicPlayer.getAlbumName());

        }
        duetoplaypause = false;

        if (mPlayPause != null)
            updatePlayPauseButton();

        if (playPauseFloating != null)
            updatePlayPauseFloatingButton();

        if (songduration != null && getActivity() != null)
            songduration.setText(TimberUtils.makeShortTimeString(getActivity(), MusicPlayer.duration() / 1000));

        if (mProgress != null) {
            mProgress.setMax((int) MusicPlayer.duration());
            if (mUpdateProgress != null) {
                mProgress.removeCallbacks(mUpdateProgress);
            }
            mProgress.postDelayed(mUpdateProgress, 10);
        }
        if (mCircularProgress != null) {
            mCircularProgress.setMax((int) MusicPlayer.duration());
            if (mUpdateCircularProgress != null) {
                mCircularProgress.removeCallbacks(mUpdateCircularProgress);
            }
            mCircularProgress.postDelayed(mUpdateCircularProgress, 10);
        }

        if (timelyView11 != null) {
            mElapsedTimeHandler = new Handler();
            mElapsedTimeHandler.postDelayed(mUpdateElapsedTime, 600);
        }
    }

    private void setSeekBarListener() {
        if (mProgress != null)
            mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {
                        MusicPlayer.seek((long) i);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        if (mCircularProgress != null) {
            mCircularProgress.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                @Override
                public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        MusicPlayer.seek((long) progress);
                    }
                }

                @Override
                public void onStopTrackingTouch(CircularSeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(CircularSeekBar seekBar) {

                }
            });
        }
    }

    public void changeDigit(TimelyView tv, int end) {
        ObjectAnimator obja = tv.animate(end);
        obja.setDuration(400);
        obja.start();
    }

    public void changeDigit(TimelyView tv, int start, int end) {
        try {
            ObjectAnimator obja = tv.animate(start, end);
            obja.setDuration(400);
            obja.start();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }

    public void tv11(int a) {
        if (a != timeArr[0]) {
            changeDigit(timelyView11, timeArr[0], a);
            timeArr[0] = a;
        }
    }

    public void tv12(int a) {
        if (a != timeArr[1]) {
            changeDigit(timelyView12, timeArr[1], a);
            timeArr[1] = a;
        }
    }

    public void tv13(int a) {
        if (a != timeArr[2]) {
            changeDigit(timelyView13, timeArr[2], a);
            timeArr[2] = a;
        }
    }

    public void tv14(int a) {
        if (a != timeArr[3]) {
            changeDigit(timelyView14, timeArr[3], a);
            timeArr[3] = a;
        }
    }

    public void tv15(int a) {
        if (a != timeArr[4]) {
            changeDigit(timelyView15, timeArr[4], a);
            timeArr[4] = a;
        }
    }
}
