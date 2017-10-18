package com.hrishikeshmishra.intellij.plugin.code.model.gist;

import java.io.File;
import java.util.Map;

public class UpdatePayload {

    private String description;
    private Map<String, FileContent> files;

    public UpdatePayload(String description, Map<String, FileContent> files) {
        this.description = description;
        this.files = files;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, FileContent> getFiles() {
        return files;
    }

    public void setFiles(Map<String, FileContent> files) {
        this.files = files;
    }
}
