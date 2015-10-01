package com.example.cellshock;

import java.util.Collection;
import java.util.HashMap;

import android.content.Context;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
//by Robert Green
public class MusicManager {
private static final String TAG = "MusicManager";
public static final int MUSIC_PREVIOUS = -1;
public static final int MUSIC_MENU = 0;
public static final int MUSIC_GAME = 1;
//private static final String MediaPlayer = null;

private static HashMap<Integer, MediaPlayer> players = new HashMap();
//private static SparseArray<MediaPlayer> players = new SparseArray<MediaPlayer>();
private static int currentMusic = -1;
private static int previousMusic = -1;

public static void start(Context context, int music) {
start(context, music, false);
}

public static void start(Context context, int music, boolean force) {
	if (!force && currentMusic > -1) {
		// already playing some music and not forced to change
		return;
	}
	if (music == MUSIC_PREVIOUS) {
		Log.d(TAG, "Using previous music [" + previousMusic + "]");
		music = previousMusic;
	}
	if (currentMusic == music) {
		// already playing this music
		return;
	}
	if (currentMusic != -1) {
		previousMusic = currentMusic;
		Log.d(TAG, "Previous music was [" + previousMusic + "]");
		// playing some other music, pause it and change
		pause();
	}
	
	currentMusic = music;
	Log.d(TAG, "Current music is now [" + currentMusic + "]");
	MediaPlayer mp = players.get(music);
	if (mp != null) {
		if (!mp.isPlaying()) {
			mp.start();
		}
	} else {
		if (music == MUSIC_MENU) {
			mp = MediaPlayer.create(context, R.raw.menumusic);
		} else if (music == MUSIC_GAME) {
			mp = MediaPlayer.create(context, R.raw.gamemusic);
		
		} else {
			Log.e(TAG, "unsupported music number - " + music);
			return;
		}
		
		players.put(music, mp);
		mp.setVolume(1, 1);
		if (mp == null) {
			Log.e(TAG, "player was not created successfully");
		} else {
			try {
				mp.setLooping(true);
				mp.start();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}
}
public static void pause() {
Collection<MediaPlayer> mps = players.values();
for (MediaPlayer p : mps) {
if (p.isPlaying()) {
p.pause();
}
}
// previousMusic should always be something valid
if (currentMusic != -1) {
previousMusic = currentMusic;
Log.d(TAG, "Previous music was [" + previousMusic + "]");
}
currentMusic = -1;
Log.d(TAG, "Current music is now [" + currentMusic + "]");
}

public static void release() {
Log.d(TAG, "Releasing media players");
Collection<MediaPlayer> mps = players.values();
for (MediaPlayer mp : mps) {
try {
if (mp != null) {
if (mp.isPlaying()) {
mp.stop();
}
mp.release();
}
} catch (Exception e) {
Log.e(TAG, e.getMessage(), e);
}
}
mps.clear();
if (currentMusic != -1) {
previousMusic = currentMusic;
Log.d(TAG, "Previous music was [" + previousMusic + "]");
}
currentMusic = -1;
Log.d(TAG, "Current music is now [" + currentMusic + "]");
}
}