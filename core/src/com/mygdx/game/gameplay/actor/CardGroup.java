package com.mygdx.game.gameplay.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.My2048Game;
import com.mygdx.game.gameplay.actor.custom.CustomGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.gameplay.blocks.Constants;


public class CardGroup extends CustomGroup {
    private Image myBgImage;
    private Label numLabel;
    private int num;

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

        numLabel = new Label("0", style);
        numLabel.setFontScale(0.8f);

        numLabel.setSize(numLabel.getPrefWidth(), numLabel.getPrefHeight());

        numLabel.setX(getWidth() / 2 - numLabel.getWidth() / 2);
        numLabel.setY(getHeight() / 2 - numLabel.getHeight() / 2);

        addActor(numLabel);
        setNum(num);
    }

    public void setNum(int num) {
        this.num = num;

        if (this.num == 0) {
            numLabel.setText("");
        } else {
            numLabel.setText(String.valueOf(this.num));
        }

        numLabel.setWidth(numLabel.getPrefWidth());
        numLabel.setX(getWidth() / 2 - numLabel.getWidth() / 2);

        switch (this.num) {
            case 2: {
                myBgImage.setColor(Constants.CardColors.RGBA_2);
                break;
            }
            case 4: {
                myBgImage.setColor(Constants.CardColors.RGBA_4);
                break;
            }
            case 8: {
                myBgImage.setColor(Constants.CardColors.RGBA_8);
                break;
            }
            case 16: {
                myBgImage.setColor(Constants.CardColors.RGBA_16);
                break;
            }
            case 32:{
                myBgImage.setColor(Constants.CardColors.RGBA_32);
                break;
            }
            case 64: {
                myBgImage.setColor(Constants.CardColors.RGBA_64);
                break;
            }
            case 128:
            case 60: {
                myBgImage.setColor(Constants.CardColors.RGBA_128);
                break;
            }
            case 256: {
                myBgImage.setColor(Constants.CardColors.RGBA_256);
                break;
            }
            case 512: {
                myBgImage.setColor(Constants.CardColors.RGBA_512);
                break;
            }
            case 1024: {
                myBgImage.setColor(Constants.CardColors.RGBA_1024);
                break;
            }
            case 2048: {
                myBgImage.setColor(Constants.CardColors.RGBA_2048);
                break;
            }
            default: {
                myBgImage.setColor(Constants.CardColors.RGBA_0);
                break;
            }
        }
    }
}
