package com.example.twitter_search.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sagi on 1/12/2017.
 * Create JsonObjectRequest with ability to set headers
 * We're not inheriting JsonObjectRequest directly - because we need the ability to send a simple
 * String body.
 * so we're gonna extend JsonRequest - and copy the parsing implementation from JsonObjectRequest.
 */
public class JsonObjectHeadersRequest extends JsonRequest<JSONObject> {
    private Map<String, String> mHeaders;

    public JsonObjectHeadersRequest(int method, String url, String requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);

        init();
    }

    private void init() {
        mHeaders = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    public void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    /**
     * Copy same implementation as JsonObjectRequest
     * @param response
     * @return
     */
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
