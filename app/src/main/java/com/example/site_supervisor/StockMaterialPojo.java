package com.example.site_supervisor;

public class StockMaterialPojo
{
    int pos;
    String code;
    String itemname;
    float dcQty;
    float usedQty;
    float stockQty;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public float getDcQty() {
        return dcQty;
    }

    public void setDcQty(float dcQty) {
        this.dcQty = dcQty;
    }

    public float getUsedQty() {
        return usedQty;
    }

    public void setUsedQty(float usedQty) {
        this.usedQty = usedQty;
    }

    public float getStockQty() {
        return stockQty;
    }

    public void setStockQty(float stockQty) {
        this.stockQty = stockQty;
    }
}
