package com.inf8405.projet_final.marathon;

/**
 * Created by youssef on 04/04/2016.
 */
public class DBContent {
    private static DBContent Instance = null;

    public static DBContent getInstance() {
        if (Instance==null)
        {
            Instance=new DBContent();
        }
        return Instance;
    }

    public static void DestroyInstance()
    {
        Instance=null;
    }

    private DBContent() {
    }
}
