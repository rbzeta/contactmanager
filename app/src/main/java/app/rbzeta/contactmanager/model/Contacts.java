package app.rbzeta.contactmanager.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 05/11/2016.
 */

public class Contacts {
    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
