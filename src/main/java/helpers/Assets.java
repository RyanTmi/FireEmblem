package helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

    private final AssetManager assetManager = new AssetManager();

    public Assets() {
        loadTerrainTextures();
        loadUnitTextures();
        loadBackgroundImages();
        loadSkin();
        loadSound();
        assetManager.finishLoading();
    }

    private void loadUnitTextures() {
        //Textures Armored
        assetManager.load("img/unit_img/arm_0.png", Texture.class);
        assetManager.load("img/unit_img/arm_1.png", Texture.class);
        assetManager.load("img/unit_img/arm_2.png", Texture.class);
        assetManager.load("img/unit_img/arm_3.png", Texture.class);

        //Textures Cavalry
        assetManager.load("img/unit_img/cav_0.png", Texture.class);
        assetManager.load("img/unit_img/cav_1.png", Texture.class);
        assetManager.load("img/unit_img/cav_2.png", Texture.class);
        assetManager.load("img/unit_img/cav_3.png", Texture.class);

        //Textures Flying
        assetManager.load("img/unit_img/fly_0.png", Texture.class);
        assetManager.load("img/unit_img/fly_1.png", Texture.class);
        assetManager.load("img/unit_img/fly_2.png", Texture.class);
        assetManager.load("img/unit_img/fly_3.png", Texture.class);

        //Textures Infantry
        assetManager.load("img/unit_img/inf_0.png", Texture.class);
        assetManager.load("img/unit_img/inf_1.png", Texture.class);
        assetManager.load("img/unit_img/inf_2.png", Texture.class);
        assetManager.load("img/unit_img/inf_3.png", Texture.class);
    }

    private void loadTerrainTextures() {
        // Textures d'Eau
        assetManager.load("img/tile_img/shore.png", Texture.class);
        assetManager.load("img/tile_img/deep_water.png", Texture.class);
        assetManager.load("img/tile_img/shallow.png", Texture.class);
        assetManager.load("img/tile_img/sand.png", Texture.class);

        // Textures terrains
        assetManager.load("img/tile_img/field.png", Texture.class);
        assetManager.load("img/tile_img/road.png", Texture.class);
        assetManager.load("img/tile_img/forest.png", Texture.class);
        assetManager.load("img/tile_img/hill.png", Texture.class);
        assetManager.load("img/tile_img/mountain.png", Texture.class);
        assetManager.load("img/tile_img/void.png", Texture.class);
    }

    private void loadSkin() {
        assetManager.load("ui/uiskin.atlas", TextureAtlas.class);
        assetManager.load("ui/uiskin.json", Skin.class);
    }

    private void loadSound() {
        // Sounds
        assetManager.load("sound/button_clic.ogg", Sound.class);
        assetManager.load("sound/dropSoundEffect.wav", Sound.class);
        assetManager.load("sound/sword-1a.ogg", Sound.class);
        assetManager.load("sound/sword-1b.ogg", Sound.class);
        assetManager.load("sound/sword-arm-2a.ogg", Sound.class);
        assetManager.load("sound/sword-arm-2b.ogg", Sound.class);

        // Musics
        assetManager.load("sound/menu_music_theme.ogg", Music.class);
        assetManager.load("sound/TheyDiedforUs.ogg", Music.class);
    }

    private void loadBackgroundImages() {
        assetManager.load("img/game_n_options_bg.png", Texture.class);
        assetManager.load("img/main_menu_bg.png", Texture.class);
        assetManager.load("img/loading_bg.png", Texture.class);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
