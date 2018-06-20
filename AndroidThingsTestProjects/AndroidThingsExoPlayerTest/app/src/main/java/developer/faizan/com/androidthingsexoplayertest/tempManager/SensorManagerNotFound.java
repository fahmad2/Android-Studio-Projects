package developer.faizan.com.androidthingsexoplayertest.tempManager;

/**
 * Created by urfi on 3/22/18.
 */

public class SensorManagerNotFound extends Exception {
    SensorManagerNotFound(){
        super("SensorManager was not found");
    }
}
