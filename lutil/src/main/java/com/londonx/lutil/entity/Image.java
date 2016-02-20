package com.londonx.lutil.entity;

import java.io.Serializable;

/**
 * Created by london on 15/12/7.
 * 图片
 * url : /user_avatar/xe5GjkPLDyNgAmH49Bme.png
 * normal : {"url":"/user_avatar/xe5GjkPLDyNgAmH49Bme.png"}
 * small : {"url":"/user_avatar/xe5GjkPLDyNgAmH49Bme.png"}
 * large : {"url":"/user_avatar/xe5GjkPLDyNgAmH49Bme.png"}
 * big : {"url":"/user_avatar/xe5GjkPLDyNgAmH49Bme.png"}
 */

public class Image extends LEntity {
    public String url;
    public UrlEntity normal;
    public UrlEntity small;
    public UrlEntity large;
    public UrlEntity big;

    public class UrlEntity implements Serializable {
        public String url;
    }
}
