package com.park.monitoring.config.error;

import org.springframework.http.ResponseEntity;

public class ErrorResponse {
    private int status;
    private String name;
    private String message;
    private String code;

    public ErrorResponse(){}

    public ErrorResponse(Builder builder){
        this.status = builder.status;
        this.name = builder.name;
        this.message = builder.message;
        this.code = builder.code;
    }

    public static class Builder{
        private int status;
        private String name;
        private String message;
        private String code;

        public Builder status(int status){
            this.status = status;
            return this;
        }
        public Builder name(String name){
            this.name = name;
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
        public ErrorResponse build(){
            return new ErrorResponse(this);
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "status=" + status +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode e){
        return ResponseEntity.status(e.getStatus())
                .body(new ErrorResponse.Builder()
                        .status(e.getStatus().value())
                        .name(e.name())
                        .message(e.getMessage())
                        .code(e.getCode())
                        .build());
    }
}
