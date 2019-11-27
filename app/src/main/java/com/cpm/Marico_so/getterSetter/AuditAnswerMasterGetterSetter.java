
package com.cpm.Marico_so.getterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuditAnswerMasterGetterSetter {

    @SerializedName("Audit_Answer_Master")
    @Expose
    private List<AuditAnswerMaster> auditAnswerMaster = null;

    public List<AuditAnswerMaster> getAuditAnswerMaster() {
        return auditAnswerMaster;
    }

    public void setAuditAnswerMaster(List<AuditAnswerMaster> auditAnswerMaster) {
        this.auditAnswerMaster = auditAnswerMaster;
    }

}
