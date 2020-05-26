package com.liuchenxi.foundation.http.base;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkLogContext implements Displayable {
    private String mProtocol;
    private String mMethod;
    private String mUrl;
    private HashMap<String, List<String>> mRequestHeader;
    private String mRequestBody;
    @Expose(serialize = false, deserialize = false)
    private String mExtraMessage;

    /// Response
    private int mStatusCode;
    private String mMessage;
    private long mRequestTime;
    private long mResponseBodySize;
    private HashMap<String, List<String>> mResponseHeader;
    private String mResponseBody;
    @Expose(serialize = false, deserialize = false)
    private String mExtraResponseMessage;

    public String getProtocol() {
        return mProtocol;
    }

    public void setProtocol(String mProtocol) {
        this.mProtocol = mProtocol;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String mMethod) {
        this.mMethod = mMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public HashMap<String, List<String>> getRequestHeader() {
        return mRequestHeader;
    }

    public void setRequestHeader(Map<String, List<String>> requestHeader) {
        this.mRequestHeader = new HashMap<>(requestHeader);
    }

    public String getRequestBody() {
        return mRequestBody;
    }

    public void setRequestBody(String mRequestBody) {
        this.mRequestBody = mRequestBody;
    }

    public String getExtraMessage() {
        return mExtraMessage;
    }

    public void setExtraMessage(String mExtraMessage) {
        this.mExtraMessage = mExtraMessage;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(int mStatusCode) {
        this.mStatusCode = mStatusCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public long getRequestTime() {
        return mRequestTime;
    }

    public void setRequestTime(long mRequestTime) {
        this.mRequestTime = mRequestTime;
    }

    public long getResponseBodySize() {
        return mResponseBodySize;
    }

    public void setResponseBodySize(long mResponseBodySize) {
        this.mResponseBodySize = mResponseBodySize;
    }

    public HashMap<String, List<String>> getResponseHeader() {
        return mResponseHeader;
    }

    public void setResponseHeader(Map<String, List<String>> responseHeader) {
        this.mResponseHeader = new HashMap<>(responseHeader);
    }

    public String getResponseBody() {
        return mResponseBody;
    }

    public void setResponseBody(String mResponseBody) {
        this.mResponseBody = mResponseBody;
    }

    public String getExtraResponseMessage() {
        return mExtraResponseMessage;
    }

    public void setExtraResponseMessage(String mExtraResponseMessage) {
        this.mExtraResponseMessage = mExtraResponseMessage;
    }

    /**
     * Format the network request info to console log message
     * @return
     */
    public String toConsoleLogString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--> ").append(mMethod).append(" ").append(mUrl).append(" ").append(mProtocol).append("\n");
        if (mRequestHeader != null) {
            appendHeader(mRequestHeader, sb);
        }

        if (!TextUtils.isEmpty(mRequestBody)) {
            sb.append(mRequestBody).append("\n");
        }

        sb.append("--> END ").append(mMethod);
        if (!TextUtils.isEmpty(mExtraMessage)) {
            sb.append(mExtraMessage);
        }
        sb.append("\n");

        if (mStatusCode == -1) {
            sb.append("<-- HTTP FAILED");
            return sb.toString();
        }

        sb.append("<-- " ).append(mStatusCode).append(" ").append(mMessage).append(" ").append(mUrl);
        String bodySize = mResponseBodySize != -1L ? mResponseBodySize + "-byte" : "unknown-length";
        sb.append(" (").append(mRequestTime).append("ms").append(mResponseHeader == null ? ", " + bodySize + " body)" : ")").append("\n");
        if (mResponseHeader != null) {
            appendHeader(mResponseHeader, sb);
        }

        if (!TextUtils.isEmpty(mResponseBody)) {
            sb.append(mResponseBody);
        }

        sb.append("<-- END HTTP ");
        sb.append(mExtraResponseMessage);

        return sb.toString();
    }

    private StringBuilder appendHeader(Map<String, List<String>> headers, StringBuilder sb) {
        if (headers == null || headers.isEmpty()) {
            return sb;
        }
        for (String key: headers.keySet()) {
            List<String> values = headers.get(key);
            for (String v : values) {
                sb.append(key).append(": ").append(v).append("\n");
            }
        }
        return sb;
    }

    @Override
    public ArrayList<String> buildDisplayableElements() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add("请求协议");
        ret.add(getProtocol());
        ret.add("请求方式");
        ret.add(mMethod);
        ret.add("请求Url");
        ret.add(mUrl);
        ret.add("请求Header");
        ret.add(header2String(mRequestHeader));
        if (!TextUtils.isEmpty(mRequestBody)) {
            ret.add("请求参数");
            ret.add(mRequestBody);
        }
        ret.add("状态码");
        ret.add(mStatusCode + "-" + mMessage);
        ret.add("返回Header");
        ret.add(header2String(mResponseHeader));
        if (!TextUtils.isEmpty(mResponseBody)) {
            ret.add("返回数据");
            ret.add(mResponseBody);
        }
        return ret;
    }

    private String header2String(Map<String, List<String>> header) {
        if (header == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String key : header.keySet()) {
            List<String> values = header.get(key);
            for (String v : values) {
                sb.append(key);
                sb.append(":");
                sb.append(v);
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
