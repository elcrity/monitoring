package com.park.monitoring.dto;

public class UserInfoDto {
    private String name;
    private String email;

    public static Builder builder(){
        return new Builder();
    }

    public UserInfoDto(Builder builder){
        this.name = builder.name;
        this.email = builder.email;
    }

    public UserInfoDto(){}

    public static class Builder{
        private String name;
        private String email;

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder email(String email){
            this.email = email;
            return this;
        }

        public UserInfoDto build(){
            return new UserInfoDto(this);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }
}

