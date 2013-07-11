/**
 * 
 */
package com.finalvote.votepence;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Devin
 *
 */
public class SendVotes extends AsyncTask<MassSMS, Integer, Integer> {
    
	private NotificationHelper mNotificationHelper;
	private int limit;
	private MassSMS smsObj;
    
	public SendVotes(Context context){
        mNotificationHelper = new NotificationHelper(context);
    }
 
    protected void onPreExecute(){
        //Create the notification in the statusbar
        mNotificationHelper.createNotification();
    }
 
    protected void onProgressUpdate(Integer... progress) {
        //This method runs on the UI thread, it receives progress updates
        //from the background thread and publishes them to the status bar
        mNotificationHelper.progressUpdate(progress[0]);
    }
    protected void onPostExecute(Integer result)    {
        //The task is complete, tell the status bar about it
        mNotificationHelper.completed(limit);
        
        String tweetUrl = "https://twitter.com/intent/tweet?text=I just voted " + limit + " times for Hunter Pence using the VotePence Android app http://bit.ly/185dn8Y";
        Uri uri = Uri.parse(tweetUrl);
        smsObj.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

	@Override
	protected Integer doInBackground(MassSMS... userInput) {
        // Context, 
        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        String LAST_MESSAGE = "FINISHED";
        smsObj = userInput[0];
        limit = smsObj.limit;
        final Counter votesSent = new Counter();
        
        while (votesSent.getNum() < limit) {
            PendingIntent sentPI = PendingIntent.getBroadcast(smsObj, 0, new Intent(SENT), 0);
            
            // ---when the SMS has been sent---
            smsObj.registerReceiver(new BroadcastReceiver() 
            {
                @Override
                public void onReceive(Context arg0, Intent arg1) 
                {
                    int resultCode = getResultCode();
                    switch (resultCode) 
                    {
    	                case Activity.RESULT_OK:
    	                	 Log.d("DEBUG", "Sent");
    	                	 //Toast.makeText(getBaseContext(), "SMS sent",Toast.LENGTH_SHORT).show();
    	                	 break;
    	                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
    	                	 Log.d("DEBUG", "generic failure");
    	                	 //Toast.makeText(getBaseContext(), "Generic failure",Toast.LENGTH_SHORT).show();
    	                	 break;
    	                case SmsManager.RESULT_ERROR_NO_SERVICE:
    	                	 Log.d("DEBUG", "no service");
    	                	 //Toast.makeText(getBaseContext(), "No service",Toast.LENGTH_SHORT).show();
    	                	 break;
    	                case SmsManager.RESULT_ERROR_NULL_PDU:
    	                	 Log.d("DEBUG", "null pdu");
    	                	 //Toast.makeText(getBaseContext(), "Null PDU",Toast.LENGTH_SHORT).show();
    	                	 break;
    	                case SmsManager.RESULT_ERROR_RADIO_OFF:
    	                	 Log.d("DEBUG", "radio off");
    	                	 //Toast.makeText(getBaseContext(), "Radio off",Toast.LENGTH_SHORT).show();
    	                	 break;
                    }
                    smsObj.unregisterReceiver(this);
                }
            }, new IntentFilter(SENT));
    		
            try {
    			SmsManager smsManager = SmsManager.getDefault();
    			smsManager.sendTextMessage(smsObj.number, null, smsObj.message, sentPI, null);
    			votesSent.increment();
                publishProgress(votesSent.getNum());
                
                ContentValues values = new ContentValues(); 
                values.put("address", smsObj.number); 
                values.put("body", smsObj.message);    
                smsObj.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
    		} catch (Exception e) {
    			Log.d("DEBUG", e.toString());
    		}
        }
        return 0;
	}

}
