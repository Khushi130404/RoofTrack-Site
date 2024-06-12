package com.example.site_supervisor;

public class MaterialConsumptionPojo
{
    int id;
    int projectID;
    String assemblyMark;
    String name;
    Double weight;
    String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
