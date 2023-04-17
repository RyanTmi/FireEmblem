package helpers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.Random;

public class SoundManager {

    private static float MUSICS_VOLUME = .75f;
    private static float SOUNDS_VOLUME = .75f;

    private final Sound[] gameSounds;
    private final Music[] gameMusics;

    public SoundManager(Assets assets) {
        this.gameSounds = new Sound[6];
        this.gameSounds[0] = assets.getAssetManager().get("sound/sword-1a.ogg");
        this.gameSounds[1] = assets.getAssetManager().get("sound/sword-1b.ogg");
        this.gameSounds[2] = assets.getAssetManager().get("sound/sword-arm-2a.ogg");
        this.gameSounds[3] = assets.getAssetManager().get("sound/sword-arm-2b.ogg");
        this.gameSounds[4] = assets.getAssetManager().get("sound/button_clic.ogg");
        this.gameSounds[5] = assets.getAssetManager().get("sound/dropSoundEffect.wav");

        this.gameMusics = new Music[2];
        this.gameMusics[0] = assets.getAssetManager().get("sound/menu_music_theme.ogg");
        this.gameMusics[1] = assets.getAssetManager().get("sound/TheyDiedforUs.ogg");
    }

    public void dispose() {
        for (Sound sound : gameSounds) {
            sound.dispose();
        }
        for (Music music : gameMusics) {
            music.dispose();
        }
    }

    public void MuteAllMusics() {
        for (Music music : gameMusics) {
            music.setVolume(0f);
        }
        MUSICS_VOLUME = 0f;
        SOUNDS_VOLUME = 0f;
    }

    public void resetAllMusicsVolume() {
        for (Music music : gameMusics) {
            music.setVolume(.5f);
        }
        MUSICS_VOLUME = .5f;
        SOUNDS_VOLUME = .75f;
    }

    // PLAY
    public void playButtonClickSound() {
        gameSounds[4].play(SOUNDS_VOLUME);
    }

    public void playDropSoundEffect() {
        gameSounds[5].play(SOUNDS_VOLUME * .7f, .8f, 0f);
    }

    public void playSwordSoundEffect() {
        Random r = new Random();
        int randomSwordSound = r.nextInt(4); // [0, 3]
        gameSounds[randomSwordSound].play(SOUNDS_VOLUME);
    }

    public void playMainMenuMusic() {
        gameMusics[0].play();
        gameMusics[0].setVolume(MUSICS_VOLUME);
        gameMusics[0].setLooping(true);
    }

    public void playGameMusic() {
        gameMusics[1].play();
        gameMusics[1].setVolume(MUSICS_VOLUME * .5f);
        gameMusics[1].setLooping(true);
    }

    // STOP
    public void stopMainMenuMusic() {
        gameMusics[0].stop();
    }

    public void stopGameMusic() {
        gameMusics[1].stop();
    }
}