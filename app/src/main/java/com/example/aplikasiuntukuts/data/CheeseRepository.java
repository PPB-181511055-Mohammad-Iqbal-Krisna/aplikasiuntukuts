package com.example.aplikasiuntukuts.data;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CheeseRepository {
    private CheeseDao mCheeseDao;
    private LiveData<List<Cheese>> mAllCheese;
    public CheeseRepository(Application application) {
        SampleDatabase db = SampleDatabase.getInstance(application);
        mCheeseDao = db.cheese();
        mAllCheese = mCheeseDao.getAllCheese();
    }
    LiveData<List<Cheese>> getmAllCheese(){
        return mAllCheese;
    }
}
