package com.example.drummertoolkit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MetronomeActivity extends Activity {
	private MetronomeTask metroTask;
	private boolean playing =false;
	private int currentBpm=100;
	Metronome metronome;
	
	private int maxBpm=280;
	private int minBpm=30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_metronme);
		metroTask = new MetronomeTask();
		
		final SeekBar sk=(SeekBar) findViewById(R.id.bpmBar);     
	
	    sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       

	    @Override       
	    public void onStopTrackingTouch(SeekBar seekBar) {      
	        // TODO Auto-generated method stub      
	    }       

	    @Override       
	    public void onStartTrackingTouch(SeekBar seekBar) {     
	        // TODO Auto-generated method stub      
	    }       

	    @Override       
	    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
	        // TODO Auto-generated method stub      

	    	setBpm(progress);

	    }       
	});     
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void toggleMetronome(View view)
	{
		Log.d("DTk","Clickazo");
        if (playing==false)
        {
        	metroTask.execute();
        	playing=true;
        }
        else
        {
        	metroTask.stop();
        	playing=false;
        	metroTask = new MetronomeTask();
        }
	}
	
	public void addBpm(View view){
		this.setBpm(this.currentBpm+1);
	}
	
	private void setBpm(int bpm)
	{
		currentBpm = bpm;
		if (currentBpm<minBpm)
		{
			currentBpm=minBpm;
		}
		if (currentBpm>maxBpm)
		{
			currentBpm=maxBpm;
		}
		
		this.metronome.setBpm(bpm);
		this.metronome.calcSilence();
		
		TextView bpmText = (TextView) findViewById(R.id.bpmText);
		bpmText.setText(Double.toString(this.currentBpm));
		final SeekBar sk=(SeekBar) findViewById(R.id.bpmBar);   
		sk.setProgress(currentBpm);
	}
	
	private class MetronomeTask extends AsyncTask<Void, Void, String>
	{

		
    	
    	MetronomeTask() {
            //mHandler = getHandler();
    		metronome = new Metronome();
    	}
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
	        metronome.setBeat(4);
	        metronome.setBeatSound(1600);
	        metronome.setSound(2200);
	        metronome.setNoteValue(1);
	        metronome.setBpm(currentBpm);
	        
	        metronome.play();
			return null;
		}
		
		public void stop()
		{
			metronome.stop();
			metronome=null;
		}
		
		
	}
}
