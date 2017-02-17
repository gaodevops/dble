package io.mycat.plan.common.item.function.strfunc;

import java.util.List;

import io.mycat.plan.common.item.Item;
import io.mycat.plan.common.item.function.ItemFunc;


public class ItemFuncUpper extends ItemStrFunc {

	public ItemFuncUpper(List<Item> args) {
		super(args);
	}

	@Override
	public final String funcName() {
		return "upper";
	}

	@Override
	public String valStr() {
		String orgStr = args.get(0).valStr();
		if (this.nullValue = args.get(0).isNull())
			return EMPTY;
		return orgStr.toUpperCase();
	}
	
	@Override
	public ItemFunc nativeConstruct(List<Item> realArgs) {
		return new ItemFuncUpper(realArgs);
	}
}
