package com.londonx.lutil.util;

import com.londonx.lutil.entity.LResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by london on 15/6/2.
 * LRequestTool
 * Update in 2015-08-04 21:50:33 DELETE not working
 * Update in 2015-08-06 11:17:52 onPostExecute not calling in pre-JELLYBEAN
 * Update in 2015-08-13 12:43:01 twice onResponse called in pre-JELLYBEAN
 * Update in 2015-08-19 19:58:15 add URLEncoder in GET
 * Update in 2015-08-21 12:40:53 'com.loopj.android:android-async-http:1.4.8' for large file upload
 * Update in 2015-08-21 19:13:19 avoid downloading the exist file
 * Update in 2015-11-03 19:12:51 add onUploadListener
 */
public class LRequestTool {
    private OnResponseListener onResponseListener;
    private OnDownloadListener onDownloadListener;
    private OnUploadListener onUploadListener;
    private static AsyncHttpClient client;

    public LRequestTool(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
        if (client == null) {
            client = new AsyncHttpClient();
            client.setURLEncodingEnabled(true);
            client.setLoggingEnabled(false);
        }
    }

    private RequestParams hashMapToRequestParams(HashMap<String, ?> hashMap) {
        RequestParams params = new RequestParams();
        for (String key : hashMap.keySet()) {
            Object value = hashMap.get(key);
            if (value instanceof File) {
                try {
                    params.put(key, ((File) value));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                params.put(key, value.toString());
            }
        }
        return params;
    }

    public void doGet(final String strUrl, final HashMap<String, ?> urlParams, final int requestCode) {
        client.get(strUrl, hashMapToRequestParams(urlParams), new HttpHandler(requestCode));
    }

    public void doPost(final String strUrl, final HashMap<String, ?> urlParams, final int requestCode) {
        client.post(strUrl, hashMapToRequestParams(urlParams), new HttpHandler(requestCode));
    }

    public void doPut(final String strUrl, final HashMap<String, ?> urlParams, final int requestCode) {
        client.put(strUrl, hashMapToRequestParams(urlParams), new HttpHandler(requestCode));
    }

    public void doDelete(final String strUrl, final HashMap<String, ?> urlParams, final int requestCode) {
        client.delete(strUrl, hashMapToRequestParams(urlParams), new HttpHandler(requestCode));
    }

    public void download(final String strUrl, final int requestCode) {
        final LResponse response = new LResponse();
        response.requestCode = requestCode;
        response.url = strUrl;

        File downloadFile = null;
        try {
            downloadFile = FileUtil.getDownloadFile(strUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (downloadFile != null &&
                downloadFile.exists() &&
                downloadFile.length() > 0) {//avoid re-download the same file
            response.downloadFile = downloadFile;
            if (onDownloadListener != null) {
                onDownloadListener.onStartDownload(response);
                onDownloadListener.onDownloaded(response);
            }
            return;
        }

        client.get(strUrl, new HttpHandler(requestCode, downloadFile));
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }

    public void setOnUploadListener(OnUploadListener onUploadListener) {
        this.onUploadListener = onUploadListener;
    }

    public interface OnResponseListener {
        void onResponse(LResponse response);
    }

    public interface OnDownloadListener {
        void onStartDownload(LResponse response);

        void onDownloading(float progress);

        void onDownloaded(LResponse response);
    }

    public interface OnUploadListener {
        void onStartUpload(LResponse response);

        void onUploading(float progress);

        void onUploaded(LResponse response);
    }

    private final class HttpHandler extends AsyncHttpResponseHandler {
        int requestCode;
        File downloadFile;

        public HttpHandler(int requestCode) {
            this.requestCode = requestCode;
        }

        public HttpHandler(int requestCode, File downloadFile) {
            this.requestCode = requestCode;
            this.downloadFile = downloadFile;
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);
            if (downloadFile != null && onDownloadListener != null) {
                onDownloadListener.onDownloading((float) bytesWritten / (float) totalSize);
            }
            if (onUploadListener != null) {
                onUploadListener.onUploading((float) bytesWritten / (float) totalSize);
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if (downloadFile != null) {
                try {
                    InputStream ips = new ByteArrayInputStream(responseBody);
                    FileOutputStream fos = new FileOutputStream(downloadFile);
                    int read;
                    byte[] buffer = new byte[1024];
                    while ((read = ips.read(buffer)) != -1) {
                        fos.write(buffer, 0, read);
                        fos.flush();
                    }
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LResponse lResponse = new LResponse();
                lResponse.requestCode = requestCode;
                lResponse.url = getRequestURI().toString();
                lResponse.responseCode = statusCode;
                lResponse.downloadFile = downloadFile;
                if (onDownloadListener != null) {
                    onDownloadListener.onDownloaded(lResponse);
                }
                return;
            }
            LResponse lResponse = new LResponse();
            lResponse.requestCode = requestCode;
            lResponse.url = getRequestURI().toString();
            lResponse.responseCode = statusCode;
            lResponse.body = new String(responseBody);
            onResponseListener.onResponse(lResponse);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            if (downloadFile != null) {
                return;
            }
            LResponse lResponse = new LResponse();
            lResponse.requestCode = requestCode;
            lResponse.url = getRequestURI().toString();
            lResponse.responseCode = statusCode;
            lResponse.body = error.getMessage();
            onResponseListener.onResponse(lResponse);
        }
    }
}
