package com.example.readsms;

public class RegistrSet {
    String name,hobby;

    public com.example.readsms.json getJson() {
        return json;
    }

    public void setJson(com.example.readsms.json json) {
        this.json = json;
    }

    json json ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public RegistrSet(){

    }

    public RegistrSet(String name, String hobby) {
        this.name = name;
        this.hobby = hobby;
    }
}
