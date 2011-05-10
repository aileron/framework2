/**
 * 
 */
package cc.aileron.generic.util;

/**
 * @author aileron
 */
public abstract class Katakana
{
    /**
     * 半角カタカナ
     */
    private static final String[] HALFWIDTH_KANA = new String[] { "ｧ", "ｱ",
            "ｨ", "ｲ", "ｩ", "ｳ", "ｪ", "ｴ", "ｫ", "ｵ", "ｶ", "ｶﾞ", "ｷ", "ｷﾞ", "ｸ",
            "ｸﾞ", "ｹ", "ｹﾞ", "ｺ", "ｺﾞ", "ｻ", "ｻﾞ", "ｼ", "ｼﾞ", "ｽ", "ｽﾞ", "ｾ",
            "ｾﾞ", "ｿ", "ｿﾞ", "ﾀ", "ﾀﾞ", "ﾁ", "ﾁﾞ", "ｯ", "ﾂ", "ﾂﾞ", "ﾃ", "ﾃﾞ",
            "ﾄ", "ﾄﾞ", "ﾅ", "ﾆ", "ﾇ", "ﾈ", "ﾉ", "ﾊ", "ﾊﾞ", "ﾊﾟ", "ﾋ", "ﾋﾞ",
            "ﾋﾟ", "ﾌ", "ﾌﾞ", "ﾌﾟ", "ﾍ", "ﾍﾞ", "ﾍﾟ", "ﾎ", "ﾎﾞ", "ﾎﾟ", "ﾏ", "ﾐ",
            "ﾑ", "ﾒ", "ﾓ", "ｬ", "ﾔ", "ｭ", "ﾕ", "ｮ", "ﾖ", "ﾗ", "ﾘ", "ﾙ", "ﾚ",
            "ﾛ", "ﾜ", "ﾜ", "ｲ", "ｴ", "ｦ", "ﾝ", "ｳﾞ" };

    /**
     * HALFWIDTH_NUMBER
     */
    private static final String[] HALFWIDTH_NUMBER = new String[] { "0", "1",
            "2", "3", "4", "5", "6", "7", "8", "9" };

    /**
     * 全角カタカナ start end
     */
    private static final char START_KANA = 'ァ', END_KANA = 'ヴ';

    /**
     * 全角数字 start end
     */
    private static final char START_NUM = '０', END_NUM = '９';

    /**
     * @param org
     * @return half width katakana
     */
    public static String fullwidth2halfwidth(final String org)
    {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = org.toCharArray();
        final int size = chars.length;

        int c = 0;
        for (int i = 0; i < size; i++)
        {
            /*
             * 変換後文字
             */
            final String conv = getFull2halfChar(chars[i]);
            if (conv == null)
            {
                continue;
            }

            /*
             * 変換対象で無い文字列を追加
             */
            final int idx = i;
            if (c < idx)
            {
                sb.append(org.substring(c, idx));
            }

            /*
             * 変換後文字列の追加
             */
            sb.append(conv);

            /*
             * 処理済みインディックスを更新
             */
            c = i + 1;
        }
        if (c < size)
        {
            sb.append(org.substring(c));
        }

        return sb.toString();
    }

    private static String getFull2halfChar(final char token)
    {
        switch (token)
        {
        case '・':
            return "･";

        case 'ー':
            return "ｰ";

        case '−':
            return "-";
        }

        /*
         * 全角数字の範囲
         */
        if (START_NUM <= token && token <= END_NUM)
        {
            return HALFWIDTH_NUMBER[token - START_NUM];
        }

        /*
         * 全角カタカナの範囲
         */
        if (START_KANA <= token && token <= END_KANA)
        {
            return HALFWIDTH_KANA[token - START_KANA];
        }

        /*
         * どれにも一致せず
         */
        return null;
    }
}
