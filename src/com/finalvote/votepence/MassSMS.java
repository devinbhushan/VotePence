package com.finalvote.votepence;

import com.finalvote.votepence.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

public class MassSMS extends Activity {

	public final String FINAL_NOTIFICATION = "FINAL";
	public int numVotes;
	public static int notifyID;
	public int limit;
	public String message;
	public String number;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        ImageButton button = (ImageButton) findViewById(R.id.send);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Log.d("DEBUG", "Click received");

                message = ((EditText) findViewById(R.id.message)).getText().toString();
                number = ((EditText) findViewById(R.id.number)).getText().toString();
                EditText textLimit = (EditText) findViewById(R.id.quantity);
                limit = Integer.parseInt(textLimit.getText().toString());
                new SendVotes(getApplicationContext()).execute(MassSMS.this);
            }
        });
	}
}
