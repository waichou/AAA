package com.framworks.base.main.fragments.childfragment.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouwei on 2018/7/29.
 */

public class FirstBean implements Parcelable {

    public String name;
    public String message;
    public long time;
    public int avatar;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.message);
        dest.writeLong(this.time);
        dest.writeInt(this.avatar);
    }

    public FirstBean() {
    }

    protected FirstBean(Parcel in) {
        this.name = in.readString();
        this.message = in.readString();
        this.time = in.readLong();
        this.avatar = in.readInt();
    }

    public static final Parcelable.Creator<FirstBean> CREATOR = new Parcelable.Creator<FirstBean>() {
        @Override
        public FirstBean createFromParcel(Parcel source) {
            return new FirstBean(source);
        }

        @Override
        public FirstBean[] newArray(int size) {
            return new FirstBean[size];
        }
    };
}
