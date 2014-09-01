package com.example.drummertoolkit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
	Metronome metronome;
	
	private int maxBpm=280;
	private int minBpm=30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_metronme);
		metroTask = new MetronomeTask();
		setBpm(100);
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
	    Spinner beatsSpinner = (Spinner) findViewById(R.id.signature_beats);
	    // Create an ArrayAdapter using the string array and a default spinner layout
	    ArrayAdapter<CharSequence> beatsAdapter = ArrayAdapter.createFromResource(this,
	         R.array.signature_beats, android.R.layout.simple_spinner_item);
	 	// Specify the layout to use when the list of choices appears
	    beatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 	// Apply the adapter to the spinner
	 	beatsSpinner.setAdapter(beatsAdapter);
	 	
	 	beatsSpinner.setOnItemSelectedListener(this);
	 	
	 	Spinner valueSpinner = (Spinner) findViewById(R.id.signature_value);
	    // Create an ArrayAdapter using the string array and a default spinner layout
	    ArrayAdapter<CharSequence> valueAdapter = ArrayAdapter.createFromResource(this,
	         R.array.signature_value, android.R.layout.simple_spinner_item);
	 	// Specify the layout to use when the list of choices appears
	    valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 	// Apply the adapter to the spinner
	    valueSpinner.setAdapter(valueAdapter);
	 	
	    valueSpinner.setOnItemSelectedListener(this);
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
	
	private void setBeats(int beats)
	{
		metronome.setBeat(beats);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		
		Log.i("DTk", "Item selected: "+  parent.getId());
		setBeats(Integer.parseInt(parent.getItemAtPosition(pos).toString()));
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
