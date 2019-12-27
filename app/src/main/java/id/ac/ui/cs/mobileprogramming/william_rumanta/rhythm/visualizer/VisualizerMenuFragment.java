/*
 * Copyright 2017 nekocode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.ObserverMedia;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.R;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.services.MediaPlayerService;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.render.GLScene;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.render.SceneController;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.render.VisualizerRenderer;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.scene.BasicSpectrumScene;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.scene.ChlastScene;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.scene.CircSpectrumScene;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.scene.EnhancedSpectrumScene;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.scene.InputSoundScene;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.scene.OriginScene;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.scene.RainbowSpectrumScene;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.scene.Sa2WaveScene;
import id.ac.ui.cs.mobileprogramming.william_rumanta.rhythm.visualizer.scene.WavesRemixScene;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class VisualizerMenuFragment extends Fragment implements Visualizer.OnDataCaptureListener, ObserverMedia {
    private static final int REQUEST_PERMISSION = 101;
    private FrameLayout mContainerView;
    private VisualizerRenderer mRender;
    private SceneController mSceneController;
    private List<Pair<String, ? extends GLScene>> mSceneList;
    private Visualizer mVisualizer;
    private int captureSize;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        final VisualizerMenuFragment ctx = this;
        mContainerView = new FrameLayout(getContext());

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        MediaPlayerService.addListener(ctx);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(getContext(), "Visualizer will not be shown", Toast.LENGTH_LONG)
                                .show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to show visualizer?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


        captureSize = Visualizer.getCaptureSizeRange()[1];
        captureSize = captureSize > 512 ? 512 : captureSize;

        return mContainerView;
    }

    public void onPlayMedia() {
        /*
          Setup texture view
         */
        final TextureView textureView = new TextureView(getContext());
        mContainerView.addView(textureView);
        textureView.setSurfaceTextureListener(mRender = new VisualizerRenderer(getContext(), captureSize / 2));
        textureView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(
                    View v, int left, int top, int right, int bottom,
                    int oldLeft, int oldTop, int oldRight, int oldBottom) {

                mRender.onSurfaceTextureSizeChanged(null, v.getWidth(), v.getHeight());
            }
        });
        textureView.requestLayout();

        mRender.setSceneController(mSceneController = new SceneController() {
            @Override
            public void onSetup(Context context, int audioTextureId, int textureWidth) {
                mSceneList = new ArrayList<>();
                GLScene defaultScene = new BasicSpectrumScene(context, audioTextureId, textureWidth);
                mSceneList.add(Pair.create("Basic Spectrum", defaultScene));
                mSceneList.add(Pair.create("Circle Spectrum", new CircSpectrumScene(context, audioTextureId, textureWidth)));
                mSceneList.add(Pair.create("Enhanced Spectrum", new EnhancedSpectrumScene(context, audioTextureId, textureWidth)));
                mSceneList.add(Pair.create("Input Sound", new InputSoundScene(context, audioTextureId, textureWidth)));
                mSceneList.add(Pair.create("Sa2Wave", new Sa2WaveScene(context, audioTextureId, textureWidth)));
                mSceneList.add(Pair.create("Waves Remix", new WavesRemixScene(context, audioTextureId, textureWidth)));
                mSceneList.add(Pair.create("Rainbow Spectrum", new RainbowSpectrumScene(context, audioTextureId, textureWidth)));
                mSceneList.add(Pair.create("Chlast", new ChlastScene(context, audioTextureId, textureWidth)));
                mSceneList.add(Pair.create("Origin Texture", new OriginScene(context, audioTextureId, textureWidth)));

                changeScene(defaultScene);
            }
        });

        System.out.println("====================TERPANGGILLL!!!!=====================");
        System.out.println(MediaPlayerService.getMediaPlayer().getAudioSessionId());
        if (MediaPlayerService.getMediaPlayer() != null) {
            mVisualizer = new Visualizer(MediaPlayerService.getMediaPlayer().getAudioSessionId());
            mVisualizer.setCaptureSize(captureSize);
            mVisualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, true);
            // Start capturing
            mVisualizer.setEnabled(true);
        }
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
        mRender.updateWaveFormFrame(new WaveFormFrame(waveform, 0, waveform.length / 2));
    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        mRender.updateFFTFrame(new FFTFrame(fft, 0, fft.length / 2));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (mSceneList != null) {
            int id = 0;
            for (Pair<String, ? extends GLScene> pair : mSceneList) {
                menu.add(0, id, id, pair.first);
                id ++;
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mSceneList != null && mSceneController != null) {
            final GLScene scene = mSceneList.get(item.getItemId()).second;
            mSceneController.changeScene(scene);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (MediaPlayerService.getMediaPlayer() != null) {
            mVisualizer.setEnabled(false);
        }
    }
}
