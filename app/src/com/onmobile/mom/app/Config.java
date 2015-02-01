package com.onmobile.mom.app;

/**
 * This class defines all the static variables needed to configure the application scope.
 *
 * @author adetalouet
 */
public abstract class Config {

    /**
     * Application TAG
     */
    public static final String TAG_APP = "MOM";
    
    /**
     * Library log
     */
    public static final boolean ENABLE_SDK_LOG = false;

    // ---------------------------------------- USER INFO

    /**
     * User login
     */
    public static final String LOGIN = "Alex";
    /**
     * User password
     */
    public static final String PASSWORD = "onmobile";
    /**
     * User id
     */
    public static final int USER_ID = 61152;
    /**
     * User datasource
     */
    public static final String DATASOURCE = "User1";
    /**
     * User address book service id
     */
    public static final int AB_SERVICE_ID = 369359;
    /**
     * User call log service id
     */
    public static final int CALL_LOG_SERVICE_ID = 369366;
    /**
     * User event service id
     */
    public static final int EVENT_SERVICE_ID = 369361;
    /**
     * User sms service id
     */
    public static final int SMS_SERVICE_ID = 369362;


    // ---------------------------------------- ACCOUNT INFO

    /**
     * Device account name
     */
    public static final String ACCOUNT_NAME = "Alex";
    /**
     * Device account password
     */
    public static final String ACCOUNT_PASSWORD = "onmobile";
    /**
     * Device account type. Must be the same as xml/sync_calendar ;
     * xml/sync_contacts
     */
    public static final String ACCOUNT_TYPE = "com.onmobile.account";

    // ---------------------------------------- DATABASE INFO

    /**
     * Contact database's flag
     */
    public static final boolean SYNC_CONTACT = true;
    /**
     * Call log database's flag
     */
    public static final boolean SYNC_CALL_LOG = true;
    /**
     * SMS database's flag
     */
    public static final boolean SYNC_SMS = true;
    /**
     * Photo database's flag
     */
    public static final boolean SYNC_PHOTO = false;
    /**
     * Video database's flag
     */
    public static final boolean SYNC_VIDEO = false;
    /**
     * Event database's flag
     */
    public static final boolean SYNC_EVENT = true;

    // ---------------------------------------- URL INFO

    /**
     * Platform URL
     */
    public static final String PLATFORME_URL = "http://demo.dpu.onmobile.com/";
    /**
     * RestApi URL
     */
    public static final String RESTAPI_URL = PLATFORME_URL + "restapi8";
    /**
     * Sync URL
     */
    public static final String SYNCSERVER_URL = PLATFORME_URL + "sync8/s";
    /**
     * RestApi summary URL
     */
    public static final String SUMMARY_RESTAPI_URL = RESTAPI_URL + "/rest/user/" + USER_ID + "/datasource/" + DATASOURCE + "/summary";
    /**
     * RestApi contact list URL
     */
    public static final String CONTACT_LIST_RESTAPI_URL = RESTAPI_URL + "/rest/user/" + USER_ID + "/datasource/" + DATASOURCE + "/ab/" + AB_SERVICE_ID + "/contact";
    /**
     * RestApi sms list URL
     */
    public static final String SMS_LIST_RESTAPI_URL = RESTAPI_URL + "/rest/user/" + USER_ID + "/datasource/" + DATASOURCE + "/mstore/" + SMS_SERVICE_ID + "/message";
    /**
     * RestApi call log list URL
     */
    public static final String CALL_LOG_LIST_RESTAPI_URL = RESTAPI_URL + "/rest/user/" + USER_ID + "/datasource/" + DATASOURCE + "/callstore/" + CALL_LOG_SERVICE_ID + "/calllog";
    /**
     * RestApi summary URL formated in JSON
     */
    public static final String SUMMARY_JSON_RESTAPI_URL = SUMMARY_RESTAPI_URL + "?output=json";
    /**
     * RestApi delete database URL
     */
    public static final String DELETE_DATABASES_RESTAPI_URL = SUMMARY_RESTAPI_URL + "?target=IMAGE,VIDEO,CONTACT,EVENT,SMS,CALL_LOG";

