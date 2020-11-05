package com.cup_of_excellence.heartrateapp.entities;

//model class for recyclerViwe
public class ModelRecord {
    //variables
    String id, name, email, dob, image, age, maxHeartRate, mintargetHeartRate, maxtargetHeartRate, addedTime, updateTime;

    //constructor
    public ModelRecord(String id, String name, String email, String dob, String image,
                       String age, String maxHeartRate, String mintargetHeartRate,
                       String maxtargetHeartRate, String addedTime, String updateTime) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.image = image;
        this.age = age;
        this.maxHeartRate = maxHeartRate;
        this.mintargetHeartRate = mintargetHeartRate;
        this.maxtargetHeartRate = maxtargetHeartRate;
        this.addedTime = addedTime;
        this.updateTime = updateTime;
    }

    //getter and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(String maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public String getMintargetHeartRate() {
        return mintargetHeartRate;
    }

    public void setMintargetHeartRate(String mintargetHeartRate) {
        this.mintargetHeartRate = mintargetHeartRate;
    }

    public String getMaxtargetHeartRate() {
        return maxtargetHeartRate;
    }

    public void setMaxtargetHeartRate(String maxtargetHeartRate) {
        this.maxtargetHeartRate = maxtargetHeartRate;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
