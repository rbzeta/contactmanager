package app.rbzeta.contactmanager.helper;

import android.os.Environment;

import java.io.File;

import app.rbzeta.contactmanager.application.AppConfig;

/**
 * Created by Robyn on 05/11/2016.
 */

public class FileUtils {
    public static File getStorageDirectory() {
        return Environment
                .getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES + AppConfig.DIR_IMG_FILE_NAME);
    }
}
