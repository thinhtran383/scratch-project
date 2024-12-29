package com.example.library.model;

public class Author {
    private String authorId;
    private String authorName;

    public Author() {}

    public Author(String authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +
                '}';
    }

    public static AuthorBuilder builder() {
        return new AuthorBuilder();
    }

    public static class AuthorBuilder {
        private String authorId;
        private String authorName;

        public AuthorBuilder() {}

        public AuthorBuilder authorId(String authorId) {
            this.authorId = authorId;
            return this;
        }

        public AuthorBuilder authorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public Author build() {
            return new Author(authorId, authorName);
        }
    }
}
