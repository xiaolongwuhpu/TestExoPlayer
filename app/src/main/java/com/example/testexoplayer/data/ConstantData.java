package com.example.testexoplayer.data;

import android.content.Context;
import android.net.Uri;

import com.example.testexoplayer.DemoApplication;
import com.example.testexoplayer.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConstantData {

    public static String jiangsu_m3u8 = "http://223.110.245.157/ott.js.chinamobile.com/PLTV/3/224/3221225935/index.m3u8";///江苏体育
    public static String wuxing_m3u8 = "http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8";///五星体育


    public static String MP4url = "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4";
    public static String MP4url2 = "https://media.w3.org/2010/05/sintel/trailer.mp4";


    public static String mkvUrl = "https://storage.googleapis.com/exoplayer-test-media-1/mkv/android-screens-lavf-56.36.100-aac-avc-main-1280x720.mkv";

    public static String localAudio =  "asset:///beach.aac";
    public static String localVideo =  "asset:///beach.mp4";

//    public static String localVideo =  "asset:///test5.mkv";

    public static String localZiMu =  "asset:///zimu.ass";

    public static String localtvs=  "asset:///cctvs.m3u";

//    public static String  rawVidow =  "rawresource:///"+R.raw.beach;
//     public static String rawPath = "android.resource://" ;//+ DemoApplication.getPackageName() + "/" + R.raw.video;


    private static final String BASE_URL =
            "https://storage.googleapis.com/exoplayer-test-media-1/gen-4/";
    private static final String BASE_URL_SCREENS = BASE_URL + "screens/dash-vod-single-segment/";
    private static final String BASE_URL_COMMON_ENCRYPTION = BASE_URL + "common-encryption/";

    public static final String H264_MANIFEST = BASE_URL_SCREENS + "manifest-h264.mpd";
    public static final String H265_MANIFEST = BASE_URL_SCREENS + "manifest-h265.mpd";
    public static final String VP9_MANIFEST = BASE_URL_SCREENS + "manifest-vp9.mpd";
    public static final String H264_23_MANIFEST = BASE_URL_SCREENS + "manifest-h264-23.mpd";
    public static final String H264_24_MANIFEST = BASE_URL_SCREENS + "manifest-h264-24.mpd";
    public static final String H264_29_MANIFEST = BASE_URL_SCREENS + "manifest-h264-29.mpd";
    // Widevine encrypted content manifests.
    public static final String WIDEVINE_H264_MANIFEST = BASE_URL_SCREENS + "manifest-h264-enc.mpd";
    public static final String WIDEVINE_H265_MANIFEST = BASE_URL_SCREENS + "manifest-h265-enc.mpd";
    public static final String WIDEVINE_VP9_MANIFEST = BASE_URL_SCREENS + "manifest-vp9-enc.mpd";


    public static final String TS03 = "https://storage.googleapis.com/exoplayer-test-media-1/360/iceland0.ts";
    public static final String TS10 = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/gear1/fileSequence0.ts";


    public static final String SmoothStreaming_ism01 = "https://playready.directtaps.net/smoothstreaming/SSWSS720H264/SuperSpeedway_720.ism/Manifest";
    private ProgressiveMediaSource progressiveMediaSource;


    public static MediaSource buildMediaSource(Uri uri, Context context) {
        String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                context, userAgent);
        @C.ContentType int type = Util.inferContentType(uri);
        Log.d("ConstantData", "type=====" + type);
        switch (type) {
            case C.TYPE_DASH://0
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS://1
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS://2
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER://3
                return new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uri);
//                return new ExtractorMediaSource.Factory(dataSourceFactory);
//                        .createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    public static MediaSource buildOkHttpMediaSource(Uri uri, Context context) {
        String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
        MediaSource source = new ExtractorMediaSource(
                uri,
                new DefaultHttpDataSourceFactory(userAgent, null),
                new DefaultExtractorsFactory(),
                null,
                null
        );
        return source;
    }



    public static List<String> loadAssetData(Context context) {
        List<String> tvList = new ArrayList<String>();
        try {
            InputStream instream = context.getAssets().open("cctvs.txt");
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                while ((line = buffreader.readLine()) != null) {
                    tvList.add(line + "\n");
                }
                instream.close();
            }
            return tvList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  tvList;
    }

}
