package szymanski.pawel.ihome.utils;

import java.io.IOException;

public class Utils {

    //FINALS----------------------------------------------------------------------------------------
    public static final String SERVER_ADRESS = "http://000.000.000.000:8080";





    //METHODS---------------------------------------------------------------------------------------
    public static boolean ping(String IP) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 " + IP);
            int mExitValue = mIpAddrProcess.waitFor();
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException | IOException ignore) {
            ignore.printStackTrace();
        }
        return false;
    }


}
