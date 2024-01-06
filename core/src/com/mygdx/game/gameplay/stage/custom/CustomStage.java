package com.mygdx.game.gameplay.stage.custom;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.My2048Game;

public abstract class CustomStage extends Stage {

    private My2048Game mainGame;

    public CustomStage(My2048Game mainGame, Viewport viewport) {
        super(viewport);
        this.mainGame = mainGame;
    }

    public My2048Game getGame2048() {
        return mainGame;
    }
}
