package com.fanyukeji.fanyuwallet.utils;

import java.io.File;
import java.io.FileNotFoundException;

public class FileUtil {
    private final String TAG = "FileUtil";

    public static void delete(File file) throws FileNotFoundException {

        if (file.exists()) {
            deleteFile(file);
        } else {
            throw new FileNotFoundException();
        }

    }

    public static void deleteFile(File file) {

        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (File fl : files) {
                deleteFile(fl);
            }

            deleteFile(file);
        }
    }

}
