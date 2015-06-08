package com.sm.cm4.sensortrack;

import android.content.Context;
import android.media.MediaScannerConnection;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Anderson on 08/06/2015.
 */
public class EventToCSV {
    private static String FOLDER = "dados";


    public static <T> void write(Context c, wrapperEvent<T> t) throws IOException {
        String pathExternal =  new String(c.getExternalCacheDir() + File.separator
                + FOLDER + File.separator );

        String nameExternal = pathExternal + "dadosSensor.csv";
        MediaScannerConnection.scanFile(c,
                new String[]{nameExternal}, null, null);

        new File(pathExternal).mkdirs();
        CSVWriter writer = new CSVWriter(new FileWriter(nameExternal));

       // List<String> entries = new ArrayList<String>();
        StringBuffer str = new StringBuffer();
        for(T data: t.getData()) {
            //entries.add(data.toString());
            str.append(data.toString() + "#");
        }
        String[] entries = str.toString().split("#");
        writer.writeNext(entries);


       // writer.writeNext((String [])entries.toArray());
        writer.close();

    }


}
