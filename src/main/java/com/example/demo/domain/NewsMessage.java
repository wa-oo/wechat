package com.example.demo.domain;

import java.util.List;

public class NewsMessage extends BaseMessage {
    private int ArticleCount;
    private List<News> Article;

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<News> getArticle() {
        return Article;
    }

    public void setArticle(List<News> article) {
        Article = article;
    }
}
