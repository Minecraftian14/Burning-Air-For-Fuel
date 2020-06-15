package main;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum Sound {
	NEXT("next.wav"),
	LAUNCH("launch.wav"),
	CLICK("click.wav"),
	SHOOT("shoot.wav"),
	BLAST("blast.wav"),
	ENEMYDEATH("enemydeath.wav");
	
	private Clip clip;
	
	Sound(String soundName) {
		try {
			URL url = this.getClass().getClassLoader().getResource(soundName);
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (UnsupportedAudioFileException e3) {
			e3.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		if(clip.isRunning())
			clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}
	
	static void init() {
		values();
	}
	
}



















