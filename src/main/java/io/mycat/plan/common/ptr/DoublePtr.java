package io.mycat.plan.common.ptr;

public class DoublePtr {
	private double db;

	public DoublePtr(double db) {
		this.db = db;
	}

	public double get() {
		return db;
	}

	public void set(double db) {
		this.db = db;
	}
}
