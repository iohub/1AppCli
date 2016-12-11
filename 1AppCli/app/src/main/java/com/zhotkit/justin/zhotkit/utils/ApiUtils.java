package com.zhotkit.justin.zhotkit.utils;

public class ApiUtils {
    public static final String HTML_HEAD;
    public static final String BODY_BEGIN;
    public static final String BODY_END;
    public static final String HTML_END = "</body></html>";

    static {
        StringBuffer sb = new StringBuffer();
        sb.append("<div class=\"main-wrap content-wrap\">");
        sb.append("<div class=\"headline\">");
        sb.append("<div class=\"img-place-holder\">");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("<div class=\"content-inner\">");
        sb.append("<div class=\"question\">");
        sb.append("<div class=\"answer\">");
        sb.append("<div class=\"content\">");

        BODY_BEGIN = sb.toString();
    }

    static {
        StringBuffer sb = new StringBuffer();
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");

        BODY_END = sb.toString();
    }

    static {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><head><meta name=\"viewport\" content=\"width=device-width, user-scalable=no\">");
        sb.append("<link href=\"news_qa.auto.css\" rel=\"stylesheet\">");
        sb.append("<link href=\"news_header.css\" rel=\"stylesheet\">");
        sb.append("<script src=\"night.js\"></script>");
        sb.append("<script src=\"img_replace.js\"></script>");
        sb.append("</head><body>");
        //sb.append("<body onload=\"set_night_mode(true);\">");
        HTML_HEAD = sb.toString();
    }

    // http://www.guokr.com/apis/minisite/article.json?retrieve_type=by_subject&limit=10&offset=11381&_=1444145242073
    public static final String URL_GUOKR_PREFIX = "http://www.guokr.com/apis/minisite/article.json?retrieve_type=by_subject";
    // http://apis.guokr.com/minisite/article/21.json
    public static final String URL_GUOKR_ARTICLE = "http://apis.guokr.com/minisite/article/";
    public static final int    GUOKR_PAGE_LIMIT = 15;
    public static final String JSON_CACHE_ZHIHU = "/zhihu_latest.json";
    public static final String URL_ZHIHU_PREFIX = "http://news-at.zhihu.com/api/4/news/";
    public static final String URL_ZHIHU_LATEST = "http://news-at.zhihu.com/api/4/news/latest";
}
