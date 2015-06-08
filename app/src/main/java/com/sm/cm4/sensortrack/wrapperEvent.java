package com.sm.cm4.sensortrack;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Anderson on 06/06/2015.
 */
public abstract class wrapperEvent<T> {

    public abstract List<T> getData();
}
