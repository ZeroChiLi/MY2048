package com.my2048;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class Countdown {
	private static int time = 10;
	private static int now_time = 10;
	private static Countdown countdown;
	private static Timer timer = null;
	private static TimerTask task = null;
	private static TextView textview;

	public Countdown() {
		countdown = this;
	}

	public static Countdown getCountdown() {
		return countdown;
	}

	public static int getTime() {
		return time;
	}

	public static void setTime(int time) {
		Countdown.time = time;
		now_time = time;
	}

	public static int getNow_time() {
		return now_time;
	}

	public static void setNow_time(int now_time) {
		Countdown.now_time = now_time;
	}

	public static void setTimer(Timer timer) {
		Countdown.timer = timer;
	}

	public static Timer getTimer() {
		return timer;
	}

	public static TextView putView(TextView putView) {
		textview = putView;
		return putView;
	}

	private static Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			textview.setText(msg.arg1 + " s");
			start();
			if (now_time == 0) {
				timer.cancel();
				GameView.isComplete(true);
			}
		};
	};

	public static void start() {
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				now_time--;
				Message message = mHandler.obtainMessage();
				message.arg1 = now_time;
				mHandler.sendMessage(message);
			}
		};
		timer.schedule(task, 1000);
	}
}