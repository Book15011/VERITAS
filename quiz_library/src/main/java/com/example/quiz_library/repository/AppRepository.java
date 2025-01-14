package com.example.quiz_library.repository;

import com.example.quiz_library.local_data.source.LocalPref;

public class AppRepository {
    private static AppRepository appRepository;
    private LocalPref appRef;
    private AppRepository() {
        appRef=LocalPref.getInstance();
    }
     public static AppRepository getInstance() {
        return appRepository=new AppRepository();
    }
    public void savePosition(int position){
        appRef.setSavePosition(position);
    }
    public int getPosition(){
        return appRef.getPosition();
    }
}
