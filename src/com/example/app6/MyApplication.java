package com.example.app6;
import org.acra.*;
import org.acra.annotation.*;

import android.app.Application;

@ReportsCrashes(formKey = "dFpDUFVDcFZiUWVvZ1ZyYUtKSWNqTnc6MQ") 
public class MyApplication extends Application{
	 @Override
	  public void onCreate() {
	      super.onCreate();

	      // The following line triggers the initialization of ACRA
	      ACRA.init(this);
	  }
}