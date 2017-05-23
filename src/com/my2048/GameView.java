package com.my2048;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

public class GameView extends GridLayout {

	// private static TextView tvSocre, tvBestScore, tvLowestScore;
	private static Card[][] cardsMap = new Card[Config.LINES][Config.LINES];
	private static List<Point> emptyPoints = new ArrayList<Point>();
	private static TextView textview2;
	private static GameView gameView = null;
	private static MainActivity acv = MainActivity.getMainActivity();

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		gameView = this;
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		gameView = this;
		initGameView();
	}

	public GameView(Context context) {
		super(context);

		gameView = this;
		initGameView();
	}

	public static GameView getGameView() {
		return gameView;
	}

	public static TextView putView2(TextView putView) {
		textview2 = putView;
		return putView;
	}

	// public static TextView puttvSocre(TextView puttvSocre) {
	// tvSocre = puttvSocre;
	// return puttvSocre;
	// }public static TextView puttvBestScore(TextView putBestView) {
	// tvBestScore = putBestView;
	// return putBestView;
	// }public static TextView puttvLowestScore(TextView puttvLowestScoreView) {
	// tvLowestScore = puttvLowestScoreView;
	// return puttvLowestScoreView;
	// }
	@SuppressLint("ClickableViewAccessibility")
	public void initGameView() {
		setColumnCount(Config.LINES);
		setBackgroundColor(0x00000000);

		setOnTouchListener(new View.OnTouchListener() {

			private float startX, startY, offsetX, offsetY;

			public boolean onTouch(View v, MotionEvent event) {
				if (Countdown.getNow_time() > 0 || MainActivity.getMode() == 0) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						startX = event.getX();
						startY = event.getY();
						break;
					case MotionEvent.ACTION_UP:
						offsetX = event.getX() - startX;
						offsetY = event.getY() - startY;

						if (Math.abs(offsetX) > Math.abs(offsetY)) {
							if (offsetX < -5) {
								swipeLeft();
							} else if (offsetX > 5) {
								swipeRight();
							}
						} else {
							if (offsetY < -5) {
								swipeUp();
							} else if (offsetY > 5) {
								swipeDown();
							}
						}
						isComplete(checkComplete());
						break;
					}
				}
				return true;
			}
		});
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		Config.CARD_WIDTH = (Math.min(w, h) - 10) / Config.LINES;

		addCards(Config.CARD_WIDTH, Config.CARD_WIDTH);

		startGame();
	}

	private void addCards(int cardWidth, int cardHeight) {

		Card c;

		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {
				c = new Card(getContext());
				c.setNum(0);
				addView(c, cardWidth, cardHeight);

				cardsMap[x][y] = c;
			}
		}
	}

	public static void startGame() {

		acv.clearScore();
		acv.showBestScore(acv.getBestScore());
		acv.showLowestScore(acv.getLowestScore());
		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {
				cardsMap[x][y].setNum(0);
			}
		}
		addRandomNum();
		addRandomNum();
	}

	private static void addRandomNum() {

		emptyPoints.clear();

		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {
				if (cardsMap[x][y].getNum() <= 0) {
					emptyPoints.add(new Point(x, y));
				}
			}
		}

		Point p = emptyPoints.get((int) (Math.random() * emptyPoints.size()));
		cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);

		acv.getAnimLayer().createScaleTo1(cardsMap[p.x][p.y]);
	}

	private void swipeLeft() {
		boolean move = false;
		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {
				for (int x1 = x + 1; x1 < Config.LINES; x1++) {
					if (cardsMap[x1][y].getNum() > 0) {
						if (cardsMap[x][y].getNum() <= 0) {
							acv.getAnimLayer().createMoveAnim(cardsMap[x1][y],
									cardsMap[x][y], x1, x, y, y);
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							x--;
							move = true;
						} else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							acv.getAnimLayer().createMoveAnim(cardsMap[x1][y],
									cardsMap[x][y], x1, x, y, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x1][y].setNum(0);
							acv.addScore(cardsMap[x][y].getNum());
							if (MainActivity.getMode() == 1) {
								switch (cardsMap[x][y].getNum()) {
								case 4:
									break;
								default:
									Countdown.setNow_time(Countdown
											.getNow_time()
											+ (int) (Math.log(cardsMap[x][y]
													.getNum()) / Math.log(2))
											- 2);
									break;
								}
							}
							move = true;
						}
						break;
					}
				}
			}
		}
		if (move) {
			addRandomNum();
		}
	}

	private void swipeRight() {
		boolean move = false;
		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 3; x >= 0; x--) {
				for (int x1 = x - 1; x1 >= 0; x1--) {
					if (cardsMap[x1][y].getNum() > 0) {
						if (cardsMap[x][y].getNum() <= 0) {
							acv.getAnimLayer().createMoveAnim(cardsMap[x1][y],
									cardsMap[x][y], x1, x, y, y);
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							x++;
							move = true;
						} else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							acv.getAnimLayer().createMoveAnim(cardsMap[x1][y],
									cardsMap[x][y], x1, x, y, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x1][y].setNum(0);
							acv.addScore(cardsMap[x][y].getNum());
							if (MainActivity.getMode() == 1) {
								switch (cardsMap[x][y].getNum()) {
								case 4:
									break;
								default:
									Countdown.setNow_time(Countdown
											.getNow_time()
											+ (int) (Math.log(cardsMap[x][y]
													.getNum()) / Math.log(2))
											- 2);
									break;
								}
							}
							move = true;
						}
						break;
					}
				}
			}
		}
		if (move) {
			addRandomNum();
		}
	}

	private void swipeUp() {
		boolean move = false;
		for (int x = 0; x < Config.LINES; x++) {
			for (int y = 0; y < Config.LINES; y++) {
				for (int y1 = y + 1; y1 < Config.LINES; y1++) {
					if (cardsMap[x][y1].getNum() > 0) {
						if (cardsMap[x][y].getNum() <= 0) {
							acv.getAnimLayer().createMoveAnim(cardsMap[x][y1],
									cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							y--;
							move = true;
						} else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							acv.getAnimLayer().createMoveAnim(cardsMap[x][y1],
									cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x][y1].setNum(0);
							acv.addScore(cardsMap[x][y].getNum());
							if (MainActivity.getMode() == 1) {
								switch (cardsMap[x][y].getNum()) {
								case 4:
									break;
								default:
									Countdown.setNow_time(Countdown
											.getNow_time()
											+ (int) (Math.log(cardsMap[x][y]
													.getNum()) / Math.log(2))
											- 2);
									break;
								}
							}
							move = true;
						}
						break;
					}
				}
			}
		}
		if (move) {
			addRandomNum();
		}
	}

	private void swipeDown() {
		boolean move = false;
		for (int x = 0; x < Config.LINES; x++) {
			for (int y = 3; y >= 0; y--) {
				for (int y1 = y - 1; y1 >= 0; y1--) {
					if (cardsMap[x][y1].getNum() > 0) {
						if (cardsMap[x][y].getNum() <= 0) {
							acv.getAnimLayer().createMoveAnim(cardsMap[x][y],
									cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							y++;
							move = true;
						} else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							acv.getAnimLayer().createMoveAnim(cardsMap[x][y1],
									cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x][y1].setNum(0);
							acv.addScore(cardsMap[x][y].getNum());
							if (MainActivity.getMode() == 1) {
								switch (cardsMap[x][y].getNum()) {
								case 4:
									break;
								default:
									Countdown.setNow_time(Countdown
											.getNow_time()
											+ (int) (Math.log(cardsMap[x][y]
													.getNum()) / Math.log(2))
											- 2);
									break;
								}
							}
							move = true;
						}
						break;
					}
				}
			}
		}
		if (move) {
			addRandomNum();
		}
	}

	private boolean checkComplete() {
		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {
				if (cardsMap[x][y].getNum() == 0
						|| (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y]))
						|| (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y]))
						|| (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1]))
						|| (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {
					return false;
				}
			}
		}
		return true;
	}

	public static void isComplete(boolean complete) {
		if (complete) {
			if (acv.getLowestScore() <= 0) {
				acv.saveLowestScore(acv.getScore());
				acv.showLowestScore(acv.getScore());
			} else {
				acv.addLowestScore(acv.getScore());
			}
			new AlertDialog.Builder(acv)
					.setTitle("傻叼")
					.setMessage("没了")
					.setPositiveButton("再来，不怂",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									if (MainActivity.getMode() == 1) {
										Countdown.getTimer().cancel();
										Countdown.setTime(10);
										textview2.setText(Countdown.getTime()
												+ " s");
										Countdown.putView(textview2);
										Countdown.start();
									}
									startGame();
								}
							}).show();
		}
	}

}