package app.rbzeta.contactmanager.application;

import android.app.Application;

import app.rbzeta.contactmanager.helper.MyDBHandler;
import app.rbzeta.contactmanager.rest.NetworkService;

/**
 * Created by Robyn on 05/11/2016.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private NetworkService mNetworkService;
    private MyDBHandler myDBHandler;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
        mNetworkService = new NetworkService();
        myDBHandler = MyDBHandler.getInstance(this);
    }

    public static synchronized MyApplication getInstance(){return mInstance;}

    public NetworkService getNetworkService(){
        return mNetworkService;
    }

    public MyDBHandler getDBHandler(){return myDBHandler;}
}
