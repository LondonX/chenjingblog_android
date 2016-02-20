package com.londonx.lutil.entity;

import java.io.File;

/**
 * Created by london on 15/6/2.
 * LResponse
 */
public class LResponse extends LEntity {
    public int requestCode;
    public int responseCode;
    public String url;
    public String body;
    public File downloadFile;
}
