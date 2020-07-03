package com.example.hbddavin.modals;

public class ModelPost {
    private String Id , image, message, name, time;

    public ModelPost() {
    }

    public ModelPost(String id, String image_url, String message, String name, String time) {
        this.Id = Id;
        this.image = image;
        this.message = message;
        this.name = name;
        this.time = time;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = Id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image_url) {
        this.image = image_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ModelPost{" +
                "Id='" + Id + '\'' +
                ", image='" + image + '\'' +
                ", message='" + message + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
