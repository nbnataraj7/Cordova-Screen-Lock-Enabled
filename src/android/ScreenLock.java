package com.abarak64.screenlock;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.*;
import android.provider.*;
import android.app.*;
import android.os.Build;

public class ScreenLock extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        Context context=this.cordova.getActivity().getApplicationContext(); 
        boolean hasSecurity = doesDeviceHaveSecuritySetup(context);
        callbackContext.success(hasSecurity ? 1 : 0);

        return true;
    }

    private static boolean isPatternSet(Context context)
    {
        ContentResolver cr = context.getContentResolver();
        try
        {
            int lockPatternEnable = Settings.Secure.getInt(cr, Settings.Secure.LOCK_PATTERN_ENABLED);
            return lockPatternEnable == 1;
        }
        catch (Settings.SettingNotFoundException e)
        {
            return false;
        }
    }

    private static boolean doesDeviceHaveSecuritySetup(Context context)
    {
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE); //api 16+
                return keyguardManager.isKeyguardSecure();
            }
            else {
                return isPatternSet(context);
            }
        } catch(Exception e) {
            return false;
        }
    }
}
