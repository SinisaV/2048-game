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

    private static final String MUSIC_ENABLED_KEY = "menu_music_enabled";
    private static final String SOUND_ENABLED_KEY = "sounds_enabled";
    private static final String SELECTED_GRID_SIZE_KEY = "selected_grid_size";
    private static final String HIGH_SCORE_KEY = "high_score";

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

    public int getHighScore() {
        int maxScore = 0;

        if (scores.isEmpty()) {
            loadScores(); // Make sure scores are loaded
        }

        for (Score score : scores) {
            if (score.getScore() > maxScore) {
                maxScore = score.getScore();
            }
        }

        return maxScore;
    }

    public boolean isMusicEnabled() {
        return PREFS.getBoolean(MUSIC_ENABLED_KEY, true);
    }

    public void setMusicEnabled(boolean enabled) {
        PREFS.putBoolean(MUSIC_ENABLED_KEY, enabled);
        PREFS.flush();
    }

    public boolean isSoundEnabled() {
        return PREFS.getBoolean(SOUND_ENABLED_KEY, true);
    }

    public void setSoundEnabled(boolean enabled) {
        PREFS.putBoolean(SOUND_ENABLED_KEY, enabled);
        PREFS.flush();
    }

    public void setGridSize(int gridSize) {
        PREFS.putInteger(SELECTED_GRID_SIZE_KEY, gridSize);
        PREFS.flush();
    }

    public int getGridSize() {
        return PREFS.getInteger(SELECTED_GRID_SIZE_KEY, 4);
    }

    public void setHighScoreEnabled(boolean enabled) {
        PREFS.putBoolean(HIGH_SCORE_KEY, enabled);
        PREFS.flush();
    }

    public boolean isHighScoreEnabled() {
        return PREFS.getBoolean(HIGH_SCORE_KEY, true);
    }
}
