package com.example.fragmentweixin.anim;

import android.os.Parcel;
import android.os.Parcelable;

import com.utils_moudle.R;


/**
 * Created by YoKeyword on 16/2/5.
 */
public class DefaultHorizontalAnimator extends FragmentAnimator implements Parcelable{

    public DefaultHorizontalAnimator() {
    	enter = R.anim.h_fragment_enter;//目标入栈动画
        exit = R.anim.h_fragment_exit;//目标退出动画
        popEnter = R.anim.h_fragment_pop_enter;//源恢复入栈动画
        popExit = R.anim.h_fragment_pop_exit;//源进入出栈动画
    }

    protected DefaultHorizontalAnimator(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DefaultHorizontalAnimator> CREATOR = new Creator<DefaultHorizontalAnimator>() {
        @Override
        public DefaultHorizontalAnimator createFromParcel(Parcel in) {
            return new DefaultHorizontalAnimator(in);
        }

        @Override
        public DefaultHorizontalAnimator[] newArray(int size) {
            return new DefaultHorizontalAnimator[size];
        }
    };
}
