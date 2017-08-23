/**
 *
 */
package io.mycat.plan.common.item.function.strfunc;

import io.mycat.plan.common.item.Item;
import io.mycat.plan.common.item.function.ItemFunc;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ItemFuncUnhex extends ItemStrFunc {

    public ItemFuncUnhex(Item a) {
        super(a);
    }

    @Override
    public final String funcName() {
        return "unhex";
    }

    @Override
    public void fixLengthAndDec() {
        decimals = 0;
        maxLength = (1 + args.get(0).maxLength) / 2;
    }

    @Override
    public String valStr() {
        nullValue = true;
        String hexString = args.get(0).valStr();
        if (args.get(0).nullValue)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(hexString.length() / 2);
        for (int index = 0; index < hexString.length(); index += 2) {
            int hexChar = 0;
            int bValue = 0;
            bValue = ((hexChar = hexcharToInt(hexString.charAt(index))) << 4);
            if (hexChar == -1)
                return null;
            bValue |= (hexChar = hexcharToInt(hexString.charAt(index + 1)));
            if (hexChar == -1)
                return null;
            baos.write(bValue);
        }
        nullValue = false;
        return baos.toString();
    }

    /*
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String decode(String hexString) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(hexString.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < hexString.length(); i += 2)
            baos.write((hexString.indexOf(hexString.charAt(i)) << 4 | hexString.indexOf(hexString.charAt(i + 1))));
        return new String(baos.toByteArray());
    }

    /**
     * convert a hex digit into number.
     */

    public static int hexcharToInt(char c) {
        if (c <= '9' && c >= '0')
            return c - '0';
        c |= 32;
        if (c <= 'f' && c >= 'a')
            return c - 'a' + 10;
        return -1;
    }

    @Override
    public ItemFunc nativeConstruct(List<Item> realArgs) {
        return new ItemFuncUnhex(realArgs.get(0));
    }

}
