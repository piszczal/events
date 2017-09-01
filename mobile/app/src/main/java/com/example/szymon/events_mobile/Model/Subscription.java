package com.example.szymon.events_mobile.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by Szymon on 30.05.2017.
 */

@JsonAutoDetect
public class Subscription implements Parcelable {

    public String subscription_type;
    public Event event;
    public Integer user;

    public Subscription() {

    }

    public String getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(String subscription_type) {
        this.subscription_type = subscription_type;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subscription_type);
        dest.writeParcelable(this.event, flags);
        dest.writeValue(this.user);
    }

    protected Subscription(Parcel in) {
        this.subscription_type = in.readString();
        this.event = in.readParcelable(Event.class.getClassLoader());
        this.user = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Subscription> CREATOR = new Parcelable.Creator<Subscription>() {
        @Override
        public Subscription createFromParcel(Parcel source) {
            return new Subscription(source);
        }

        @Override
        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };
}
