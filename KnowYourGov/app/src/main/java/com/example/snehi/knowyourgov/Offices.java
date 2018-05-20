package com.example.snehi.knowyourgov;

/**
 * Created by snehi on 2017-04-10.
 */

public class Offices {

    String name;
    int indices[];

    public Offices(String name, int[] indices) {
        this.name = name;
        this.indices = indices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }
}
