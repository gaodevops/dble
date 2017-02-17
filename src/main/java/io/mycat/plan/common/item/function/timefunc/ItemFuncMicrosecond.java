package io.mycat.plan.common.item.function.timefunc;

import java.math.BigInteger;
import java.util.List;

import io.mycat.plan.common.item.Item;
import io.mycat.plan.common.item.function.primary.ItemIntFunc;
import io.mycat.plan.common.time.MySQLTime;

public class ItemFuncMicrosecond extends ItemIntFunc {

	public ItemFuncMicrosecond(List<Item> args) {
		super(args);
	}

	@Override
	public final String funcName() {
		return "microsecond";
	}

	@Override
	public BigInteger valInt() {
		MySQLTime ltime = new MySQLTime();
		return getArg0Time(ltime) ? BigInteger.ZERO : BigInteger.valueOf(ltime.second_part);
	}

	@Override
	public void fixLengthAndDec() {
		maybeNull = true;
	}

}
