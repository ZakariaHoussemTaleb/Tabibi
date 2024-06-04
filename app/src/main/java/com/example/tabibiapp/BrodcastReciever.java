package com.example.tabibiapp;





import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.os.AsyncTask;

import androidx.core.content.ContextCompat;


/*public class BrodcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = SplashScreen.checkInternet(context);
        if (status.equals("connected")){
            Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show();
        } else if (status.equals("disconnected")) {
            Toast.makeText(context, "disconnected", Toast.LENGTH_SHORT).show();
        }
    }
}*/
public class BrodcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new InternetCheckTask(context).execute();
    }
}

class InternetCheckTask extends AsyncTask<Void, Void, Boolean> {
    private Context context;

    public InternetCheckTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Process ipProcess = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean internetAvailable) {
        String status = internetAvailable ? "connected" : "disconnected";
        if (status.equals("disconnected")){
            showCustomDialog(context);
        } else if (status.equals("connected")) {
            if (context instanceof SplashScreen) {
                ((SplashScreen) context).updateUI();
            } else {
                Intent intent = new Intent(context, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
    private void showCustomDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialigue_box_no_internet);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.custom_dialogue));
        dialog.setCancelable(false);

        Button dialogueDepose = dialog.findViewById(R.id.exitinternet);
        dialogueDepose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (context instanceof SplashScreen) {
                    ((SplashScreen) context).finishAffinity();((SplashScreen) context).finishAndRemoveTask();
                }
            }
        });

        dialog.show();
    }
}
