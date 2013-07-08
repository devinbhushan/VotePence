package com.finalvote.votepence;

import com.finalvote.votepence.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

public class MassSMS extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        ImageButton button = (ImageButton) findViewById(R.id.send);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Log.d("DEBUG", "Click received");

                String message = ((EditText) findViewById(R.id.message)).getText().toString();
                String number = ((EditText) findViewById(R.id.number)).getText().toString();
                int i = 0;
                EditText textLimit = (EditText) findViewById(R.id.quantity);
                int limit = Integer.parseInt(textLimit.getText().toString());
                
                while (i < limit) {
                    sendSMS(number, message);
                    i++;
                }

                Toast.makeText(MassSMS.this, "Sending " + limit + " messages to " + number, Toast.LENGTH_LONG).show();
            }
        });
	}
    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNumber, null, message, null, null);
		  } catch (Exception e) {
			Toast.makeText(getApplicationContext(),
				"SMS faild, please try again!",
				Toast.LENGTH_LONG).show();
			e.printStackTrace();
		  }
    }

}
