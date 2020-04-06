package common.actions;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.message.GZipEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

    private static final Logger LOG = LoggerFactory.getLogger(Util.class);

    public static final Client JERSEYCLIENT;

    static {
        JERSEYCLIENT = InsecureClient.createClient();
    }

    /**
     * GET Request
     *
     * @param url
     * @param jwt
     * @return
     * @throws IOException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static Response sendGetRequest(String url, String jwt) {
        return sendGetRequest(url, jwt, MediaType.APPLICATION_JSON);
    }

    public static Response sendGetRequest(String url, String jwt, String... mediaTypeStrings) {
        WebTarget target = JERSEYCLIENT.target(url);
        LOG.info("Sending GET REQUEST URL = {}", url);
        LOG.debug("Sending GET REQUEST JWT = {}", jwt);
        Response getResponse = target.request(mediaTypeStrings)
                .header(HttpHeaders.AUTHORIZATION, "Token " + jwt)
                .get(Response.class);
        LOG.info("GET request response = {}", getResponse);
        return getResponse;
    }

    public static Response sendGetRequestAuth(String url, Map<String, String> queryParam) {
        Cookie sessionId =  getTrialCookies();
        WebTarget target = JERSEYCLIENT.target(url);
        if ( null !=queryParam) {
            url = url + "?";
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOG.info("Sending GET REQUEST URL = {}", url);
        Response getResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .cookie(sessionId)
                .get(Response.class);
        LOG.info("GET request response = {}", getResponse);
        return getResponse;
    }

    public static Response sendGetRequestAuth(String url, Map<String, String> queryParam,
            Cookie sessionId) {
        WebTarget target = JERSEYCLIENT.target(url);
        if ( null !=queryParam) {
            url = url + "?";
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOG.info("Sending GET REQUEST URL = {}", url);
        Response getResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .cookie(sessionId)
                .get(Response.class);
        LOG.info("GET request response = {}", getResponse);
        return getResponse;
    }

    public static Response sendTrialGetRequestAuth(String url, Map<String, String> queryParam,
            String username , String password) {
        Cookie sessionId =  getTrialCookies(username, password);

        WebTarget target = JERSEYCLIENT.target(url);
        if ( null !=queryParam) {
            url = url + "?";
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOG.info("Sending GET REQUEST URL = {}", url);
        Response getResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .cookie(sessionId)
                .get(Response.class);
        LOG.info("GET request response = {}", getResponse);
        return getResponse;
    }

    public static Response sendGetRequestAuthWithContentType(String url,
            Map<String, String> queryParam, String contentType) {
        Cookie sessionId =  getTrialCookies();
        WebTarget target = JERSEYCLIENT.target(url);
        if ( null !=queryParam) {
            url = url + "?";
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOG.info("Sending GET REQUEST URL = {}", url);
        Response getResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, contentType)
                .header("X-Requested-By", "ThoughtSpot")
                .cookie(sessionId)
                .get(Response.class);
        LOG.info("GET request response = {}", getResponse);
        return getResponse;
    }

    public static Response sendGetRequestWithJwtAuth(String url, String jwt,
            Map<String, String> queryParam) throws IOException, KeyManagementException,
            NoSuchAlgorithmException {
        url = url + "?";
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        WebTarget target = JERSEYCLIENT.target(url);
        target.register(GZipEncoder.class);
        if ( null != queryParam) {
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOG.info("Sending post request with Query Param {}", target.toString());
        LOG.info("Sending POST REQUEST URL = {}", url);
        Response response = target.request()
                .header(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br")
                .header(HttpHeaders.CONTENT_TYPE, "applcation/json")
                .header(HttpHeaders.ACCEPT, "application/snowflake")
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .header(HttpHeaders.HOST, Constants.SNOWFLAKE_HOST_HEADER)
                .header("Origin", Constants.SNOWFLAKE_ORIGIN_HEADER)
                .header("Referer", Constants.SNOWFLAKE_REFERER_HEADER)
                .get(Response.class);
        LOG.info("GET request response = {}", response);
        System.out.println(response.getHeaders());
        return response;
    }

    public static Response sendPostRequestNoAuth(String url,
            MultivaluedMap<String, String> input, Map<String, String> queryParam)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {
        url = url + "?";
        WebTarget target = JERSEYCLIENT.target(url);
        if ( null != queryParam) {
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOG.info("Sending post request with Query Param {}", target.toString());
        LOG.info("Sending POST REQUEST URL = {}", url);
        Response postResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header("X-Requested-By", "ThoughtSpot")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .post(Entity.form(input));
        LOG.info("GET request response = {}", postResponse);

        return postResponse;
    }

    public static Response sendPostRequestWithSessionId(String url,
            MultivaluedMap<String, String> input, Map<String, String> queryParam,
            Cookie sessionId, Cookie clientId, Cookie __cfduid)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {
        url = url + "?";
        WebTarget target = JERSEYCLIENT.target(url);
        if ( null != queryParam) {
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOG.info("Sending post request with Query Param {}", target.toString());
        LOG.info("Sending POST REQUEST URL = {}", url);
        Response postResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header("X-Requested-By", "ThoughtSpot")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .cookie("clientId", String.valueOf(clientId))
                .cookie("JSESSIONID", String.valueOf(sessionId))
                .cookie("__cfduid", String.valueOf(__cfduid))
                .post(Entity.form(input));
        LOG.info("GET request response = {}", postResponse);

        return postResponse;
    }

    public static Response sendSFPostRequest(String url,
            String input, Map<String, String> queryParam, String jwt)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {
        url = url + "?";
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        WebTarget target = JERSEYCLIENT.target(url);
        target.register(GZipEncoder.class);
        if ( null != queryParam) {
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOG.info("Sending post request with Query Param {}", target.toString());
        LOG.info("Sending POST REQUEST URL = {}", url);
        Response postResponse = target.request()
                .header(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br")
                .header(HttpHeaders.CONTENT_TYPE, "applcation/json")
                .header(HttpHeaders.ACCEPT, "application/snowflake")
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .header(HttpHeaders.HOST, Constants.SNOWFLAKE_HOST_HEADER)
                .header("Origin", Constants.SNOWFLAKE_ORIGIN_HEADER)
                .header("Referer", Constants.SNOWFLAKE_REFERER_HEADER)
                .post(Entity.json(input));
        LOG.info("GET request response = {}", postResponse);
        System.out.println(postResponse.getHeaders());
        return postResponse;
    }

    public static Response sendPostRequestWithAuthCookie(String url,
            MultivaluedMap<String, String> input, Map<String, String> queryParam)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {
        url = url + "?";
        Cookie sessionId =  getTrialCookies();
        WebTarget target = JERSEYCLIENT.target(url);
        if ( null != queryParam) {
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOG.info("Sending post request with Query Param {}", target.toString());
        LOG.info("Sending POST REQUEST URL = {}", url);
        Response postResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header("X-Requested-By", "ThoughtSpot")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .cookie(sessionId)
                .post(Entity.form(input));
        LOG.info("GET request response = {}", postResponse);

        return postResponse;
    }

    public static Response sendPostRequestWithAuthCookie(String url,
            MultivaluedMap<String, String> input, String username , String password, Map<String,
            String> queryParam)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {
        url = url + "?";
        Cookie sessionId =  getTrialCookies(username, password);
        WebTarget target = JERSEYCLIENT.target(url);
        if ( null != queryParam) {
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOG.info("Sending post request with Query Param {}", target.toString());
        LOG.info("Sending POST REQUEST URL = {}", url);
        Response postResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header("X-Requested-By", "ThoughtSpot")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .cookie(sessionId)
                .post(Entity.form(input));
        LOG.info("GET request response = {}", postResponse);

        return postResponse;
    }

    public static Cookie getTrialCookies() {
        WebTarget target = JERSEYCLIENT.target(Constants.SESSION_LOGIN);
        LOG.info("Sending post request with Query Param {}", target.toString());
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("username", Constants.CUSTOMER_USERNAME);
        formData.add("password", Constants.CUSTOMER_PASSWORD);

        Response postResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header("X-Requested-By", "ThoughtSpot")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .post(Entity.form(formData));
        LOG.info("GET request response = {}", postResponse);

        return postResponse.getCookies().get("JSESSIONID");
    }

    public static Cookie getTrialCookies(String username , String password) {
        WebTarget target = JERSEYCLIENT.target(Constants.TRIAL_SESSION_LOGIN);
        LOG.info("Sending post request with Query Param {}", target.toString());
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("username", username);
        formData.add("password", password);

        Response postResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header("X-Requested-By", "ThoughtSpot")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .post(Entity.form(formData));
        LOG.info("GET request response = {}", postResponse);

        return postResponse.getCookies().get("JSESSIONID");
    }

    /**
     * PUT Request using JSON Web Token(jwt)
     *
     * @param url
     * @param jwt
     * @param input
     * @return response
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static Response sendPutRequest(String url, String jwt, String input) throws IOException, KeyManagementException,
            NoSuchAlgorithmException {
        WebTarget target = JERSEYCLIENT.target(url);
        LOG.info("Sending PUT REQUEST URL = {}", url);
        LOG.info("Sending PUT REQUEST INPUT = {}", input);
        Response putResponse = target.request(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).put(Entity.json(input));
        LOG.info("POST request response = {}", putResponse);
        return putResponse;
    }

    /**
     * DELETE Request
     *
     * @param url
     * @param jwt
     * @return response
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static Response sendDeleteRequest(String url, String jwt) throws IOException, KeyManagementException,
            NoSuchAlgorithmException {
        WebTarget target = JERSEYCLIENT.target(url);
        LOG.info("Sending DELETE REQUEST URL = {}", url);
        LOG.debug("Sending DELETE REQUEST JWT = {}", jwt);
        Response deleteResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).delete();
        LOG.info("DELETE request response = {}", deleteResponse);
        return deleteResponse;
    }

    public static Response sendDeleteRequestAuth(String url, Map<String, String> queryParam) {
        WebTarget target = JERSEYCLIENT.target(url);
        Cookie sessionId =  getTrialCookies();
        if ( null !=queryParam) {
            url = url + "?";
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                target = target.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOG.info("Sending DELETE REQUEST URL = {}", url);
        Response deleteResponse = target.request(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("X-Requested-By", "ThoughtSpot")
                .cookie(sessionId)
                .delete();
        LOG.info("GET request response = {}", deleteResponse);
        return deleteResponse;
    }

    /**
     * Read JSON response
     */
    public static String readResponse(Response response) throws IOException {
        return response.readEntity(String.class);
    }

    /**
     * Verify expected response
     */
    public static void verifyExpectedResponse(Response response, Status expectedStatus) throws Exception {
        if (response.getStatus() != expectedStatus.getStatusCode()) {
            throw new Exception("Expected response code: " + expectedStatus.getStatusCode() + " Actual response code: "
                    + response.getStatus() + " Reason: " + response.getStatusInfo().getReasonPhrase() + "\n Response: "
                    + readResponse(response));
        } else {
            LOG.info("RESPONSE OK EXPECTED {} ACTUAL {}", expectedStatus.getStatusCode(), response.getStatus());
        }
    }

    public static String buildRequestData(Map<String, Object> postRequestInput) throws JsonProcessingException {
        ObjectMapper postRequestInputObjMapper = new ObjectMapper();
        String input = postRequestInputObjMapper.writeValueAsString(postRequestInput);
        return input;
    }

}