    /*
     * HttpRequest timeout
     */
    public static final int QUERY_TIMEOUT = 30000; //30s

    // ---------------------------------------- DATABASES SIZE

    /**
     * Number of call log
     */
    public static final int NUMBER_OF_CALL_LOG = 100;
    /**
     * Number of call log to delete
     */
    public static final int NUMBER_OF_CALL_LOG_TO_DELETE = 5;
    /**
     * Number of call log to add
     */
    public static final int NUMBER_OF_CALL_LOG_TO_ADD = 10;
    /**
     * Total call log number -  depending on the sync mode, remove the deleted values or not
     */
    public static final int NUMBER_OF_CALL_LOG_FINAL = NUMBER_OF_CALL_LOG + NUMBER_OF_CALL_LOG_TO_ADD;


    /**
     * Number of contact
     */
    public static final int NUMBER_OF_CONTACT = 100;
    /**
     * AB size - we have 3 numbers per contact - half of the contact doesn't have phone number but only IM number
     */
    public static final int MAX_PHONE_NUMBER_IN_AB = NUMBER_OF_CONTACT * 3 / 2;
    /**
     * Number of contact to delete
     */
    public static final int NUMBER_OF_CONTACT_TO_DELETE = 5;
    /**
     * Number of contact to add
     */
    public static final int NUMBER_OF_CONTACT_TO_ADD = 10;
    /**
     * Total contact number
     */
    public static final int NUMBER_OF_CONTACT_FINAL = NUMBER_OF_CONTACT - NUMBER_OF_CONTACT_TO_DELETE + NUMBER_OF_CONTACT_TO_ADD;

    /**
     * Number of SMS
     */
    public static final int NUMBER_OF_SMS = 10;
    /**
     * Number of SMS to delete
     */
    public static final int NUMBER_OF_SMS_TO_DELETE = 5;
    /**
     * Number of SMS to add
     */
    public static final int NUMBER_OF_SMS_TO_ADD = 10;
    /**
     * Total SMS number -  depending on the sync mode, remove the deleted values or not
     */
    public static final int NUMBER_OF_SMS_FINAL = NUMBER_OF_SMS + NUMBER_OF_SMS_TO_ADD;


    /**
     * Number of PHOTO
     */
    public static final int NUMBER_OF_PHOTO = 5;//363;

    /**
     * Number of VIDEO
     */
    public static final int NUMBER_OF_VIDEO = 67;

    /**
     * Number of recurrent event
     */
    public static final int NUMBER_OF_RECURRENT_EVENT = 50;
    /**
     * Number of all day event
     */
    public static final int NUMBER_OF_ALL_DAY_EVENT = 50;
    /**
     * Number of simple event
     */
    public static final int NUMBER_OF_SIMPLE_EVENT = 50;
    /**
     * Number of birthday event
     */
    public static final int NUMBER_OF_BIRTHDAY_EVENT = 0;
    /**
     * Number of exception occurrence event
     */
    public static final int NUMBER_OF_EXCEPTION_OCCURRENCE_EVENT = 0;
    /**
     * Total event number
     */
    public static final int NUMBER_OF_EVENT = NUMBER_OF_RECURRENT_EVENT + NUMBER_OF_ALL_DAY_EVENT + NUMBER_OF_SIMPLE_EVENT + NUMBER_OF_BIRTHDAY_EVENT + NUMBER_OF_EXCEPTION_OCCURRENCE_EVENT;


    //--------------- Useful if {@link RESTUser} is used (createUser, getUser, deleteUser)
//	public static final String	FIRSTNAME							= "Test"; 
//	public static final String 	LASTNAME							= "QA"; 
//	public static final String	MSISDN								= "0600000000";
//	public static final String 	EMAIL								= "voxmobili.qa.test1@gmail.com"; 
//	public static final String 	RESTADMIN_URL						= PLATFORME_URL + "restadmin8";
//	public static final String 	CREATE_USER_RESTADMIN_URL 			= RESTADMIN_URL + "/rest/user/";
//	public static final String 	GET_USER_RESTADMIN_URL 				= RESTADMIN_URL + "/rest/user/?criteria=EMAIL&value=" + EMAIL + "&output=json";

}
