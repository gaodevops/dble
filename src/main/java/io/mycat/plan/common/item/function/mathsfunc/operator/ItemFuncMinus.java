package io.mycat.plan.common.item.function.mathsfunc.operator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;

import io.mycat.plan.common.field.Field;
import io.mycat.plan.common.item.Item;
import io.mycat.plan.common.item.function.primary.ItemFuncAdditiveOp;

public class ItemFuncMinus extends ItemFuncAdditiveOp {

	public ItemFuncMinus(Item a, Item b) {
		super(a, b);
	}

	@Override
	public final String funcName() {
		return "-";
	}

	@Override
	public BigDecimal realOp() {
		BigDecimal val0 = args.get(0).valReal();
		BigDecimal val1 = args.get(1).valReal();
		if (this.nullValue = (args.get(0).isNull() || args.get(1).isNull()))
			return BigDecimal.ZERO;
		return val0.subtract(val1);
	}

	@Override
	public BigInteger intOp() {
		BigInteger v0 = args.get(0).valInt();
		BigInteger v1 = args.get(1).valInt();
		if (this.nullValue = (args.get(0).isNull() || args.get(1).isNull()))
			return BigInteger.ZERO;
		return v0.subtract(v1);
	}

	@Override
	public BigDecimal decimalOp() {
		BigDecimal v0 = args.get(0).valDecimal();
		BigDecimal v1 = args.get(1).valDecimal();
		if (this.nullValue = (args.get(0).isNull() || args.get(1).isNull()))
			return null;
		return v0.subtract(v1);
	}

	@Override
	public SQLExpr toExpression() {
		return new SQLBinaryOpExpr(args.get(0).toExpression(), SQLBinaryOperator.Subtract, args.get(1).toExpression());
	}

	@Override
	protected Item cloneStruct(boolean forCalculate, List<Item> calArgs, boolean isPushDown, List<Field> fields) {
		List<Item> newArgs = null;
		if (!forCalculate)
			newArgs = cloneStructList(args);
		else
			newArgs = calArgs;
		return new ItemFuncMinus(newArgs.get(0), newArgs.get(1));
	}

}
