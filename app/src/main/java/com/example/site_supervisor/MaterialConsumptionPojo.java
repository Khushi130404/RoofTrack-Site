package com.example.site_supervisor;

public class MaterialConsumptionPojo
{
    int id;
    String assemblyMark;
    String name;
    Double weight;
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
}
