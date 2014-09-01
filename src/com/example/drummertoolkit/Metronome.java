package com.example.drummertoolkit;

import android.util.Log;

public class Metronome {

        private int bpm;
        private int beat;
        private int noteValue;
        private int silence;

        private double beatSound;
        private double sound;
    	private double[] soundTickArray;
    	private double[] soundTockArray;
    	private double[] silenceSoundArray;
    	
    	private int currentBeat = 1;
    	
    	
        public double getSound() {
			return sound;
		}

		public void setSound(double sound) {
			this.sound = sound;
		}

		private final int tick = 300; // samples of tick

        private boolean play = true;

        private AudioGenerator audioGenerator = new AudioGenerator(8000);

        public Metronome() {
                audioGenerator.createPlayer();
        }

       
        
        private double[] getTick()
        {
        	double tickSample1[]=audioGenerator.getSineWave(80, 8000, 2000);
        	double tickSample2[]=audioGenerator.getSineWave(220, 8000, 2200);
        	
        	
        	double finalSample[] = concatenate(tickSample1, tickSample2);
        	return finalSample;
        }
        
        public double[] concatenate (double[] a, double[] b) {
        	double[] result = new double[a.length + b.length];
        	System.arraycopy(a, 0, result, 0,  a.length);
        	System.arraycopy(b, 0, result, a.length, b.length);
			return result;
        }
        
        private double [] getTock()
        {
        	double tockSample1[]=audioGenerator.getSineWave(80, 8000,1400);
        	double tockSample2[]=audioGenerator.getSineWave(220, 8000,1600);
        	double finalSample[] = concatenate(tockSample1, tockSample2);
        	return finalSample;
        }

        public void calcSilence() {
    		silence = (int) (((60/(double)bpm)*8000)-tick);	
    		Log.i("DTk", "BPM:"+Integer.toString(bpm) + Integer.toString(silence));
    		soundTickArray = new double[this.tick];	
    		soundTockArray = new double[this.tick];
    		silenceSoundArray = new double[this.silence];
    		//msg = new Message();
    	//	msg.obj = ""+currentBeat;
    		double[] tick = getTick();
    		double[] tock = getTock();
    		for(int i=0;i<this.tick;i++) {
    			soundTickArray[i] = tick[i];
    			soundTockArray[i] = tock[i];
    		}
    		for(int i=0;i<silence;i++)
    			silenceSoundArray[i] = 0;
    	}
    	
    	public void play() {
    		calcSilence();
    		do {
    			//msg = new Message();
    			//msg.obj = ""+currentBeat;
    			if(currentBeat == 1)
    				audioGenerator.writeSound(soundTickArray);
    			else
    				audioGenerator.writeSound(soundTockArray);				
    			//if(bpm <= 120)
    			//	mHandler.sendMessage(msg);
    			audioGenerator.writeSound(silenceSoundArray);
    			//if(bpm > 120)
    			//	mHandler.sendMessage(msg);
    			currentBeat++;
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

        /* Getters and Setters ... */
}