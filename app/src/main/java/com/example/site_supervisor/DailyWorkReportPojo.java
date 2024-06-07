package com.example.site_supervisor;

public class DailyWorkReportPojo
{
    int id;
    int srno;
    boolean isEditable=false;
    String itemDetails;
    String qty;
    String unit;
    String workQty;
    String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(String itemDetails) {
        this.itemDetails = itemDetails;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWorkQty() {
        return workQty;
    }

    public void setWorkQty(String workQty) {
        this.workQty = workQty;
    }

    public String getRemark() {
        return remark;
    }

    public boolean getEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
