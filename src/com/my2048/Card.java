package com.my2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {

	private TextView label;

	public Card(Context context) {
		super(context);

		label = new TextView(getContext());
		label.setTextSize(32);
		label.setGravity(Gravity.CENTER);
//		label.setTextColor(0x00000000);
		
		LayoutParams lp = new LayoutParams(-1, -1);
		lp.setMargins(10, 10, 0, 0);
		addView(label, lp);
		setNum(0);
	}

	public TextView getLabel() {
		return label;
	}
	
	private int num = 0;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;

		if (num <= 0) {
			label.setText("");
		}else{
			label.setText(num+"");
		}
		
		switch (num) {
		case 0:
			label.setBackgroundColor(0x33000000);
			break;
		case 2:
			label.setBackgroundColor(0xffC4FB8C);
			break;
		case 4:
			label.setBackgroundColor(0xff97E747);
			break;
		case 8:
			label.setBackgroundColor(0xff74A642);
			break;
		case 16:
			label.setBackgroundColor(0xff2B8C27);
			break;
		case 32:
			label.setBackgroundColor(0xff24BE5F);
			break;
		case 64:
			label.setBackgroundColor(0xff2BDAA6);
			break;
		case 128:
			label.setBackgroundColor(0xff10EEFD);
			break;
		case 256:
			label.setBackgroundColor(0xff0486DC);
			break;
		case 512:
			label.setBackgroundColor(0xff0831C7);
			break;
		case 1024:
			label.setBackgroundColor(0xff7B5DC1);
			break;
		case 2048:
			label.setBackgroundColor(0xff54208B);
			break;
		case 4096:
			label.setBackgroundColor(0xffC334D9);
			break;
		case 8192:
			label.setBackgroundColor(0xffFF66E6);
			break;
		default:
			label.setBackgroundColor(0xffFF0080);
			break;
		}
	}

	public boolean equals(Card o) {
		return getNum() == o.getNum();
	}
}
