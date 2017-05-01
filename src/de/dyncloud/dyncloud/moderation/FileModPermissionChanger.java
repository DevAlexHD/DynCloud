package de.dyncloud.dyncloud.moderation;


import java.io.File;
import java.io.FilePermission;
import java.nio.file.attribute.PosixFilePermission;

/**
 * Created by Alexander on 14.04.2017.
 */
public class FileModPermissionChanger {

    public File pathToFile;

    public FileModPermissionChanger(File pathToFile) {
        this.pathToFile = pathToFile;
    }

    public void start() {
        pathToFile.setExecutable(true);

    }

}
