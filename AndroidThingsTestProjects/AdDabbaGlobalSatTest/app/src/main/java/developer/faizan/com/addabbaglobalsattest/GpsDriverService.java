package developer.faizan.com.addabbaglobalsattest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GpsDriverService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
