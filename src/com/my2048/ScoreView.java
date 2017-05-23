package com.my2048;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.widget.TextView;

public class ScoreView extends Activity {

	private static int score;
	private static TextView tvSocre, tvBestScore, tvLowestScore;
	private static ScoreView scoreView = null;
	private static MainActivity main;

	public static MainActivity getMain() {
		return main;
	}

	public static void setMain(MainActivity getmain) {
		main = getmain;
	}

	public static TextView getTvSocre() {
		return tvSocre;
	}

	public static void setTvSocre(TextView getTvSocre) {
		tvSocre = getTvSocre;
	}

	public static TextView getTvBestScore() {
		return tvBestScore;
	}

	public static void setTvBestScore(TextView getTvBestScore) {
		tvBestScore = getTvBestScore;
	}

	public static TextView getTvLowestScore() {
		return tvLowestScore;
	}

	public static void setTvLowestScore(TextView getTvLowestScore) {
		tvLowestScore = getTvLowestScore;
	}

	public ScoreView() {
		scoreView = this;
	}

	public static ScoreView getScoreView() {
		return scoreView;
	}

	public static void clearScore() {
		score = 0;
		showScore();
	}

	public static void showScore() {
		tvSocre.setText(score + "");
	}

	public void addScore(int s) {
		score += s;
		showScore();

		int maxScore = Math.max(score, getBestScore());
		saveBestScore(maxScore);
		showBestScore(maxScore);
	}

	public void addLowestScore(int s) {
		int minScore = Math.min(s, getLowestScore());
		saveLowestScore(minScore);
		showLowestScore(minScore);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		ScoreView.score = score;
	}

	public void saveBestScore(int s) {
		Editor e = MainActivity.getMainActivity().getSharedPreferences("bestScore", MODE_PRIVATE).edit();
		e.putInt("bestScore", s);
		e.commit();
	}

	public void saveLowestScore(int s) {
		Editor e = MainActivity.getMainActivity().getSharedPreferences("lowestScore", MODE_PRIVATE).edit();
		e.putInt("lowestScore", s);
		e.commit();
	}

	public int getBestScore() {
		return MainActivity.getMainActivity().getSharedPreferences("bestScore", MODE_PRIVATE).getInt("bestScore", 0);
	}

	public int getLowestScore() {
		return MainActivity.getMainActivity().getSharedPreferences("lowestScore", MODE_PRIVATE).getInt("lowestScore", 0);
	}

	public void showBestScore(int s) {
		tvBestScore.setText(s + "");
	}

	public void showLowestScore(int s) {
		tvLowestScore.setText(s + "");
	}

}
