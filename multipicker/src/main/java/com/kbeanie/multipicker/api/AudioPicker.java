package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.core.PickerManager;
import com.kbeanie.multipicker.core.threads.AudioProcessorThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to pick an audio file.
 */
public final class AudioPicker extends PickerManager {
    private final static String TAG = AudioPicker.class.getSimpleName();
    private AudioPickerCallback callback;

    private String mimeType = "audio/*";

    public AudioPicker(Activity activity) {
        super(activity, Picker.PICK_AUDIO);
    }

    public AudioPicker(Fragment fragment) {
        super(fragment, Picker.PICK_AUDIO);
    }

    public AudioPicker(android.app.Fragment appFragment) {
        super(appFragment, Picker.PICK_AUDIO);
    }

    public void setAudioPickerCallback(AudioPickerCallback callback) {
        this.callback = callback;
    }

    public void allowMultiple() {
        this.allowMultiple = true;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void pickAudio() {
        try {
            pick();
        } catch (PickerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String pick() throws PickerException {
        if (callback == null) {
            throw new PickerException("AudioPickerCallback is null!!! Please set one");
        }
        String action = Intent.ACTION_GET_CONTENT;
        Intent intent = new Intent(action);
        intent.setType(mimeType);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        pickInternal(intent, pickerType);
        return null;
    }

    @Override
    public void submit(int requestCode, int resultCode, Intent data) {
        if (requestCode != pickerType) {
            onError("onActivityResult requestCode is different from the type the chooser was initialized with.");
        } else {
            handleAudioData(data);
        }
    }

    private void handleAudioData(Intent intent) {
        List<String> uris = new ArrayList<>();
        if (intent != null) {
            if (intent.getDataString() != null) {
                String uri = intent.getDataString();
                Log.d(TAG, "handleAudioData: " + uri);
                uris.add(uri);
            } else if (intent.getClipData() != null) {
                ClipData clipData = intent.getClipData();
                Log.d(TAG, "handleAudioData: Multiple audios with ClipData");
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Log.d(TAG, "Item [" + i + "]: " + item.getUri().toString());
                    uris.add(item.getUri().toString());
                }
            }

            processFiles(uris);
        }
    }

    private void processFiles(List<String> uris) {
        AudioProcessorThread thread = new AudioProcessorThread(getContext(), getFileObjects(uris), cacheLocation);
        thread.setAudioPickerCallback(callback);
        thread.start();
    }

    private void onError(final String errorMessage) {
        try {
            if (callback != null) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(errorMessage);
                    }
                });
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private List<ChosenFile> getFileObjects(List<String> uris) {
        List<ChosenFile> files = new ArrayList<>();
        for (String uri : uris) {
            ChosenAudio audio = new ChosenAudio();
            audio.setQueryUri(uri);
            audio.setDirectoryType(Environment.DIRECTORY_MUSIC);
            audio.setType("audio");
            files.add(audio);
        }
        return files;
    }
}