package com.my2048;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private TextView tvSocre, tvBestScore, tvLowestScore, tvS, tvBS, tvLS, tvT,
			tvTime;
	private Button btnHelp, btnRestart;
	private AnimLayer animLayer = null;
	private int score = 0;
	private static MainActivity mainActivity = null;
	private long mExitTime;
	private long ReTime = 0;
	private static int mode = 0;

	public MainActivity() {
		mainActivity = this;
	}

	public static MainActivity getMainActivity() {
		return mainActivity;
	}

	public static int getMode() {
		return mode;
	}

	public static void setMode(int mode) {
		MainActivity.mode = mode;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.timeMode:
			if (mode == 0) {
				mode = 1;
				GameView.startGame();
				tvLS.setVisibility(View.GONE);
				tvLowestScore.setVisibility(View.GONE);
				tvT.setVisibility(View.VISIBLE);
				tvTime.setVisibility(View.VISIBLE);
				Countdown.setTime(10);
				tvTime.setText(Countdown.getTime() + " s");
				Countdown.putView(tvTime);
				GameView.putView2(tvTime);
				Countdown.start();
			}
			break;
		case R.id.classicalMode:
			if (mode == 1) {
				Countdown.getTimer().cancel();
				mode = 0;
				GameView.startGame();
				tvLS.setVisibility(View.VISIBLE);
				tvLowestScore.setVisibility(View.VISIBLE);
				tvT.setVisibility(View.GONE);
				tvTime.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void initView() {
		tvBestScore = (TextView) findViewById(R.id.tvBestScore);
		tvSocre = (TextView) findViewById(R.id.tvScore);
		tvLowestScore = (TextView) findViewById(R.id.tvlowestScore);
		tvS = (TextView) findViewById(R.id.tvS);
		tvLS = (TextView) findViewById(R.id.tvLS);
		tvBS = (TextView) findViewById(R.id.tvBS);
		btnHelp = (Button) findViewById(R.id.btnHelp);
		btnRestart = (Button) findViewById(R.id.btnRestart);
		animLayer = (AnimLayer) findViewById(R.id.animLayer);
		tvT = (TextView) findViewById(R.id.tvT);
		tvTime = (TextView) findViewById(R.id.tvTime);

		btnHelp.setOnClickListener(this);
		btnRestart.setOnClickListener(this);

		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Pointy.ttf");
		tvSocre.setTypeface(typeFace);
		tvS.setTypeface(typeFace);
		tvLowestScore.setTypeface(typeFace);
		tvLS.setTypeface(typeFace);
		tvBestScore.setTypeface(typeFace);
		tvBS.setTypeface(typeFace);
		btnHelp.setTypeface(typeFace);
		btnRestart.setTypeface(typeFace);
		tvT.setTypeface(typeFace);
		tvTime.setTypeface(typeFace);

		// GameView.puttvBestScore(tvBestScore);
		// GameView.puttvLowestScore(tvLowestScore);
		// GameView.puttvSocre(tvSocre);

//		ScoreView.setMain(getMainActivity());
//		ScoreView.setTvBestScore(tvBestScore);
//		ScoreView.setTvLowestScore(tvLowestScore);
//		ScoreView.setTvSocre(tvSocre);
	}

	public void clearScore() {
		score = 0;
		showScore();
	}

	public void showScore() {
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
		if (mode == 0) {
			int minScore = Math.min(s, getLowestScore());
			saveLowestScore(minScore);
			showLowestScore(minScore);
		}
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void saveBestScore(int s) {
		Editor e = getPreferences(MODE_PRIVATE).edit();
		if (mode == 0) {
			e.putInt("bestScore", s);
		} else if (mode == 1) {
			e.putInt("bestTimeScore", s);
		}
		e.commit();
	}

	public void saveLowestScore(int s) {
		Editor e = getPreferences(MODE_PRIVATE).edit();
		e.putInt("lowestScore", s);
		e.commit();
	}

	public int getBestScore() {
		int bestScore = 0;
		if (mode == 0) {
			bestScore = getPreferences(MODE_PRIVATE).getInt("bestScore", 0);
		} else if (mode == 1) {
			bestScore = getPreferences(MODE_PRIVATE).getInt("bestTimeScore", 0);
		}
		return bestScore;
	}

	public int getLowestScore() {
		return getPreferences(MODE_PRIVATE).getInt("lowestScore", 0);
	}

	public void showBestScore(int s) {
		tvBestScore.setText(s + "");
	}

	public void showLowestScore(int s) {
		tvLowestScore.setText(s + "");
	}

	public AnimLayer getAnimLayer() {
		return animLayer;
	}

	// 按两次退出

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("InflateParams")
	public void HelpDialog() {
		final Dialog d = new Dialog(this, R.style.myDialogTheme);
		View vv = LayoutInflater.from(this).inflate(R.layout.dialog_type, null);
		Button btnUnderstand = (Button) vv.findViewById(R.id.btnUnderstand);
		TextView tvHelp = (TextView) vv.findViewById(R.id.tvHelp);
		d.setContentView(vv);
		d.setCanceledOnTouchOutside(true);
		d.show();
		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/Pointy.ttf");
		tvHelp.setTypeface(typeFace);
		btnUnderstand.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Toast imageToast = Toast.makeText(MainActivity.this,
						"这是带图片的toast", Toast.LENGTH_LONG);
				ImageView imageView = new ImageView(MainActivity.this);
				imageView.setImageResource(R.drawable.should_not_xie_l);
				imageToast.setView(imageView);
				imageToast.show();
				d.dismiss();
			}
		});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnHelp:
			HelpDialog();
			break;
		case R.id.btnRestart:
			if ((System.currentTimeMillis() - ReTime) > 2000) {
				Toast.makeText(MainActivity.this,
						"Click on more time to replay", Toast.LENGTH_LONG)
						.show();
				ReTime = System.currentTimeMillis();
			} else {
				Toast imageToast = Toast.makeText(MainActivity.this,
						"这是带图片的toast", Toast.LENGTH_LONG);
				ImageView imageView = new ImageView(MainActivity.this);
				imageView.setImageResource(R.drawable.hehe);
				imageToast.setView(imageView);
				imageToast.show();
				if (mode == 1) {
					Countdown.getTimer().cancel();
					GameView.putView2(tvTime);
					Countdown.setTime(10);
					tvTime.setText(Countdown.getTime() + " s");
					Countdown.start();
				}
				GameView.startGame();
			}
			break;
		default:
			break;
		}
	}

}
