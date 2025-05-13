package ru.mirea.ovsyannikov.mireaproject.ui;

import java.io.File;

public class FileItem {
    private String name;
    private File file;

    public FileItem(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }
}
