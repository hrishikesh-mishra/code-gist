package com.hrishikeshmishra.intellij.plugin.code.model.actions;

public class CodeDetail {

    private String fileName;
    private String fileContent;
    private String GistId;

    public CodeDetail(String fileName, String fileContent, String gistId) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        GistId = gistId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public String getGistId() {
        return GistId;
    }

    @Override
    public String toString() {
        return "CodeDetail{" +
                "fileName='" + fileName + '\'' +
                ", fileContent='" + fileContent + '\'' +
                ", GistId='" + GistId + '\'' +
                '}';
    }
}
