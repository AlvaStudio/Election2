package com.alvastudio.election2.Classes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alvastudio.election2.Models.Item;
import com.alvastudio.election2.Models.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class FileUtils {
    public static ArrayList<Item> loadDataLocal(Context context) {
        ArrayList<Item> arrayList = new ArrayList<>();
        File file = new File(context.getFilesDir(), Constants.FILE_ITEMS);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = context.openFileInput(Constants.FILE_ITEMS);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                arrayList = (ArrayList<Item>) objectInputStream.readObject();
                objectInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("FileUtils", "file not found");
        }
        return arrayList;
    }

    public static Boolean writeDataLocal(ArrayList<Item> arrayList, Context context) {
        boolean result = false;

        try {
            FileOutputStream fos = context.openFileOutput(Constants.FILE_ITEMS, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arrayList);
            oos.flush();
            oos.close();
            result = true;
            Log.d("FileUtils", "Success Writing item file");
        } catch (Exception e) {
            Log.d("FileUtils", "Error Writing file - " + e.toString());
            e.printStackTrace();
        }
        return result;
    }
}
