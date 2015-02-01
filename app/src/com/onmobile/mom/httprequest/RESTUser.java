package com.onmobile.mom.httprequest;


/**
 * This class makes web query through the RESTADMIN URL in order to create, get
 * and delete a user. For the moment it is not use as we create a PIM user once
 * and we just work with his databases (cf {@link PIMRequest})
 *
 * @author adetalouet
 */
public abstract class RESTUser {

    // private static final String TAG = "RESTUser - ";
    //
    // private static final String KEY_FIRSTNAME = "firstname";
    // private static final String KEY_LASTNAME = "lastname";;
    // private static final String KEY_LOGIN = "login";
    // private static final String KEY_PASSWORD = "password";
    // private static final String KEY_MSISDN = "msisdn";
    // private static final String KEY_EMAIL = "email";
    //
    // private static final String USER_ID = "user";
    // private static final String USER_DATASOURCE = "datasource";
    //
    // private static final String DESCRIPTION = "description";
    // private static final String USER_NOT_FOUND = "User not found";
    //
    // private static final String HEADER_LOCATION = "Location";
    //
    // private static	 String USER_LOCATION_URL;

    /**
     * This funtion creates the user
     */
    public static void createUser() {

        // Log.d(Config.TAG_APP, TAG + "createUser -----------------------");
        //
        // List<NameValuePair> params = new ArrayList<NameValuePair>();
        // params.add(new BasicNameValuePair(KEY_FIRSTNAME, Config.FIRSTNAME));
        // params.add(new BasicNameValuePair(KEY_LASTNAME, Config.LASTNAME));
        // params.add(new BasicNameValuePair(KEY_LOGIN, Config.LOGIN));
        // params.add(new BasicNameValuePair(KEY_PASSWORD, Config.PASSWORD));
        // params.add(new BasicNameValuePair(KEY_MSISDN, Config.MSISDN));
        // params.add(new BasicNameValuePair(KEY_EMAIL, Config.EMAIL));
        //
        // try {
        // // Set the time out
        // HttpParams httpParameters = new BasicHttpParams();
        // HttpConnectionParams.setSoTimeout(httpParameters,
        // Config.QUERY_TIMEOUT);
        //
        // // Set the query
        // HttpPost httpPost = new HttpPost(Config.CREATE_USER_RESTADMIN_URL);
        // httpPost.setEntity(new UrlEncodedFormEntity(params));
        //
        // // Create the client and execute the query
        // DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        // HttpResponse httpResponse = httpClient.execute(httpPost);
        //
        // // Get all headers
        // Header[] headers = httpResponse.getAllHeaders();
        // for (Header header : headers) {
        // Log.d(Config.TAG_APP, TAG + "Key : " + header.getName()
        // + " ,Value : " + header.getValue());
        //
        // // Save the user location
        // if (header.getName().equals(HEADER_LOCATION))
        // USER_LOCATION_URL = header.getValue();
        //
        // }
        // } catch (UnsupportedEncodingException e) {
        // Log.e(Config.TAG_APP, TAG + "createUser : " + e);
        // e.printStackTrace();
        // } catch (ClientProtocolException e) {
        // Log.e(Config.TAG_APP, TAG + "createUser : " + e);
        // e.printStackTrace();
        // } catch (IOException e) {
        // Log.e(Config.TAG_APP, TAG + "createUser : " + e);
        // e.printStackTrace();
        // }
        //
        // setUserIdAndDatasource();
    }

    /**
     * This function deletes the user.
     */
    public static void deleteUser() {

        // Log.d(Config.TAG_APP, TAG + "deleteUser -----------------------");
        //
        // try {
        // // Set the time out
        // HttpParams httpParameters = new BasicHttpParams();
        // HttpConnectionParams.setSoTimeout(httpParameters,
        // Config.QUERY_TIMEOUT);
        //
        // // Set the BASIC authentication
        // String auth = android.util.Base64.encodeToString((Config.LOGIN
        // + ":" + Config.PASSWORD).getBytes("UTF-8"),
        // android.util.Base64.NO_WRAP);
        //
        // // Set the query
        // HttpDelete httpDelete = new HttpDelete(USER_LOCATION_URL);
        // httpDelete.addHeader("Authorization", "Basic " + auth);
        //
        // // Create the client and execute the query
        // DefaultHttpClient client = new DefaultHttpClient(httpParameters);
        // client.execute(httpDelete);
        //
        // } catch (Exception e) {
        // Log.e(Config.TAG_APP, TAG + "deleteUser : " + e);
        // }
    }

    /**
     * This function checks if the user exists or not.
     * <p/>
     * //	 * @return The state of the user : true = exist ; false = don't exist
     */
    public static void getUser() {

        // Log.d(Config.TAG_APP, TAG + "getUser --------------------------");
        //
        // InputStream is = null;
        //
        // try {
        // // Set the time out
        // HttpParams httpParameters = new BasicHttpParams();
        // HttpConnectionParams.setSoTimeout(httpParameters,
        // Config.QUERY_TIMEOUT);
        //
        // // Set the query
        // HttpGet httpGet = new HttpGet(Config.GET_USER_RESTADMIN_URL);
        //
        // // Create the client, execute the query and get the response
        // DefaultHttpClient client = new DefaultHttpClient(httpParameters);
        // HttpResponse response = client.execute(httpGet);
        // HttpEntity httpEntity = response.getEntity();
        // is = httpEntity.getContent();
        //
        // } catch (UnsupportedEncodingException e) {
        // Log.e(Config.TAG_APP, TAG + "getUser : " + e);
        // e.printStackTrace();
        // } catch (ClientProtocolException e) {
        // Log.e(Config.TAG_APP, TAG + "getUser : " + e);
        // e.printStackTrace();
        // } catch (IOException e) {
        // Log.e(Config.TAG_APP, TAG + "getUser : " + e);
        // e.printStackTrace();
        // }
        //
        // JSONObject c = Utils.getJSONObject(is);
        // String desc = null;
        // try {
        // if (c.has(DESCRIPTION)) {
        // desc = c.getString(DESCRIPTION);
        // if (desc.equals(USER_NOT_FOUND))
        // return false;
        // } else
        // return true;
        // } catch (JSONException e) {
        // Log.e(Config.TAG_APP, TAG + "getUser : " + e);
        // }
        // return true;
    }

    /**
     * Once the user is created, we have to get his {@code Config.USER_ID} and
     * {@code Config.USER_DATASOURCE} in order to make web queries. We get those
     * values by splitting the USER_LOCATION_URL returned in the header of the
     * {@code createUser()}.
     */
    public static void setUserIdAndDatasource() {

        // Log.d(Config.TAG_APP, TAG +
        // "getUserIdAndDatasource ----------------");
        //
        // String tmp = USER_LOCATION_URL;
        //
        // String data[] = tmp.split("/");
        //
        // for(int i=0; i<data.length; i++){
        // if(data[i].equals(USER_ID))
        // Config.USER_ID = Integer.parseInt(data[i+1]);
        // if(data[i].equals(USER_DATASOURCE))
        // Config.DATASOURCE = data[i+1];
        // }
        //
        // Log.d(Config.TAG_APP, TAG + USER_ID + " : " + Config.USER_ID);
        // Log.d(Config.TAG_APP, TAG + USER_DATASOURCE + " : " +
        // Config.DATASOURCE);
    }
}
