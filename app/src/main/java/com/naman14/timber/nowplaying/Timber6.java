package com.naman14.timber.nowplaying;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.naman14.timber.MusicPlayer;
import com.naman14.timber.MusicService;
import com.naman14.timber.R;
import com.naman14.timber.dataloaders.SongLoader;
import com.naman14.timber.models.Song;
import com.naman14.timber.utils.TimberUtils;
import com.naman14.timber.widgets.CircleImageView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * Created by naman on 22/02/17.
 */

public class Timber6 extends BaseNowplayingFragmentEvenMode {

    TextView nextSong;
    CircleImageView nextArt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_timber6, container, false);

        setMusicStateListener();
        setSongDetails(rootView);

        initGestures(rootView.findViewById(R.id.album_art));

        ((SeekBar) rootView.findViewById(R.id.song_progress)).getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
        ((SeekBar) rootView.findViewById(R.id.song_progress)).getThumb().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));

        nextSong = (TextView) rootView.findViewById(R.id.title_next);
        nextArt = (CircleImageView) rootView.findViewById(R.id.album_art_next);

        rootView.findViewById(R.id.nextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayer.next();
            }
        });

        return rootView;
    }

    @Override
    public void onMetaChanged() {
        super.onMetaChanged();
        if (getActivity() != null) {
            long nextId = MusicPlayer.getNextAudioId();
            Song next = SongLoader.getSongForID(getActivity(), nextId);
            nextSong.setText(next.title);
            nextArt.setImageURI(TimberUtils.getAlbumArtUri(next.albumId));
        }
    }
}
