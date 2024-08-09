package com.park.monitoring.dto;

public class ErrorResponseDto {
    private int status;
    private String message;
    private String code;

    public ErrorResponseDto(Builder builder) {
        this.status = builder.status;
        this.message = builder.message;
        this.code = builder.code;
    }

    public static class Builder{
        private int status;
        private String message;
        private String code;
        public Builder status(int status){
            this.status = status;
            return this;
        }
        public Builder message(String message){
            this.message = message;
            return this;
        }
        public Builder code(String code){
            this.code = code;
            return this;
        }
        public ErrorResponseDto build(){
            return new ErrorResponseDto(this);
        }
    }
}
