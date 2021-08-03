package com.f2f.incls.activity;


        import android.app.ActivityManager;
        import android.content.Context;
        import android.os.AsyncTask;

        import java.util.List;
        import java.util.Timer;
        import java.util.TimerTask;
        import java.util.concurrent.ExecutionException;

public class LogOutTimerUtil {


    public interface LogOutListener {
        void doLogout();
    }


    static Timer longTimer;
    static final int LOGOUT_TIME = 3600000; // delay in milliseconds i.e. 5 min = 300000 ms or use timeout argument

    public static synchronized void startLogoutTimer(final Context context, final LogOutListener logOutListener) {
        if (longTimer != null) {
            longTimer.cancel();
            longTimer = null;
        }
        if (longTimer == null) {

            longTimer = new Timer();

            longTimer.schedule(new TimerTask() {

                public void run() {

                    cancel();

                    longTimer = null;

                    try {
                        boolean foreGround = new ForegroundCheckTask().execute(context).get();

                        if (foreGround) {
                            logOutListener.doLogout();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }, LOGOUT_TIME);
        }
    }

    public static synchronized void stopLogoutTimer() {
        if (longTimer != null) {
            longTimer.cancel();
            longTimer = null;
        }
    }

    static class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

}

  /*  private void callAutoLogout() {
        Intent alaramIntent = new Intent(LoginActivity.this, BootCompletedIntentReceiver.class);
        alaramIntent.setAction("LogOutAction");
        Log.e("MethodCall","AutoLogOutCall");
        alaramIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);

        Log.e("Logout", "Auto Logout set at..!" + calendar.getTime());
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }*/


/*public class BootCompletedIntentReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(final Context context, Intent intent)
    {

        if("LogOutAction".equals(intent.getAction())){

            Log.e("LogOutAuto", intent.getAction());
            Toast.makeText(context, "Logout Action", Toast.LENGTH_SHORT).show();
            //Do your action
        }
    }
}*/


