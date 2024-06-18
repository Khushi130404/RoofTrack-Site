package com.example.site_supervisor;

public class MaterialConsumptionPojo
{
    int id;
    String assemblyMark;
    String name;
    int qty;
    boolean isEditable = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssemblyMark() {
        return assemblyMark;
    }

    public void setAssemblyMark(String assemblyMark) {
        this.assemblyMark = assemblyMark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public int getQty()
    {
        return qty;
    }

    public void setQty(int qty)
    {
        this.qty = qty;
    }
}
