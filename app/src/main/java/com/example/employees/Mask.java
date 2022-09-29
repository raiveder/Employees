package com.example.employees;

import android.os.Parcel;
import android.os.Parcelable;

public class Mask implements Parcelable {

    private int Id;
    private String Surname;
    private String Name;
    private int Age;
    private String Image;

    protected Mask(Parcel in) {
        Id = in.readInt();
        Surname = in.readString();
        Name = in.readString();
        Age = in.readInt();
        Image = in.readString();
    }

    public static final Creator<Mask> CREATOR = new Creator<Mask>() {
        @Override
        public Mask createFromParcel(Parcel in) {
            return new Mask(in);
        }

        @Override
        public Mask[] newArray(int size) {
            return new Mask[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAge(int age) {
        Age = age;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getSurname() {
        return Surname;
    }

    public String getName() {
        return Name;
    }

    public int getAge() {
        return Age;
    }

    public String getImage() {
        return Image;
    }

    public Mask(int Id, String surname, String name, int age, String image) {
        this.Id = Id;
        Surname = surname;
        Name = name;
        Age = age;
        Image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Surname);
        dest.writeString(Name);
        dest.writeInt(Age);
        dest.writeString(Image);
    }
}