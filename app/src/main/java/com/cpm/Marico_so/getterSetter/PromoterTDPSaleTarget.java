package com.cpm.Marico_so.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoterTDPSaleTarget {

    @SerializedName("Store_Id")
    @Expose
    private Integer storeId;
    @SerializedName("Time_Period")
    @Expose
    private String timePeriod;
    @SerializedName("TDP_Volume_Target")
    @Expose
    private Double tDPVolumeTarget;
    @SerializedName("TDP_Volume_Achievement")
    @Expose
    private Integer tDPVolumeAchievement;
    @SerializedName("TDP_Vol_Per")
    @Expose
    private Double tDPVolPer;
    @SerializedName("TDP_Value_Target")
    @Expose
    private Double tDPValueTarget;
    @SerializedName("TDP_Value_Achievement")
    @Expose
    private Integer tDPValueAchievement;
    @SerializedName("TDP_Val_Per")
    @Expose
    private Double tDPValPer;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Double getTDPVolumeTarget() {
        return tDPVolumeTarget;
    }

    public void setTDPVolumeTarget(Double tDPVolumeTarget) {
        this.tDPVolumeTarget = tDPVolumeTarget;
    }

    public Integer getTDPVolumeAchievement() {
        return tDPVolumeAchievement;
    }

    public void setTDPVolumeAchievement(Integer tDPVolumeAchievement) {
        this.tDPVolumeAchievement = tDPVolumeAchievement;
    }

    public Double getTDPVolPer() {
        return tDPVolPer;
    }

    public void setTDPVolPer(Double tDPVolPer) {
        this.tDPVolPer = tDPVolPer;
    }

    public Double getTDPValueTarget() {
        return tDPValueTarget;
    }

    public void setTDPValueTarget(Double tDPValueTarget) {
        this.tDPValueTarget = tDPValueTarget;
    }

    public Integer getTDPValueAchievement() {
        return tDPValueAchievement;
    }

    public void setTDPValueAchievement(Integer tDPValueAchievement) {
        this.tDPValueAchievement = tDPValueAchievement;
    }

    public Double getTDPValPer() {
        return tDPValPer;
    }

    public void setTDPValPer(Double tDPValPer) {
        this.tDPValPer = tDPValPer;
    }

    private String store_name;

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }
}
