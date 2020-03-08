package com.synnex.shellexecutor.bo;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String group;
    private List<Category> categories = new ArrayList<>();

    public Group() {
    }

    public Group(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
