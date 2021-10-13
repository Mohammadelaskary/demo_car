package com.example.democar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class WorkShop implements Parcelable {
    private String workshopId;
    private String workshopName;
    private String userId;
    private String description;
    private List<String> imageUrls;
    private String token;
    private List<String> availableCalendars;

    public WorkShop() {
    }


    public WorkShop(String workshopId, String workshopName, String userId, String description, List<String> imageUrls) {
        this.workshopId = workshopId;
        this.workshopName = workshopName;
        this.userId = userId;
        this.description = description;
        this.imageUrls = imageUrls;
    }


    protected WorkShop(Parcel in) {
        workshopId = in.readString();
        workshopName = in.readString();
        userId = in.readString();
        description = in.readString();
        imageUrls = in.createStringArrayList();
        token = in.readString();
        availableCalendars = in.createStringArrayList();
    }

    public static final Creator<WorkShop> CREATOR = new Creator<WorkShop>() {
        @Override
        public WorkShop createFromParcel(Parcel in) {
            return new WorkShop(in);
        }

        @Override
        public WorkShop[] newArray(int size) {
            return new WorkShop[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(String workshopId) {
        this.workshopId = workshopId;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAvailableCalendars() {
        return availableCalendars;
    }

    public void setAvailableCalendars(List<String> availableCalendars) {
        this.availableCalendars = availableCalendars;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(workshopId);
        dest.writeString(workshopName);
        dest.writeString(userId);
        dest.writeString(description);
        dest.writeStringList(imageUrls);
        dest.writeString(token);
        dest.writeStringList(availableCalendars);
    }
}
