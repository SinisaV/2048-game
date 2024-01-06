package com.mygdx.game.gameplay.actor.custom;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.My2048Game;

public abstract class CustomGroup extends Group {

        private My2048Game mainGame;

        public CustomGroup(My2048Game mainGame) {
            this.mainGame = mainGame;
        }

        public My2048Game getGame2048() {
            return mainGame;
        }
}
