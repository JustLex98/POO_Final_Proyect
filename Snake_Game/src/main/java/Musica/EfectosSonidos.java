package Musica;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.net.URL;
        
        
public class EfectosSonidos {
    private Clip currentClip;
    private String musicaActual;
    
    public void playMusicLoop(String path){
    
     // Evitar reiniciar la misma música si ya está sonando
        if (musicaActual != null && musicaActual.equals(path) && currentClip != null && currentClip.isRunning()) {
            return;
        }
        
    stop(); // Detener música anterior
    try{
        URL url = getClass().getClassLoader().getResource("sounds/" + path);
        if (url == null) return;
        
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
        currentClip = AudioSystem.getClip();
        currentClip.open(audioIn);
        currentClip.loop(Clip.LOOP_CONTINUOUSLY);
        currentClip.start();
    }
    catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
        e.printStackTrace();
    }
    
    }
    
    public void playSound(String path){
        try{
            URL url = getClass().getClassLoader().getResource("sounds/" + path);
            if (url == null) return;
            
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    
    }
    
    public void stop(){
        if (currentClip != null && currentClip.isRunning()){
            currentClip.stop();
            currentClip.close();
            currentClip = null;
            musicaActual = null;
        
        }
    }
}