package com.inf8405.projet_final.marathon;

import java.util.UUID;

/**
 * Created by youssef on 03/04/2016.
 */
public class Participant {

    String id;
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");

    public Participant()
    {
        id=uidFormat_.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
