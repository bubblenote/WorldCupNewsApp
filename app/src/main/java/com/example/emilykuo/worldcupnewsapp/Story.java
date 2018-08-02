package com.example.emilykuo.worldcupnewsapp;

public class Story {

    private String articleTitle;
    private String articleAuthor;
    private String articleSection;
    private String articleDate;
    private String articleURL;

    public Story(String title, String author, String section, String date, String url) {
        articleTitle = title;
        articleAuthor = author;
        articleSection = section;
        articleDate = date;
        articleURL = url;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleAuthor(){
        return articleAuthor;
    }

    public String getArticleSection() {
        return articleSection;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public String getArticleURL() {
        return articleURL;
    }
}