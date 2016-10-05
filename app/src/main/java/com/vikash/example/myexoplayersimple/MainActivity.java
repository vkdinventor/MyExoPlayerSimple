package com.vikash.example.myexoplayersimple;

import android.media.MediaCodec;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.SurfaceView;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;
    private SurfaceView surfaceView;
    private int RENDERER_COUNT = 300000;
    private int minBufferMs =    250000;
    private final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private final int BUFFER_SEGMENT_COUNT = 256;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView= (SurfaceView) findViewById(R.id.surfaceView);

        //String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:40.0) Gecko/20100101 Firefox/40.0";
        String userAgent = System.getProperty("http.agent");
        //String url = "https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4";
        String url = "http://ic1bLDDa:NoVNx7nC@172.16.12.100:80/openhome/streaming/channels/0/flv";


        Map<String, String> params = new HashMap<String, String>(1);
        final String cred = "ic1bLDDa" + ":" + "NoVNx7nC";
        final String auth = "Basic "+ Base64.encodeToString(cred.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);

        DefaultHttpDataSource dataSource = new DefaultHttpDataSource(userAgent, null);
        dataSource.setRequestProperty("Authorization", auth);
        dataSource.setRequestProperty("Accept", "...");

//        ExtractorSampleSource sampleSource = new ExtractorSampleSource(uri, source, extractor, 2,
//                BUFFER_SIZE);
//        MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(sampleSource,
//                null, true, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING, 5000, null, player.getMainHandler(),
//                player, 50);
//        MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource,
//                null, true, player.getMainHandler(), player);

     ////////////////////////////////////////////////////////////

        
        Allocator allocator = new DefaultAllocator(minBufferMs);
       // DataSource dataSource = new DefaultUriDataSource(this, null, userAgent);


        ExtractorSampleSource sampleSource = new ExtractorSampleSource( Uri.parse(url), dataSource, allocator,
                BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);

        MediaCodecVideoTrackRenderer videoRenderer = new
                MediaCodecVideoTrackRenderer(this, sampleSource, MediaCodecSelector.DEFAULT,
                MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);

        MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, MediaCodecSelector.DEFAULT);

        exoPlayer = ExoPlayer.Factory.newInstance(RENDERER_COUNT);
        exoPlayer.prepare(videoRenderer, audioRenderer);
        exoPlayer.sendMessage(videoRenderer,
                MediaCodecVideoTrackRenderer.MSG_SET_SURFACE,
                surfaceView.getHolder().getSurface());
        exoPlayer.setPlayWhenReady(true);

    }
}