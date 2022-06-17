package com.adair.core.utils;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

/**
 * Paint相关工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/26
 */
public class PaintUtils {


    /**
     * 获取文字绘制基线
     *
     * @param paint 画笔
     *
     * @return baseline位置
     */
    public static float getBaseLine(Paint paint) {
        FontMetrics fontMetrics = paint.getFontMetrics();
        return (fontMetrics.descent - fontMetrics.ascent) / 2f - fontMetrics.ascent;
    }

    /**
     * 获取画笔绘制文字高度
     *
     * @param paint 画笔
     *
     * @return 文字高度
     */
    public static float getTextHeight(Paint paint) {
        FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading;
    }

}
