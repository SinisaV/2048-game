package com.mygdx.game.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.My2048Game;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();
    private final Preferences PREFS;
    private List<Score> scores;

    private GameManager() {
        PREFS = Gdx.app.getPreferences(My2048Game.class.getSimpleName());
        scores = new ArrayList<>();
    }

    public void saveScore(Score score) {
        scores.add(score);
        saveScoresToJson();
    }

    public List<Score> loadScores() {
        String scoresJson = Gdx.files.local("scores.json").exists() ?
                Gdx.files.local("scores.json").readString() :
                "[]";

        Json json = new Json();
        scores = json.fromJson(ArrayList.class, Score.class, scoresJson);
        return scores;
    }

    private void saveScoresToJson() {
        Json json = new Json();
        String scoresJson = json.prettyPrint(scores);
        Gdx.files.local("scores.json").writeString(scoresJson, false);
    }
}
