package de.dyncloud.dyncloud.cloudwrapper;

import java.io.File;

/**
 * Created by Alexander on 14.04.2017.
 */
public class CloudFileManager {

    public CloudFileManager() {

    }

    public void checkForNotUsedFiles() {
       File file = new File(CloudWrapper.getWrapper().cloudPath + "temporary");
       for(File files : file.listFiles()) {
           files.delete();
       }
    }
}
