package com.park.monitoring.model;

import java.util.Objects;
public class User {
    private Long id;
    private String name;
    private String email;
    private String passwd;


    public static Builder builder(){
        return new Builder();
    }
    public User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.passwd = builder.passwd;
    }

    public User() {}

    public static class Builder{
        private String name;
        private String email;
        private String passwd;


        public Builder name(String name){
            this.name=name;
            return this;
        }
        public Builder email(String email){
            this.email=email;
            return this;
        }

        public Builder passwd(String passwd){
            this.passwd=passwd;
            return this;
        }
        public User build(){
            return new User(this);
        }
    }

    public Builder toBuilder(){
        return new Builder().name(this.name).email(this.email).passwd(this.passwd);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public String getPasswd() {
        return passwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(passwd, user.passwd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, passwd);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
