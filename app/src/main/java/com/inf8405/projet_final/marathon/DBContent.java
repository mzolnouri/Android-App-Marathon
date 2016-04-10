package com.inf8405.projet_final.marathon;

import java.util.Map;

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

    public Participant getActualParticipant() {
        // To Do
        return null;
    }

    public void updateParticipantInformationInRemoteContent(){
        // To Do
    }

    public Map<String,Marathon> GetMarathonMap(String participantId){
        // To Do
        return null;
    }


}
