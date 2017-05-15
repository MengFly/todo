package com.example.mengfei.todo;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.mengfei.todo.utils.AppFileManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Created by mengfei on 2017/5/15.
 */
@RunWith(AndroidJUnit4.class)
public class FileManagerTest {

    @Test
    public void testRootFile() {
        Log.d("test", "testRootFile: " + AppFileManager.getAppRootFileDir(true, TodoApplication.getContext()));
        Log.d("test", "testRootFile: " + AppFileManager.getAppRootCacheDir(true, TodoApplication.getContext()));
        Log.d("test", "testRootFile: " + AppFileManager.getAppRootFileDir(false, TodoApplication.getContext()));
        Log.d("test", "testRootFile: " + AppFileManager.getAppRootCacheDir(false, TodoApplication.getContext()));


    }

}
