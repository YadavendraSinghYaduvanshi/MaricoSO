package com.cpm.Marico_so.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoterTargetMM {
    @SerializedName("Store_Id")
    @Expose
    private Integer storeId;
    @SerializedName("Monthly_Volume_Target")
    @Expose
    private Double monthlyVolumeTarget;
    @SerializedName("MTD_Volume_Target")
    @Expose
    private Double mTDVolumeTarget;
    @SerializedName("MTD_Volume_Achievement")
    @Expose
    private Integer mTDVolumeAchievement;
    @SerializedName("MTD_Vol_Per")
    @Expose
    private Double mTDVolPer;
    @SerializedName("Monthly_Value_Target")
    @Expose
    private Double monthlyValueTarget;
    @SerializedName("MTD_Value_Target")
    @Expose
    private Double mTDValueTarget;
    @SerializedName("MTD_Value_Achievement")
    @Expose
    private Integer mTDValueAchievement;
    @SerializedName("MTD_Val_Per")
    @Expose
    private Double mTDValPer;

    private String store_name="";


    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Double getMonthlyVolumeTarget() {
        return monthlyVolumeTarget;
    }

    public void setMonthlyVolumeTarget(Double monthlyVolumeTarget) {
        this.monthlyVolumeTarget = monthlyVolumeTarget;
    }

    public Double getMTDVolumeTarget() {
        return mTDVolumeTarget;
    }

    public void setMTDVolumeTarget(Double mTDVolumeTarget) {
        this.mTDVolumeTarget = mTDVolumeTarget;
    }

    public Integer getMTDVolumeAchievement() {
        return mTDVolumeAchievement;
    }

    public void setMTDVolumeAchievement(Integer mTDVolumeAchievement) {
        this.mTDVolumeAchievement = mTDVolumeAchievement;
    }

    public Double getMTDVolPer() {
        return mTDVolPer;
    }

    public void setMTDVolPer(Double mTDVolPer) {
        this.mTDVolPer = mTDVolPer;
    }

    public Double getMonthlyValueTarget() {
        return monthlyValueTarget;
    }

    public void setMonthlyValueTarget(Double monthlyValueTarget) {
        this.monthlyValueTarget = monthlyValueTarget;
    }

    public Double getMTDValueTarget() {
        return mTDValueTarget;
    }

    public void setMTDValueTarget(Double mTDValueTarget) {
        this.mTDValueTarget = mTDValueTarget;
    }

    public Integer getMTDValueAchievement() {
        return mTDValueAchievement;
    }

    public void setMTDValueAchievement(Integer mTDValueAchievement) {
        this.mTDValueAchievement = mTDValueAchievement;
    }

    public Double getMTDValPer() {
        return mTDValPer;
    }

    public void setMTDValPer(Double mTDValPer) {
        this.mTDValPer = mTDValPer;
    }
}
