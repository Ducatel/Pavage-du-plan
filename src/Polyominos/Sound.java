package Polyominos;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

/**
 * Class qui va permettre la lecture de la musique
 * @author David Ducatel
 *
 */
public class Sound implements Runnable {

	/**
	 * Variable contenant le son a jouer
	 */
	private SourceDataLine line;

	/**
	 * Variable contenant le flux audio d'entree
	 */
	private AudioInputStream fluxAudio;

	/**
	 * variable contenant le format audio du flux d'entree
	 */
	private AudioFormat audioFormat;

	/**
	 * boolean permettant l'arret de la lecture
	 */
	private boolean stop = false;
	
	/**
	 * Objet contenant les different reglage de son possible (sourdine, reverb, etc ...)
	 */
	private Mixer mixer;
	
	/**
	 * Controlleur de sourdine
	 */
	private BooleanControl mute;
	
	/**
	 * Controlleur de balance
	 */
    private FloatControl balanceControl;
    
    /**
     * Objet permettant la mise a jour de l'IHM
     */
    private Gestion g;
    
    
    /**
     * Constructeur du lecteur de la musique
     */
	public Sound(boolean supportedMixer) {
		

		// on place le boolean d'arret a false
		stop = false;

		try {
			// recuperation d'un flux audio a partir du fichier wav contenu dans
			// le classPath
			fluxAudio = AudioSystem.getAudioInputStream(getClass()
					.getClassLoader().getResourceAsStream(
							"resources/tetris.wav"));

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(supportedMixer){
			 //Recherche du mixer logiciel Java Sound Audio Engine
	        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
	
	            if (mixerInfo.getName().compareTo("Java Sound Audio Engine") == 0) {
	                mixer = AudioSystem.getMixer(mixerInfo);
	                break;
	            }
	
	        }
		}

		// recuperation du format audio du flux
		audioFormat = fluxAudio.getFormat();

		// permet de pouvoir definir des actions sur le flux
		// (demarre/pause/arret)
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);

		try {
			// on recupere la ligne de sortie son que nous voulons
			if(supportedMixer)
				line = (SourceDataLine) mixer.getLine(info);
			else
				line = (SourceDataLine) AudioSystem.getLine(info);

		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		if(supportedMixer){
			//Recuperation du controlleur de sourdine
			if (line.isControlSupported(BooleanControl.Type.MUTE)) {
	            mute = (BooleanControl) line
	                    .getControl(BooleanControl.Type.MUTE);
	        }
			
			//recupération du controlleur de balance
	        if (line.isControlSupported(FloatControl.Type.PAN)) {
	            balanceControl = (FloatControl) line.getControl(FloatControl.Type.PAN);
	        }
		}
 
	}
	
	/**
	 * Methode permettant de lancer la lecture de la musique
	 */
	public void run() {
		
		//lancement de la lecture
		lecture();		
		
		//une fois la lecture terminé on replace les bouton dans les etats initiaux
		resetAffichage();
		
	}
	
	/**
	 * Fonction qui va tester si la JVM supportant l'application
	 * peut gerer les truc mixer
	 * @return true si la JVM gere les mixer sinon false
	 */
	public static boolean supportedMixer(){
		
		 //Recherche du mixer logiciel Java Sound Audio Engine
		Mixer tmp=null;
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {

            if (mixerInfo.getName().compareTo("Java Sound Audio Engine") == 0) {
               tmp = AudioSystem.getMixer(mixerInfo);
                break;
            }

        }
        
        //si tmp est null, cela indique que la JVM ne gere pas les mixer
		if(tmp==null)
			return false;
		else
			return true;
		
        	
	}
	
	/**
	 * Fonction qui passe l'objet de l'IHM pour les mise a jour
	 * @param g Objet de l'ihm
	 */
	public void setGestion(Gestion g){
		this.g=g;
	}
	
	/**
	 * Fonction qui retourne le controleur de balance
	 * @return le controleur de balance
	 */
    public FloatControl getBalanceControl() {
        return balanceControl;
    }

	/**
	 * Methode permettant de couper la lecture du son
	 */
	public void stop() {
		stop = true;
	}
	
	/**
	 * Fonction qui retourne le controleur de sourdine
	 * @return le controleur de sourdine
	 */
    public BooleanControl getMute() {
        return mute;
    }
    
    /**
     * Fonction qui replace l'IHM aux etats initiaux
     */
    private void resetAffichage(){
    	g.play.setEnabled(true);
		g.stop.setEnabled(false);
		
		if(Gestion.supportedMixer){
			g.balance.setValue(0);
			g.balance.setEnabled(false);
			g.mute.setIcon(new ImageIcon(getClass().getResource("/resources/son.png")));
			g.mute.setEnabled(false);
			g.isMute=false;
		}
	}
	
	/**
	 * Methode effectuant la lecture du son
	 */
	private void lecture() {
		// Ouverture de la source du son a jouer
		try {
			line.open(audioFormat);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		// redirection du flux son vers la carte son
		line.start();

		// lecture du stream audio (lecture d'inputStream standard)
		try {
			byte bytes[] = new byte[1024];
			int bytesRead = 0;
			while (((bytesRead = fluxAudio.read(bytes, 0, bytes.length)) != -1)
					&& !stop) {

				line.write(bytes, 0, bytesRead);
			}
		} catch (IOException io) {
			io.printStackTrace();
		}

		// on ferme la source du son
		line.close();

	}
}
