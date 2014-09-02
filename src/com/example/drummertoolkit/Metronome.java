package com.example.drummertoolkit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Metronome {

        private int bpm;
        private int beat;
        private int noteValue;
        private int silence;
        private int subdivision=3;

        private double beatSound;
        private double sound;
    	private double[] soundTickArray;
    	private double[] soundTockArray;
    	private double[] soundTuckArray;
    	private double[] silenceSoundArray;
    	
    	private int currentBeat = 1;
    	private int currentSubdivision = 1;
    	
    	private Message msg;
    	private Handler mHandler;
    	
    	public Metronome(Handler handler) {
    		audioGenerator.createPlayer();
    		this.mHandler = handler;
    	}
    	
        public double getSound() {
			return sound;
		}

		public void setSound(double sound) {
			this.sound = sound;
		}

		private final int tick = 250; // samples of tick

        private boolean play = true;

        private AudioGenerator audioGenerator = new AudioGenerator(8000);

        public Metronome() {
                audioGenerator.createPlayer();
        }

       
        
        
        
        public double[] concatenate (double[] a, double[] b) {
        	double[] result = new double[a.length + b.length];
        	System.arraycopy(a, 0, result, 0,  a.length);
        	System.arraycopy(b, 0, result, a.length, b.length);
			return result;
        }
        
        private double[] getTick() //Accent
        {
        	double tickSample1[]=audioGenerator.getSineWave(50, 8000, 2000);
        	double tickSample2[]=audioGenerator.getSineWave(200, 8000, 2200);
        	
        	
        	double finalSample[] = concatenate(tickSample1, tickSample2);
        	return finalSample;
        }
        
        private double [] getTock()
        {
        	double tockSample1[]=audioGenerator.getSineWave(50, 8000,1500);
        	double tockSample2[]=audioGenerator.getSineWave(200, 8000,1700);
        	double finalSample[] = concatenate(tockSample1, tockSample2);
        	return finalSample;
        }
        
        private double [] getTuck() //Subdivision
        {
        	double tuckSample1[]=audioGenerator.getSineWave(50, 8000,900);
        	double tuckSample2[]=audioGenerator.getSineWave(200, 8000,1100);
        	double finalSample[] = concatenate(tuckSample1, tuckSample2);
        	return finalSample;
        }

        public void calcSilence() {
    		msg = new Message();
    		msg.obj = ""+currentBeat;
        	silence = (int) (((60/(double)bpm/(double)subdivision)*8000)-tick);	
    		Log.i("DTk", "BPM:"+Integer.toString(bpm) + Integer.toString(silence));
    		soundTickArray = new double[this.tick];	
    		soundTockArray = new double[this.tick];
    		soundTuckArray = new double[this.tick];
    		silenceSoundArray = new double[this.silence];
    		double[] tick = getTick();
    		double[] tock = getTock();
    		double[] tuck = getTuck();
    		for(int i=0;i<this.tick;i++) {
    			soundTickArray[i] = tick[i];
    			soundTockArray[i] = tock[i];
    			soundTuckArray[i] = tuck[i];
    		}
    		for(int i=0;i<silence;i++)
    			silenceSoundArray[i] = 0;
    	}
    	
    	public void play() {
    		calcSilence();
    		do {
    			msg = new Message();
    			msg.obj = ""+currentBeat;
    			if (currentSubdivision==1)
    			{
	    			if(currentBeat == 1)
	    				audioGenerator.writeSound(soundTickArray);
	    			else
	    				audioGenerator.writeSound(soundTockArray);
	    			
	    			
    			}
    			else
    			{
    				audioGenerator.writeSound(soundTuckArray);
    			}

    			mHandler.sendMessage(msg);
    			audioGenerator.writeSound(silenceSoundArray);
    			currentSubdivision++;
    			if (currentSubdivision>subdivision)
    			{
    				currentBeat++;
    				currentSubdivision=1;
    			}
    			if(currentBeat > beat)
    				currentBeat = 1;
    		} while(play);
    	}

        public double getBpm() {
			return bpm;
		}

		public void setBpm(int bpm) {
			this.bpm = bpm;
		}

		public int getBeat() {
			return beat;
		}

		public void setBeat(int beat) {
			this.beat = beat;
		}

		public int getNoteValue() {
			return noteValue;
		}

		public void setNoteValue(int noteValue) {
			this.noteValue = noteValue;
		}

		public double getBeatSound() {
			return beatSound;
		}

		public void setBeatSound(double beatSound) {
			this.beatSound = beatSound;
		}

		public void stop() {
                play = false;
                audioGenerator.destroyAudioTrack();
        }
		
		public double getSubdivision() {
			return subdivision;
		}

		public void setSubdivision(int subdivision) {
			this.subdivision = subdivision;
		}

        /* Getters and Setters ... */
}