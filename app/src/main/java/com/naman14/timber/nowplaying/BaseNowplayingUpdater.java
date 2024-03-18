import com.naman14.timber.MusicPlayer;


public class BaseNowplayingUpdater {

    private int overflowcounter = 0;
    boolean fragmentPaused = false;


    public void getUpdateProgress(mProgress) {
        return new Runnable() {
            @Override
            public void run() {

                long position = MusicPlayer.position();
                if (mProgress != null) {
                    mProgress.setProgress((int) position);
                    if (elapsedtime != null && getActivity() != null)
                        elapsedtime.setText(TimberUtils.makeShortTimeString(getActivity(), position / 1000));
                }
                overflowcounter--;
                int delay = 250; //not sure why this delay was so high before
                if (overflowcounter < 0 && !fragmentPaused) {
                    overflowcounter++;
                    mProgress.postDelayed(mUpdateProgress, delay); //delay
                }
            }
        };
    }

    public void getUpdateCircularProgress(mCircularProgress) {
        return new Runnable() {

            @Override
            public void run() {
                long position = MusicPlayer.position();
                if (mCircularProgress != null) {
                    mCircularProgress.setProgress((int) position);
                    if (elapsedtime != null && getActivity() != null)
                        elapsedtime.setText(TimberUtils.makeShortTimeString(getActivity(), position / 1000));

                }
                overflowcounter--;
                if (MusicPlayer.isPlaying()) {
                    int delay = (int) (1500 - (position % 1000));
                    if (overflowcounter < 0 && !fragmentPaused) {
                        overflowcounter++;
                        mCircularProgress.postDelayed(mUpdateCircularProgress, delay);
                    }
                }

            }
        };
    }

    public void pause(Boolean p) {
        fragmentPaused = p;
    }


}