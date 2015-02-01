package com.onmobile.mom.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

import com.onmobile.mom.accountmanager.MyAccount;
import com.onmobile.mom.app.Config;

/**
 * This class manage a Calendar. In order to do so, we had to create
 * {@link MyAccount} to allow calendar creation. This class provides function to
 * create events. The types of event created for the test is define in the
 * specification, and their number can be set in the {@link Config} class
 *
 * @author adetalouet
 */
public class MyCalendar implements IDatabase {

    /**
     * Tag to debug
     */
    private static final String TAG = "MyCalendar - ";
    /**
     * Test case context
     */
    private Context mContext;
    /**
     * Calendar ID; updated once the calendar is created.
     */
    private long mCalId;

    /**
     * Constructor
     *
     * @param context - the context of the test case
     */
    public MyCalendar(Context context) {
        mContext = context;
    }

    /**
     * This function create a calendar in the created account ({@link MyAccount})
     */
    public void createCalendar() {

        ContentResolver cr = mContext.getContentResolver();

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, Config.ACCOUNT_NAME);
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, Config.ACCOUNT_TYPE);
        values.put(CalendarContract.Calendars.NAME, "MyCalendar");
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                "MyCalendar");
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0x000000);
        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(CalendarContract.Calendars.OWNER_ACCOUNT,
                Config.ACCOUNT_NAME);
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        values.put(CalendarContract.Calendars.VISIBLE, 1);

        Uri creationUri = asSyncAdapter(CalendarContract.Calendars.CONTENT_URI,
                Config.ACCOUNT_NAME, Config.ACCOUNT_TYPE);
        Uri created = cr.insert(creationUri, values);
        mCalId = Long.parseLong(created.getLastPathSegment());

        Log.d(Config.TAG_APP, TAG + "Calendar created: " + mCalId + " - " + created);
    }

    /**
     * This function gives the created calendar Uri
     *
     * @param uri         - {@code CalendarContract.Calendars.CONTENT_URI}
     * @param accountName - the name of {@link MyAccount}
     * @param accountType - the type of {@link MyAccount}
     * @return The Uri of the created calendar
     */
    private Uri asSyncAdapter(Uri uri, String accountName, String accountType) {
        return uri
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,
                        "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME,
                        accountName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        accountType).build();
    }

    /**
     * This function creates a specific event
     *
     * @param isSingle - whether the event is a single event
     * @param isAllDay - whether the event is an all day event
     * @param title    - the event title
     * @param hour     - the event start hour (if>24, will be the next day. For
     *                 example hour = 36, the event will be at noon the day after the
     *                 creation date
     */
    private void createEvent(boolean isSingle, boolean isAllDay, String title,
                             int hour) {

        int allDay = 0;
        if (isAllDay)
            allDay = 1;

        long calId = 1;

        // Create the event's start and end time
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long start = cal.getTimeInMillis();
        cal.set(Calendar.HOUR, hour + 1);
        long end = cal.getTimeInMillis();

        // Create the event using the ContentProvider given by Android
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, start);
        values.put(CalendarContract.Events.DTEND, end);
        // If the event is recurrent, the pattern is : Daily event on working days for 20 days
        if (!isSingle)
            values.put(CalendarContract.Events.RRULE,
                    "FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO");
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.EVENT_LOCATION, "Copacabana");
        values.put(CalendarContract.Events.CALENDAR_ID, calId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Berlin");
        values.put(CalendarContract.Events.DESCRIPTION,
                "This is the automatic qa application system");
        values.put(CalendarContract.Events.ACCESS_LEVEL,
                CalendarContract.Events.ACCESS_PRIVATE);
        values.put(CalendarContract.Events.ALL_DAY, allDay);
        values.put(CalendarContract.Events.AVAILABILITY,
                CalendarContract.Events.AVAILABILITY_BUSY);

        // ------
        // If we want to make invitation, set those keys with the affected
        // values
        // ------
        // values.put(CalendarContract.Events.ORGANIZER,
        // "some.mail@some.address.com");
        // values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS,
        // CalendarContract.Events.STATUS_CONFIRMED);
        // values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1);
        // values.put(CalendarContract.Events.GUESTS_CAN_MODIFY, 1);

        mContext.getContentResolver().insert(
                CalendarContract.Events.CONTENT_URI, values);
    }

    @Override
    public void createDataSet() {

        createCalendar();

        Log.d(Config.TAG_APP, TAG + "createDataSet started");

        for (int i = 0; i < Config.NUMBER_OF_SIMPLE_EVENT; i++)
            createEvent(true, false, "Single " + i, 8 + i + 24 * i);

        for (int i = 0; i < Config.NUMBER_OF_ALL_DAY_EVENT; i++)
            createEvent(true, true, "All day " + i, 24 * 3 * i);

        for (int i = 0; i < Config.NUMBER_OF_RECURRENT_EVENT; i++)
            createEvent(false, false, "Reccurrent " + i, 1 + 2 * i + 24 * 7 * i);

        // TODO BIRTHDAY EVENT AND EXCEPTION OCCURRENCE EVENT

        Log.d(Config.TAG_APP, TAG + "create " + Config.NUMBER_OF_EVENT + " Events");

    }

    @Override
    public void deleteAll() {
        int d = 0;

        for (Integer id : getEventIds()) {
            mContext.getContentResolver().delete(
                    CalendarContract.Events.CONTENT_URI, CalendarContract.Events._ID + " =?", new String[]{String.valueOf(id)});
            d++;
        }

        Log.d(Config.TAG_APP, TAG + " delete " + d + " Events");
    }

    @Override
    public int getCount() {
        int nb = -1;
        Cursor phones = mContext.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI, null, null, null,
                null);
        if (phones != null) {
            nb = phones.getCount();
            phones.close();
        }
        return nb;
    }

    /**
     * This function goes through the Calendar and save the eventId of each entry.
     */
    private List<Integer> getEventIds() {

        List<Integer> mEventId = new ArrayList<Integer>();

        Cursor phones = mContext.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI, null, null, null,
                null);
        if (phones != null) {
            while (phones.moveToNext()) {
                mEventId.add(phones.getInt(phones
                        .getColumnIndexOrThrow(PhoneLookup._ID)));
            }
            phones.close();
        }


        return mEventId;
    }

    public void deleteCalendar() {
        Log.d(Config.TAG_APP, TAG + " delete Calendar");
        mContext.getContentResolver().delete(
                CalendarContract.Calendars.CONTENT_URI, CalendarContract.Calendars._ID + " =?", new String[]{String.valueOf(mCalId)});
    }

    /**
     * Get the calendar ID
     *
     * @return the calendar ID
     */
    public long getCalId() {
        return mCalId;
    }


}
