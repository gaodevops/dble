package io.mycat.plan.common.item.function.timefunc;

import java.util.List;

import io.mycat.plan.common.item.Item;
import io.mycat.plan.common.time.MySQLTime;

public class ItemFuncTime extends ItemTimeFunc {

	public ItemFuncTime(List<Item> args) {
		super(args);
	}
	
	@Override
	public final String funcName() {
		return "time";
	}

	@Override
	public boolean getTime(MySQLTime ltime) {
		return getArg0Time(ltime);
	}

}
