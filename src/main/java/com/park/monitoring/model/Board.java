package com.park.monitoring.model;

public class Board {
    private Long id;
    private String title;
    private String content;

    public Board() {
    }
    public Board(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.content = builder.content;
    }

    public static class Builder{
        private Long id;
        private String title;
        private String content;

        public Builder id(Long id){
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }
        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Board build() {return new Board(this);
        }

    }
    public Builder toBuilder(){
        return new Builder().id(this.id).title(this.title).content(this.content);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Boards{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
