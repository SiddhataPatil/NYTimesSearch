package com.codepath.siddhatapatil.nytimessearch.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by siddhatapatil on 9/24/17.
 */

public class Filter implements Parcelable {
    private String beginDate;
    private String sort;
    private ArrayList<String> fl = new ArrayList<String>();

    public Filter() {

    }

    protected Filter(Parcel in) {
        beginDate = in.readString();
        sort = in.readString();
        fl = in.createStringArrayList();
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public ArrayList<String> getFl() {
        return fl;
    }

    public void setFl(ArrayList<String> fl) {
        this.fl = fl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(beginDate);
        dest.writeString(sort);
        dest.writeStringList(fl);
    }
}