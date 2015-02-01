package com.onmobile.mom.httprequest;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.onmobile.mom.app.Config;
import com.onmobile.mom.app.Utils;

/**
 * This class makes a web query in order to get the number of data added on the
 * PIM. Those results are used to check the test case success. We can also
 * delete the content of chosen databases.
 * 
 * @author adetalouet
 */

public class PIMRequest {

	/**
	 * Tag to debug
	 */
	private static final String TAG = "PIMSummary - ";

	// JSON keys
	/**
	 * Key to retrieve the call log count
	 */
	private static final String TAG_SUMMARY_CALL_LOG_NB = "calllogNb";
	/**
	 * Key to retrieve the contact count
	 */
	private static final String TAG_SUMMARY_CONTACT_NB = "contactNb";
	/**
	 * Key to retrieve the image count
	 */
	private static final String TAG_SUMMARY_IMAGE_NB = "imageNb";
	/**
	 * Key to retrieve the sms count
	 */
	private static final String TAG_SUMMARY_SMS_NB = "smsNb";
	/**
	 * Key to retrieve the video count
	 */
	private static final String TAG_SUMMARY_VIDEO_NB = "videoNb";
	/**
	 * Key to retrieve the event count
	 */
	private static final String TAG_SUMMARY_EVENT_NB = "eventNb";

	/**
	 * Contain the web response (summary)
	 */
	private InputStream is = null;

	// Value used for the checkResult
	/**
	 * Call log count
	 */
	private int mCallLogNb;
	/**
	 * Contact count
	 */
	private int mContactNb;
	/**
	 * SMS count
	 */
	private int mSmsNb;
	/**
	 * Image count
	 */
	private int mImageNb;
	/**
	 * Video count
	 */
	private int mVideoNb;
	/**
	 * Event count
	 */
	private int mEventNb;

	/**
	 * Constructor - lunch the query
	 */
	public PIMRequest() {
		getSummary();
		parseSummary();
	}

	/**
	 * Function to make the query and get the response
	 */
	private void getSummary() {

		Log.d(Config.TAG_APP, TAG
				+ "getSummary : get number of data per database");

		try {
			// Set the time out
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(httpParameters,
					Config.QUERY_TIMEOUT);

			// Set the BASIC authentication
			String auth = android.util.Base64.encodeToString((Config.LOGIN
					+ ":" + Config.PASSWORD).getBytes("UTF-8"),
					android.util.Base64.NO_WRAP);

			// Set the query
			HttpGet httpGet = new HttpGet(Config.SUMMARY_JSON_RESTAPI_URL);
			httpGet.addHeader("Authorization", "Basic " + auth);

			Log.d(Config.TAG_APP, TAG + httpGet.getRequestLine());

			// Create the client, execute the query and get the response
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpResponse response = client.execute(httpGet);
			HttpEntity httpEntity = response.getEntity();
			is = httpEntity.getContent();

			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e(Config.TAG_APP, TAG + "getSummary : statusCode="
						+ statusCode);
			} else
				Log.d(Config.TAG_APP, TAG + response.getStatusLine());

		} catch (Exception e) {
			Log.e(Config.TAG_APP, TAG + "getSummary : " + e);
		}
	}

	/**
	 * Function to parse to response
	 */
	private void parseSummary() {
		JSONObject c = Utils.getJSONObject(is);

		// Log.d(Config.TAG_APP, TAG + "parseSummary : " + c);

		try {
			mCallLogNb = c.getInt(TAG_SUMMARY_CALL_LOG_NB);
			mContactNb = c.getInt(TAG_SUMMARY_CONTACT_NB);
			mImageNb = c.getInt(TAG_SUMMARY_IMAGE_NB);
			mSmsNb = c.getInt(TAG_SUMMARY_SMS_NB);
			mVideoNb = c.getInt(TAG_SUMMARY_VIDEO_NB);
			mEventNb = c.getInt(TAG_SUMMARY_EVENT_NB);

		} catch (JSONException e) {
			Log.e(Config.TAG_APP, TAG + "parseSummary : " + e);
		}
	}

	/**
	 * Delete the PIM databases content : CONTACT, SMS, IMAGE, VIDEO, CALL_LOG,
	 * EVENT
	 */
	public static void deleteDatabases() {
		Log.d(Config.TAG_APP, TAG + "-- deleteServerDatabase --");

		try {
			// Set the time out
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(httpParameters,
					Config.QUERY_TIMEOUT);

			// Set the BASIC authentication
			String auth = android.util.Base64.encodeToString((Config.LOGIN
					+ ":" + Config.PASSWORD).getBytes("UTF-8"),
					android.util.Base64.NO_WRAP);

			// Set the query
			HttpDelete httpDelete = new HttpDelete(
					Config.DELETE_DATABASES_RESTAPI_URL);
			httpDelete.addHeader("Authorization", "Basic " + auth);

			Log.d(Config.TAG_APP, TAG + httpDelete.getRequestLine());

			// Create the client and execute the query
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpResponse response = client.execute(httpDelete);

			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e(Config.TAG_APP, TAG + "deleteDatabases : statusCode="
						+ statusCode);
			} else
				Log.d(Config.TAG_APP, TAG + response.getStatusLine());

		} catch (Exception e) {
			Log.e(Config.TAG_APP, TAG + "deleteServerDatabase : " + e);
		}
	}

	/**
	 * This function make an HttpRequest in order to retrieve a list of data
	 * format in XML.
	 * 
	 * @param url
	 *            - url where to get the data
	 * @param type
	 *            - type of data
	 * @return The XML formated in a String
	 */
	public static String getListXML(String url, String type) {

		Log.d(Config.TAG_APP, TAG + "-- get" + type + "ListXML --");

		try {
			// Set the time out
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(httpParameters,
					Config.QUERY_TIMEOUT);

			// Set the BASIC authentication
			String auth = android.util.Base64.encodeToString((Config.LOGIN
					+ ":" + Config.PASSWORD).getBytes("UTF-8"),
					android.util.Base64.NO_WRAP);

			// Set the query
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Authorization", "Basic " + auth);
			httpGet.addHeader("Accept", "application/xml");

			Log.d(Config.TAG_APP, TAG + httpGet.getRequestLine());

			// Create the client, execute the query and get the response
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpResponse response = client.execute(httpGet);

			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e(Config.TAG_APP, TAG + "get" + type
						+ "ListXML : statusCode=" + statusCode);
				return null;
			} else
				Log.d(Config.TAG_APP, TAG + response.getStatusLine());

			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null)
				return EntityUtils.toString(httpEntity);

		} catch (Exception e) {
			Log.e(Config.TAG_APP, TAG + "get" + type + "ListXML : " + e);
		}

		return null;
	}

	/**
	 * Get the PIM call log count
	 * 
	 * @return the mCallLogNb
	 */
	public int getCallLogNb() {
		return mCallLogNb;
	}

	/**
	 * Get the PIM contact count
	 * 
	 * @return the mContactNb
	 */
	public int getContactNb() {
		return mContactNb;
	}

	/**
	 * Get the PIM SMS count
	 * 
	 * @return the mSmsNb
	 */
	public int getSmsNb() {
		return mSmsNb;
	}

	/**
	 * Get the PIM images count
	 * 
	 * @return the mImageNb
	 */
	public int getImageNb() {
		return mImageNb;
	}

	/**
	 * Get the PIM videos count
	 * 
	 * @return the mVideoNb
	 */
	public int getVideoNb() {
		return mVideoNb;
	}

	/**
	 * Get the PIM events count
	 * 
	 * @return the mEventNb
	 */
	public int getEventNb() {
		return mEventNb;
	}
}
