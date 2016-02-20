package com.londonx.lutil.util;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by london on 15/7/8.
 * 多媒体播放器
 * Update at 2015-07-24 17:26:11
 */
public class LMediaPlayer implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        Handler.Callback,
        SurfaceHolder.Callback, SeekBar.OnSeekBarChangeListener {
    public MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private SeekBar skbProgress;
    private OnProgressChangeListener onProgressChangeListener;
    private MediaPlayer.OnPreparedListener onPreparedListener;

    /**
     * Media player
     *
     * @param surfaceView null if you want to play Audio
     * @param skbProgress show play and buffering progress
     */
    public LMediaPlayer(SurfaceView surfaceView, SeekBar skbProgress,
                        MediaPlayer.OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
        if (skbProgress != null) {
            this.skbProgress = skbProgress;
            skbProgress.setOnSeekBarChangeListener(this);
        }
        if (surfaceView != null) {
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
        }
        mediaPlayer = new MediaPlayer();
        if (onPreparedListener != null) {
            mediaPlayer.setOnPreparedListener(onPreparedListener);
        }
        Timer mTimer = new Timer();
        mTimer.schedule(mTimerTask, 0, 26);
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        surfaceHolder.addCallback(this);
    }

    public void setSkbProgress(SeekBar skbProgress) {
        this.skbProgress = skbProgress;
        handleProgress.sendEmptyMessage(0);
        skbProgress.setOnSeekBarChangeListener(this);
    }

    /**
     * ****************************************************
     * 通过定时器和Handler来更新进度条
     * ****************************************************
     */
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null) {
                return;
            }
            boolean isPlaying = false;
            try {
                isPlaying = mediaPlayer.isPlaying();
            } catch (IllegalStateException ignore) {
            }
            if (isPlaying && skbProgress != null && !skbProgress.isPressed()) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handleProgress = new Handler(this);
    //*****************************************************

    /**
     * start playing
     */
    public void play() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.start();
    }


    /**
     * online media url
     *
     * @param videoUrl media(video and mp3) url
     */
    public void prepareUrl(String videoUrl) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            if (onPreparedListener != null) {
                mediaPlayer.setOnPreparedListener(onPreparedListener);
            }
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * online media url
     *
     * @param videoUrl media(video and mp3) url
     */
    public void playUrl(String videoUrl) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            if (onPreparedListener != null) {
                mediaPlayer.setOnPreparedListener(onPreparedListener);
            }
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepareAsync();//prepare之后自动播放
        } catch (Exception e) {
            e.printStackTrace();
            try {
                rescue(videoUrl);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void rescue(String url) throws IOException {
        Log.i("LondonX", "rescue");
        try {
            Thread.sleep(160);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mediaPlayer = new MediaPlayer();
        if (onPreparedListener != null) {
            mediaPlayer.setOnPreparedListener(onPreparedListener);
        }
        mediaPlayer.reset();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepareAsync();
    }

    /**
     * pause playing
     */
    public void pause() {
        mediaPlayer.pause();
    }

    /**
     * stop playing and auto mediaPlayer.release();
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        try {
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
    }


    /**
     * 通过onPrepared播放
     */
    @Override
    public void onPrepared(MediaPlayer arg0) {
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        if (videoHeight != 0 && videoWidth != 0) {
            arg0.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        if (skbProgress != null) {
            skbProgress.setSecondaryProgress(bufferingProgress);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (mediaPlayer == null) {
            return false;
        }
        int position = mediaPlayer.getCurrentPosition();
        int duration = mediaPlayer.getDuration();
        if (onProgressChangeListener != null) {
            onProgressChangeListener.progressChanged(position, duration);
        }
        if (duration > 0 && skbProgress != null) {
            skbProgress.setMax(duration);
            skbProgress.setProgress(position);
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }
        if (onProgressChangeListener != null) {
            onProgressChangeListener.progressChanged(progress, seekBar.getMax());
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(seekBar.getProgress());
    }

    public interface OnProgressChangeListener {
        void progressChanged(int position, int duration);
    }
}
