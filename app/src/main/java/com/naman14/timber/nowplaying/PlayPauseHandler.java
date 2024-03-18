import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.naman14.timber.MusicPlayer;
import com.naman14.timber.R;
import com.naman14.timber.widgets.PlayPauseButton;
import com.naman14.timber.widgets.PlayPauseDrawable;

public class PlayPauseHandler {

    private PlayPauseButton mPlayPause;
    private PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable();
    private View playPauseWrapper;
    private View playPauseFloating;
    private RecyclerView recyclerView;

    public PlayPauseHandler(PlayPauseButton playPause, View playPauseWrapper, View playPauseFloating, RecyclerView recyclerView) {
        this.mPlayPause = playPause;
        this.playPauseWrapper = playPauseWrapper;
        this.playPauseFloating = playPauseFloating;
        this.recyclerView = recyclerView;
        initListeners();
    }

    private void initListeners() {
        if (playPauseWrapper != null)
            playPauseWrapper.setOnClickListener(mButtonListener);

        if (playPauseFloating != null)
            playPauseFloating.setOnClickListener(mFLoatingButtonListener);
    }

    private final View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            duetoplaypause = true;
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            } else {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MusicPlayer.playOrPause();
                    if (recyclerView != null && recyclerView.getAdapter() != null)
                        recyclerView.getAdapter().notifyDataSetChanged();
                }
            }, 200);
    };}

    private final View.OnClickListener mFLoatingButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            duetoplaypause = true;
            if(MusicPlayer.getCurrentTrack() == null) {
                Toast.makeText(getContext(), getString(R.string.now_playing_no_track_selected), Toast.LENGTH_SHORT).show();
            } else {
                playPauseDrawable.transformToPlay(true);
                playPauseDrawable.transformToPause(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MusicPlayer.playOrPause();
                        if (recyclerView != null && recyclerView.getAdapter() != null)
                            recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }, 250);
            }
    };
    }

    public void updatePlayPauseButton() {
        if (MusicPlayer.isPlaying) {
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            }
        } else {
            if (mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
        }
    }

    public void updatePlayPauseFloatingButton() {
        if (MusicPlayer.isPlaying) {
            playPauseDrawable.transformToPause(false);
        } else {
            playPauseDrawable.transformToPlay(false);
        }
    }
}

