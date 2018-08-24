package com.test.test;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by zhouwei on 2018/5/23.
 */

public class Father implements Parcelable {

    private String name;
    private int age;
    private Date date;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
    }

    public Father() {
    }

    protected Father(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Parcelable.Creator<Father> CREATOR = new Parcelable.Creator<Father>() {
        @Override
        public Father createFromParcel(Parcel source) {
            return new Father(source);
        }

        @Override
        public Father[] newArray(int size) {
            return new Father[size];
        }
    };
}
