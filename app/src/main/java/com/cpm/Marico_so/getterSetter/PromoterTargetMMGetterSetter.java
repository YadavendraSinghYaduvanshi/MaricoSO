package com.cpm.Marico_so.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromoterTargetMMGetterSetter {

    @SerializedName("Promoter_Target_MM")
    @Expose
    private List<PromoterTargetMM> promoterTarget = null;

    public List<PromoterTargetMM> getPromoterTarget() {
        return promoterTarget;
    }

    public void setPromoterTarget(List<PromoterTargetMM> promoterTarget) {
        this.promoterTarget = promoterTarget;
    }
}
