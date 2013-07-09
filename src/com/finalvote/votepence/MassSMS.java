package com.finalvote.votepence;

import com.finalvote.votepence.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
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
            	//Log.d("DEBUG", "Click received");

                String message = ((EditText) findViewById(R.id.message)).getText().toString();
                String number = ((EditText) findViewById(R.id.number)).getText().toString();
                int i = 0;
                EditText textLimit = (EditText) findViewById(R.id.quantity);
                int limit = Integer.parseInt(textLimit.getText().toString());
                
                while (i < limit) {
	                sendSMS(number, message, limit);
	            	
	                ContentValues values = new ContentValues(); 
	                values.put("address", number); 
	                values.put("body", message);    
	                getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	                //Log.d("DEBUG", "Sending message #" + i);
	                i++;
                }
                
                Toast.makeText(MassSMS.this, "Successfully sent " + limit + " messages to " + number, Toast.LENGTH_LONG).show();
            }
        });
	}
    private void sendSMS(String phoneNumber, String message, int remainingTexts) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        
        registerReceiver(new BroadcastReceiver() 
        {
            @Override
            public void onReceive(Context arg0, Intent arg1) 
            {
                int resultCode = getResultCode();
                switch (resultCode) 
                {
	                case Activity.RESULT_OK:
	                	 Log.d("DEBUG", "Sent");
	                	 //Toast.makeText(getBaseContext(), "SMS sent",Toast.LENGTH_LONG).show();
	                	 break;
	                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	                	 Log.d("DEBUG", "generic failure");
	                	 //Toast.makeText(getBaseContext(), "Generic failure",Toast.LENGTH_LONG).show();
	                	 break;
	                case SmsManager.RESULT_ERROR_NO_SERVICE:
	                	 Log.d("DEBUG", "no service");
	                	 //Toast.makeText(getBaseContext(), "No service",Toast.LENGTH_LONG).show();
	                	 break;
	                case SmsManager.RESULT_ERROR_NULL_PDU:
	                	 Log.d("DEBUG", "null pdu");
	                	 //Toast.makeText(getBaseContext(), "Null PDU",Toast.LENGTH_LONG).show();
	                	 break;
	                case SmsManager.RESULT_ERROR_RADIO_OFF:
	                	 Log.d("DEBUG", "radio off");
	                	 //Toast.makeText(getBaseContext(), "Radio off",Toast.LENGTH_LONG).show();
	                	 break;
                }
                MassSMS.this.unregisterReceiver(this);
            }
        }, new IntentFilter(SENT));


        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNumber, null, message, sentPI, null);
		  } catch (Exception e) {
				Log.d("DEBUG", e.toString());
		  }
    }

}
