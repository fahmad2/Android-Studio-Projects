package com.faizan.exoplayer2mobile.utils;


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;

import com.faizan.exoplayer2mobile.R;
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AssetPathUtils {

    private static final String TAG = "VideoPathUtils";

    public static void setVideoPaths(Context context, List<String> videoPathList){
        Field[] idFields = R.raw.class.getFields();

        Resources resources = context.getResources();
        String packageName = context.getPackageName();

        for (Field field: idFields) {
            String filename = field.getName();
            int raw_id = resources.getIdentifier(filename, "raw", packageName);
            TypedValue value = new TypedValue();
            resources.getValue(raw_id, value, true);
            String [] fullFilepath = value.string.toString().split("/");
            String fullFilename = fullFilepath[fullFilepath.length -1];
            String [] fullFilnameArr = fullFilename.split("\\.");
            String extension = fullFilnameArr[fullFilnameArr.length-1];

            if(extension.equalsIgnoreCase("mp4")) {
                String video_path = "android.resource://" + packageName + "/" + raw_id;
                videoPathList.add(video_path);
            }
        }
    }

    public static MediaSource[] getMediaSources(Context context){
        List<MediaSource> mediaSources = new ArrayList<>();
        Field[] idFields = R.raw.class.getFields();

        Resources resources = context.getResources();
        String packageName = context.getPackageName();

        for(Field filename: idFields) {
            int resourcesIdentifier = resources.getIdentifier(filename.getName(), "raw", packageName);

            if(!isVideoMp4(resources, resourcesIdentifier)){
                Log.d(TAG, "Skipping the video: "+filename.getName()+" as it is not MP4 format");
                continue;
            }
            else{
                Log.d(TAG, "The video format for: "+filename.getName()+" is MP4");
            }
            DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(resourcesIdentifier));
            final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(context);

            try {
                rawResourceDataSource.open(dataSpec);
            } catch (RawResourceDataSource.RawResourceDataSourceException e) {
                e.printStackTrace();
            }
            DataSource.Factory factory = new DataSource.Factory() {
                @Override
                public DataSource createDataSource() {
                    return rawResourceDataSource;
                }
            };

            MediaSource mediaSource = new ExtractorMediaSource(rawResourceDataSource.getUri(), factory, Mp4Extractor.FACTORY, null, null );
            mediaSources.add(mediaSource);
        }


        return mediaSources.toArray(new MediaSource[idFields.length]);
    }

    private static boolean isVideoMp4(Resources resources, int rawId){
        TypedValue value = new TypedValue();
        resources.getValue(rawId, value, true);
        String [] fullFilepath = value.string.toString().split("/");
        String fullFilename = fullFilepath[fullFilepath.length -1];
        String [] fullFilnameArr = fullFilename.split("\\.");
        String extension = fullFilnameArr[fullFilnameArr.length-1];


        return extension.equalsIgnoreCase("mp4");
    }

//    public static MediaSource[] getMediaSources(List<String> videoPathList,
//                                         List<MediaSource> mediaSources){
//
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
//                Util.getUserAgent(this, "myApplication"));
//
//        for (String s: videoPathList) {
//
//            Uri uri = Uri.parse(s);
//            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).
//                    createMediaSource(uri);
//
//            mediaSources.add(mediaSource);
//        }
//
//        return mediaSources.toArray(new MediaSource[mediaSources.size()]);
//    }

}
