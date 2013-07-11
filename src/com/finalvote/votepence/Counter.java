package com.finalvote.votepence;

public class Counter {
	private int num;
	
	public Counter () {
		this.num = 0;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int limit) {
		this.num = limit;
	}
	
	public void increment() {
		this.num++;
	}
}
