package com.example.drummertoolkit;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	
	public void goToMetronome(View view)
	{
		Intent nextScreen = new Intent(this, MetronomeActivity.class);
    	//Log.i("DTk","ADFADSF");
    	//nextScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(nextScreen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	

}
