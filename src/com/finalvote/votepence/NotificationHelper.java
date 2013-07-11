/**
 * 
 */
package com.finalvote.votepence;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * @author Devin
 *
 */
public class NotificationHelper {
    private Context mContext;
    private int NOTIFICATION_ID = 1;
    private int FINAL_NOTIFICATION = 2;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private PendingIntent mContentIntent;
    private CharSequence mContentTitle;
    public NotificationHelper(Context context)
    {
        mContext = context;
    }
 
    /**
     * Put the notification into the status bar
     */
    public void createNotification() {
        //get the notification manager
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
 
        //create the notification
        int icon = R.drawable.hunterpenceicon;
        CharSequence tickerText = "Voting in Progress..."; //Initial text that appears in the status bar
        long when = System.currentTimeMillis();
        mNotification = new Notification(icon, tickerText, when);
 
        //create the content which is shown in the notification pulldown
        mContentTitle = "#VotePence"; //Full title of the notification in the pull down
        CharSequence contentText = "Voting in Progress..."; //Text of the notification in the pull down
 
        //you have to set a PendingIntent on a notification to tell the system what you want it to do when the notification is selected
        //I don't want to use this here so I'm just creating a blank one
        Intent notificationIntent = new Intent();
        mContentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
 
        //add the additional content and intent to the notification
        mNotification.setLatestEventInfo(mContext, mContentTitle, contentText, mContentIntent);
 
        //make this notification appear in the 'Ongoing events' section
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
 
        //show the notification
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }
 
    /**
     * Receives progress updates from the background task and updates the status bar notification appropriately
     * @param percentageComplete
     */
    public void progressUpdate(int votesSent) {
        //build up the new status message
        CharSequence contentText = String.valueOf(votesSent) + " votes sent...";
        //publish it to the status bar
        mNotification.setLatestEventInfo(mContext, mContentTitle, contentText, mContentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }
 
    /**
     * called when the background task is complete, this removes the notification from the status bar.
     * We could also use this to add a new ‘task complete’ notification
     */
    public void completed(int numVotes)    {
        //remove the notification from the status bar
        mNotificationManager.cancel(NOTIFICATION_ID);
        final String FINISHED = "FINISHED";
        PendingIntent finalNotification = PendingIntent.getBroadcast(mContext, 0, new Intent(FINISHED), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
        .setContentTitle("#VotePence")
        .setSmallIcon(R.drawable.hunterpenceicon)
        .setContentIntent(finalNotification);
        
        builder.setContentText("Finished sending " + numVotes + " votes for Hunter Pence!");
        // Because the ID remains unchanged, the existing notification is
        // updated.
        mNotificationManager.notify(
                FINAL_NOTIFICATION,
                builder.build());
    }
}
