package io.mycat.plan.common.field;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.mycat.plan.common.field.string.FieldStr;
import io.mycat.plan.common.item.FieldTypes;

public class FieldNull extends FieldStr {
	private static FieldNull _instance = new FieldNull("NULL", "", 63, 0, 0, 0);

	public static FieldNull getInstance() {
		return _instance;
	}

	FieldNull(String name, String table, int charsetIndex, int field_length, int decimals, long flags) {
		super(name, table, charsetIndex, field_length, decimals, flags);
		this.ptr = null;
	}

	@Override
	public FieldTypes fieldType() {
		return FieldTypes.MYSQL_TYPE_NULL;
	}

	@Override
	public boolean equals(final Field other, boolean binary) {
		if (this == other)
			return true;
		return false;
	}

	@Override
	public BigDecimal valReal() {
		return BigDecimal.ZERO;
	}

	@Override
	public BigInteger valInt() {
		return BigInteger.ZERO;
	}

	@Override
	public String valStr() {
		return null;
	}

	@Override
	public BigDecimal valDecimal() {
		return null;
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	protected void internalJob() {
	}

	@Override
	public int compareTo(Field other) {
		return -1;
	}

	@Override
	public int compare(byte[] v1, byte[] v2) {
		return -1;
	}

}
