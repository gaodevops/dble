package io.mycat.plan.common.item.function.bitfunc;

import java.math.BigInteger;
import java.util.List;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;

import io.mycat.plan.common.field.Field;
import io.mycat.plan.common.item.Item;
import io.mycat.plan.common.item.function.primary.ItemFuncBit;

public class ItemFuncLeftShift extends ItemFuncBit {

	public ItemFuncLeftShift(Item a, Item b) {
		super(a, b);
	}

	@Override
	public final String funcName() {
		return "<<";
	}

	@Override
	public BigInteger valInt() {
		BigInteger arg1 = args.get(0).valInt();
		if (args.get(0).nullValue) {
			nullValue = true; /* purecov: inspected */
			return BigInteger.ZERO; /* purecov: inspected */
		}
		int shift = args.get(1).valInt().intValue();
		if (args.get(1).nullValue) {
			nullValue = true;
			return BigInteger.ZERO;
		}
		nullValue = false;
		return shift < Long.SIZE * 8 ? arg1.shiftLeft(shift) : BigInteger.ZERO;
	}

	@Override
	public SQLExpr toExpression() {
		return new SQLBinaryOpExpr(args.get(0).toExpression(), SQLBinaryOperator.LeftShift, args.get(1).toExpression());
	}

	@Override
	protected Item cloneStruct(boolean forCalculate, List<Item> calArgs, boolean isPushDown, List<Field> fields) {
		List<Item> newArgs = null;
		if (!forCalculate)
			newArgs = cloneStructList(args);
		else
			newArgs = calArgs;
		return new ItemFuncLeftShift(newArgs.get(0), newArgs.get(1));
	}
}
