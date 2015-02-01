package com.onmobile.mom.xmlparser;

/**
 * This class allow to convert keys from the PIM to Android. In fact, when a
 * sync is done, some keys are not persistent between the services. So in order
 * to maintain the test, and to check the values, we need to use this mappinp
 * class.
 *
 * @author adetalouet
 */
public abstract class Mapping {

    //
    //
    // Android
    // ---------------------------------------------------------------------------------------------
    //
    //
    /**
     * Android mobile key
     */
    public static final String TEL_CELL = "Mobile";
    /**
     * Android fax key
     */
    public static final String TEL_FAX = "Fax";
    /**
     * Android pager key
     */
    public static final String TEL_PAGER = "Pager";
    /**
     * Android voice key
     */
    public static final String TEL_VOICE = "";
    // Main
    // ISDN
    // Company Main
    // Radio
    // Telex
    // TTY TDD
    // Assistant
    // MMS
    // Car
    // Callback

    /**
     * Android IM AIM key
     */
    public static final String IM_AIM = "AIM";
    /**
     * Android IM ICQ key
     */
    public static final String IM_ICQ = "ICQ";
    /**
     * Android IM Yahoo key
     */
    public static final String IM_YAHOO = "Yahoo";
    /**
     * Android IM Skype key
     */
    public static final String IM_SKYPE = "Skype";
    /**
     * Android IM Google Talk key
     */
    public static final String IM_GTALK = "Google Talk";
    /**
     * Android IM Jabber key
     */
    public static final String IM_JABBER = "Jabber";
    /**
     * Android IM Windows Live key
     */
    public static final String IM_MSN = "Windows Live";
    /**
     * Android IM NetMeeting key
     */
    public static final String IM_ = "NetMeeting";

    /**
     * Android type Work key
     */
    public static final String PRO = "Work";
    /**
     * Android type Home key
     */
    public static final String PERSO = "Home";
    /**
     * Android type Unknown key
     */
    public static final String UNKNOWN = " ";
    /**
     * Android type Other key
     */
    public static final String OTHER = "Other";

    /**
     * Android incoming call key
     */
    public static final String FROM = "1";
    /**
     * Android outgoing call key
     */
    public static final String TO = "2";
    /**
     * Android missed call key
     */
    public static final String MISSED = "3";

    /**
     * Android SMS inbox folder key
     */
    public static final String SMS_INBOX_FOLDER = "inbox";
    /**
     * Android SMS outbox folder key
     */
    public static final String SMS_SENT_FOLDER = "sent";

    //
    //
    // PIM
    // --------------------------------------------------------------------------------------------
    //
    //
    /**
     * PIM mobile key
     */
    public static final String Mobile = "TELL_CELL";
    /**
     * PIM fax key
     */
    public static final String Fax = "TEL_FAX";
    /**
     * PIM pagger key
     */
    public static final String Pager = "TEL_PAGER";
    /**
     * PIM voice key
     */
    public static final String Empty = "TELL_VOICE";
//	public static final String Main 			= " ";
//	public static final String ISDN 			= " ";
//	public static final String Company_Main 	= " ";
//	public static final String Radio 			= " ";
//	public static final String Telex 			= " ";
//	public static final String TTY_TTD 			= " ";
//	public static final String Assistant 		= " ";
//	public static final String MMS 				= " ";
//	public static final String Car 				= " ";
//	public static final String Callback 		= " ";


    /**
     * PIM IM AIN key
     */
    public static final String AIM = "IM_AIM";
    /**
     * PIM IM ICQ key
     */
    public static final String ICQ = "IM_ICQ";
    /**
     * PIM IM Yahoo key
     */
    public static final String Yahoo = "IM_YAHOO";
    /**
     * PIM IM Skype key
     */
    public static final String Skype = "IM_SKYPE";
    /**
     * PIM IM Google Talk key
     */
    public static final String Google_Talk = "IM_GTALK";
    /**
     * PIM IM Jabber key
     */
    public static final String Jabber = "IM_JABBER";
    /**
     * PIM IM Windows Live key
     */
    public static final String Window_Live = "IM_MSN";
    /**
     * PIM IM NetMeeting key
     */
    public static final String NetMeeting = " ";

    /**
     * PIM type Work key
     */
    public static final String Work = "PRO";
    /**
     * PIM type Home key
     */
    public static final String Home = "PERSO";
    /**
     * PIM type Unknown key
     */
    public static final String UOther = "UNKNOWN";
    /**
     * PIM type Work key
     */
    public static final String Other = "OTHER";

    /**
     * PIM incoming call key
     */
    public static final String From = "FROM";
    /**
     * PIM outgoing call key
     */
    public static final String To = "TO";
    /**
     * PIM missed call key
     */
    public static final String Missed = "MISSED";

    /**
     * PIM SMS outbox key
     */
    public static final String SMS_sent_folder = "Outbox";
    /**
     * PIM SMS inbox key
     */
    public static final String SMS_inbox_folder = "Inbox";


    /**
     * This function map a PIM key with the corresponding Android key.
     *
     * @param toMap - the PIM key to convert
     * @return the Android key
     */
    public static String PIMtoAndroid(String toMap) {

        if (toMap.equals(Mobile))
            return TEL_CELL;

        if (toMap.equals(Fax))
            return TEL_FAX;

        if (toMap.equals(Pager))
            return TEL_PAGER;

        if (toMap.equals(""))
            return TEL_VOICE;

        if (toMap.equals(AIM))
            return IM_AIM;

        if (toMap.equals(ICQ))
            return IM_ICQ;

        if (toMap.equals(Yahoo))
            return IM_YAHOO;

        if (toMap.equals(Skype))
            return IM_SKYPE;

        if (toMap.equals(Google_Talk))
            return IM_GTALK;

        if (toMap.equals(Jabber))
            return IM_JABBER;

        if (toMap.equals(Window_Live))
            return IM_MSN;

        if (toMap.equals(Work))
            return PRO;

        if (toMap.equals(Home))
            return PERSO;

        if (toMap.equals(Other))
            return OTHER;

        if (toMap.equals(From))
            return FROM;

        if (toMap.equals(To))
            return TO;

        if (toMap.equals(Missed))
            return MISSED;

        if (toMap.equals(SMS_sent_folder))
            return SMS_SENT_FOLDER;

        if (toMap.equals(SMS_inbox_folder))
            return SMS_INBOX_FOLDER;

        return null;
    }

}
