package com.mygdx.game.gameplay.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.My2048Game;
import com.mygdx.game.gameplay.actor.custom.CustomGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.gameplay.blocks.Colors;


public class CardGroup extends CustomGroup {
    private Image myBgImage;
    private Label numberLabel;
    private int number;

    public CardGroup(My2048Game mainGame) {
        super(mainGame);
        init();
    }

    private void init() {
        myBgImage = new Image(getGame2048().getAssetManager().get(AssetDescriptors.GAMEPLAY).findRegion(RegionNames.CARD_BACKGROUND));
        addActor(myBgImage);
        setSize(myBgImage.getWidth(), myBgImage.getHeight());


        LabelStyle style = new LabelStyle();
        style.font = getGame2048().getCustomFont();
        style.fontColor = new Color(0x5E5B51FF);

        numberLabel = new Label("0", style);
        numberLabel.setFontScale(0.8f);

        numberLabel.setSize(numberLabel.getPrefWidth(), numberLabel.getPrefHeight());

        numberLabel.setX(getWidth() / 2 - numberLabel.getWidth() / 2);
        numberLabel.setY(getHeight() / 2 - numberLabel.getHeight() / 2);

        addActor(numberLabel);
        setNumber(number);
    }

    public void setNumber(int num) {
        this.number = num;

        if (this.number == 0) {
            numberLabel.setText("");
        } else {
            numberLabel.setText(String.valueOf(this.number));
        }

        numberLabel.setWidth(numberLabel.getPrefWidth());
        numberLabel.setX(getWidth() / 2 - numberLabel.getWidth() / 2);

        switch (this.number) {
            case 2: {
                myBgImage.setColor(Colors.RGBA_2);
                break;
            }
            case 4: {
                myBgImage.setColor(Colors.RGBA_4);
                break;
            }
            case 8: {
                myBgImage.setColor(Colors.RGBA_8);
                break;
            }
            case 16: {
                myBgImage.setColor(Colors.RGBA_16);
                break;
            }
            case 32:{
                myBgImage.setColor(Colors.RGBA_32);
                break;
            }
            case 64: {
                myBgImage.setColor(Colors.RGBA_64);
                break;
            }
            case 128:
            case 60: {
                myBgImage.setColor(Colors.RGBA_128);
                break;
            }
            case 256: {
                myBgImage.setColor(Colors.RGBA_256);
                break;
            }
            case 512: {
                myBgImage.setColor(Colors.RGBA_512);
                break;
            }
            case 1024: {
                myBgImage.setColor(Colors.RGBA_1024);
                break;
            }
            case 2048: {
                myBgImage.setColor(Colors.RGBA_2048);
                break;
            }
            default: {
                myBgImage.setColor(Colors.RGBA_0);
                break;
            }
        }
    }
}
