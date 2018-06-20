package developers.adpagri.com.androidthingstest;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.VideoView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    VideoView mainVideoWidget;
    public static int index;
    List<String> videoPathList;
    String currentVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainVideoWidget = findViewById(R.id.mainVideoView);
        //mainVideoWidget.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);

        videoPathList = new ArrayList<>();
        getAllVideoPaths();


//        mainVideoWidget.setVideoPath(videoPathList.get(0));
//        mainVideoWidget.setMediaController(new MediaController(this));
//        mainVideoWidget.requestFocus();

        index = 0;
        run_videos();
    }

    public void getAllVideoPaths(){
        Field [] id_fields = R.raw.class.getFields();

        Resources resources = getResources();
        String packageName = getPackageName();

        for (int i = 0; i < id_fields.length; i++) {
            String filename = id_fields[i].getName();
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

    public void run_videos(){
        currentVideoPath = videoPathList.get(index);
        mainVideoWidget.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int total_videos = videoPathList.size();
                if(index<total_videos-1){
                    index++;
                }
                else{
                    index=0;
                }
                currentVideoPath = videoPathList.get(index);
                mainVideoWidget.setVideoPath(currentVideoPath);
                mainVideoWidget.start();
            }
        });
        mainVideoWidget.setVideoPath(currentVideoPath);
        mainVideoWidget.start();
    }
}