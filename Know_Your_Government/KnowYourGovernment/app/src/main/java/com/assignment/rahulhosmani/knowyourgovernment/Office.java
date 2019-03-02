package com.assignment.rahulhosmani.knowyourgovernment;

import java.util.List;

public class Office {
    private String  name;
    private List<Integer> officialIndices;

    public Office(String name, List<Integer> officialIndices) {
        this.name = name;
        this.officialIndices = officialIndices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getOfficialIndices() {
        return officialIndices;
    }

    public void setOfficialIndices(List<Integer> officialIndices) {
        this.officialIndices = officialIndices;
    }
}
