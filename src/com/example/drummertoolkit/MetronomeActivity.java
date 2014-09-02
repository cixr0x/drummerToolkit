package com.example.drummertoolkit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class MetronomeActivity extends Activity implements OnItemSelectedListener {
	private MetronomeTask metroTask;
	private boolean playing =false;
	private int currentBpm;
	private int beats;
	private int subdivision;
	private int volume;
	Metronome metronome;
	
	private int maxBpm=240;
	private int minBpm=20;
	
	private AudioManager audio;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_metronme);
		metroTask = new MetronomeTask();
		setBpm(100);
		final SeekBar bpmBar=(SeekBar) findViewById(R.id.bpmBar);     
		bpmBar.setOnSeekBarChangeListener(bpmBarListener);
		
	    Spinner beatsSpinner = (Spinner) findViewById(R.id.signature_beats);
	    ArrayAdapter<CharSequence> beatsAdapter = ArrayAdapter.createFromResource(this,
	         R.array.signature_beats, android.R.layout.simple_spinner_item);
	    beatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 	beatsSpinner.setAdapter(beatsAdapter);
	 	beatsSpinner.setOnItemSelectedListener(this);
	 	
	 	Spinner valueSpinner = (Spinner) findViewById(R.id.signature_value);
	    ArrayAdapter<CharSequence> valueAdapter = ArrayAdapter.createFromResource(this,
	         R.array.signature_value, android.R.layout.simple_spinner_item);
	    valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    valueSpinner.setAdapter(valueAdapter);
	    valueSpinner.setOnItemSelectedListener(this);
	    
	    Spinner subdivisionSpinner = (Spinner) findViewById(R.id.subdivision_spinner);
	    ArrayAdapter<CharSequence> subdivisionAdapter = ArrayAdapter.createFromResource(this,
	         R.array.subdivision, android.R.layout.simple_spinner_item);
	    subdivisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    subdivisionSpinner.setAdapter(subdivisionAdapter);
	    subdivisionSpinner.setOnItemSelectedListener(this);
	    audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	    
	    SeekBar volumeBar = (SeekBar) findViewById(R.id.volumeBar);
	    volumeBar.setMax(audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
	    volumeBar.setProgress(volume);
        volumeBar.setOnSeekBarChangeListener(volumeListener);
	}
	
	private Handler getHandler() {
    	return new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	String message = (String)msg.obj;
            	TextView beatCounter = (TextView) findViewById(R.id.beatCounter);
            	beatCounter.setText(message);
            }
        };
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
	
	public void subsBpm(View view){
		this.setBpm(this.currentBpm-1);
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

		this.metronome.setBpm(currentBpm);
		this.metronome.calcSilence();
		
		TextView bpmText = (TextView) findViewById(R.id.bpmText);
		bpmText.setText(Double.toString(this.currentBpm));
		final SeekBar sk=(SeekBar) findViewById(R.id.bpmBar);   
		sk.setProgress(currentBpm);
	}
	
	private class MetronomeTask extends AsyncTask<Void, Void, String>
	{

		
    	
    	MetronomeTask() {
            mHandler = getHandler();
    		metronome = new Metronome(mHandler);
    	}
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
	        metronome.setBeat(beats);
	        metronome.setSubdivision(subdivision);
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
	
	private void setSubdivision(int subdivision)
	{
		this.subdivision=subdivision;
		metronome.setSubdivision(subdivision);
		metronome.calcSilence();
	}
	private void setBeats(int beats)
	{
		this.beats=beats;
		metronome.setBeat(beats);
		
	}

	private OnSeekBarChangeListener volumeListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			volume = (short) progress;
			audio.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}   	
    	
    };
    private OnSeekBarChangeListener bpmBarListener = new OnSeekBarChangeListener() {
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
    	
    };
    
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		if (parent.getId() ==R.id.signature_beats)
		{
			setBeats(Integer.parseInt(parent.getItemAtPosition(pos).toString()));
		}
		
		if (parent.getId() ==R.id.signature_value)
		{
			
		}
		
		if (parent.getId() ==R.id.subdivision_spinner)
		{
			setSubdivision(Integer.parseInt(parent.getItemAtPosition(pos).toString()));
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
