package com.park.monitoring.dto;

public class BoardDto {
    private Long id;
    private String title;
    private String content;

    public static Builder builder(){
        return new Builder();
    }

    public BoardDto(Builder builder){
        this.id = builder.id;
        this.title = builder.title;
        this.content = builder.content;
    }

    public BoardDto(){}

    public static class Builder{
        private Long id;
        private String title;
        private String content;

        public Builder id(Long id){
            this.id = id;
            return this;
        }

        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder content(String content){
            this.content = content;
            return this;
        }

        public BoardDto build(){
            return new BoardDto(this);
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
            return "Builder{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}

