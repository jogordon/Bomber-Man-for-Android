package edu.udel.jogordon.bombsgoboom.android;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.util.Log;

public class SoundManager implements OnPreparedListener {
    private Context context;

    private AudioManager audioManager;
    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;

    private Map<String, Integer> soundMap;
    private Map<String, String> musicMap;

    private boolean playingBackgroundMusic;

    public SoundManager(Context context) {
        this.context = context;
    }

    public void init() {
        // setup references to internal Android sound classes
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // find our sounds/music
        addSounds();
        addMusic();
    }

    public void playSound(String sound) {

        int streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        soundPool.play(soundMap.get(sound), streamVolume, streamVolume, 1, 0, 1f);
    }
    
    public String[] getAllSounds() {
        return soundMap.keySet().toArray(new String[soundMap.size()]);
    }

    private void addSounds() {
        try {
            soundMap = new HashMap<String, Integer>();

            // be careful not to use the name sounds for your folder,
            // that folder has some special sounds already in it
            String[] files = context.getAssets().list("mysounds");
            for (int i = 0; i < files.length; i++) {
                String withoutExtension = files[i].replaceFirst("\\..*", "");
                soundMap.put(withoutExtension, soundPool.load(
                    context.getAssets().openFd("mysounds/" + files[i]), 1));
            }
        }
        catch (IOException e) {
            Log.e("Sound Example", "IOException", e);
        }
    }

    private void addMusic() {
        try {
            musicMap = new HashMap<String, String>();
            // be careful not to use the name music for your folder,
            String[] musicFiles = context.getAssets().list("mymusic");
            for (int i = 0; i < musicFiles.length; i++) {
                String withoutExtension = musicFiles[i].replaceFirst("\\..*", "");
                musicMap.put(withoutExtension, "mymusic/" + musicFiles[i]);
                musicFiles[i] = withoutExtension;
            }
        }
        catch (IOException e) {
            Log.e("Sound", "IOException", e);
        }
    }

    public void playMusic(String musicName, boolean loop) {
        // assigns the data source for the music and then allows it to buffer up
        // until ready
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(musicMap.get(musicName));
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepareAsync();
            mediaPlayer.setLooping(loop);
            playingBackgroundMusic = true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayingBackgroundMusic() {
        return playingBackgroundMusic;
    }
    
    public void stopBackgroundMusic() {
        if (playingBackgroundMusic) {
            mediaPlayer.reset();
            playingBackgroundMusic = false;
        }
    }
    
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }
}
