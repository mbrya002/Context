package apps.matts.contextlearning;

import android.app.Application;

/**
 * Created by mgrah on 11/28/2017.
 */

public class KeepVals extends Application {

    private boolean mGlobalSoundVar = true;



    public boolean getGlobalSoundValue() {
        return mGlobalSoundVar;
    }

    public void toggleGlobalSoundValue() {
        mGlobalSoundVar = !mGlobalSoundVar;
    }
}
