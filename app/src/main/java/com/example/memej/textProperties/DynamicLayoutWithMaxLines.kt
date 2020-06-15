package com.example.memej.textProperties

import android.text.DynamicLayout
import android.text.Layout
import android.text.TextDirectionHeuristic
import android.text.TextDirectionHeuristics
import android.text.TextUtils.TruncateAt
import android.util.Log
import java.lang.reflect.Constructor

object StaticLayoutWithMaxLines {

    private const val LOGTAG = "MaxLines"
    private const val TEXT_DIR_CLASS = "android.text.TextDirectionHeuristic"
    private const val TEXT_DIRS_CLASS = "android.text.TextDirectionHeuristics"
    private const val TEXT_DIR_FIRSTSTRONG_LTR = "FIRSTSTRONG_LTR"
    private var sInitialized = false
    private var sConstructor: Constructor<DynamicLayout>? = null
    private lateinit var sConstructorArgs: Array<Any?>
    private var sTextDirection: Any? = null

    @Synchronized
    fun ensureInitialized() {
        if (sInitialized) {
            return
        }
        try {
            val textDirClass: Class<*>
            textDirClass = TextDirectionHeuristic::class.java
            sTextDirection = TextDirectionHeuristics.FIRSTSTRONG_LTR

            val signature = arrayOf(
                CharSequence::class.java,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                TextPaint::class.java,
                Int::class.javaPrimitiveType,
                Layout.Alignment::class.java,
                textDirClass,
                Float::class.javaPrimitiveType,
                Float::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType,
                TruncateAt::class.java,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )

            // Make the StaticLayout constructor with max lines public
            sConstructor =
                DynamicLayout::class.java.getDeclaredConstructor(*signature)
            sConstructor!!.isAccessible = true
            sConstructorArgs = arrayOfNulls(signature.size)
        } catch (e: NoSuchMethodException) {
            Log.e(
                LOGTAG,
                "StaticLayout constructor with max lines not found.",
                e
            )
        } catch (e: ClassNotFoundException) {
            Log.e(
                LOGTAG,
                "TextDirectionHeuristic class not found.",
                e
            )
        } catch (e: NoSuchFieldException) {
            Log.e(
                LOGTAG,
                "TextDirectionHeuristics.FIRSTSTRONG_LTR not found.",
                e
            )
        } catch (e: IllegalAccessException) {
            Log.e(
                LOGTAG,
                "TextDirectionHeuristics.FIRSTSTRONG_LTR not accessible.",
                e
            )
        } finally {
            sInitialized = true
        }
    }

    @Synchronized
    fun create(
        source: CharSequence?, bufstart: Int, bufend: Int,
        paint: TextPaint?, outerWidth: Int, align: Layout.Alignment?,
        spacingMult: Float, spacingAdd: Float,
        includePad: Boolean, ellipsize: TruncateAt?,
        ellipsisWidth: Int, maxLines: Int
    ): DynamicLayout {
        ensureInitialized()
        return try {
            sConstructorArgs[0] = source
            sConstructorArgs[1] = bufstart
            sConstructorArgs[2] = bufend
            sConstructorArgs[3] = paint
            sConstructorArgs[4] = outerWidth
            sConstructorArgs[5] = align
            sConstructorArgs[6] = sTextDirection
            sConstructorArgs[7] = spacingMult
            sConstructorArgs[8] = spacingAdd
            sConstructorArgs[9] = includePad
            sConstructorArgs[10] = ellipsize
            sConstructorArgs[11] = ellipsisWidth
            sConstructorArgs[12] = maxLines
            sConstructor!!.newInstance(*sConstructorArgs)
        } catch (e: Exception) {
            throw IllegalStateException("Error creating StaticLayout with max lines: $e")
        }
    }
}