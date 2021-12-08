package com.kudashov.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import kotlin.math.max
import kotlin.random.Random

class SimpleCustomView @JvmOverloads constructor(
    context: Context,
    private val attr: AttributeSet? = null,
    private val defStyleAttr: Int = -1
) : View(context, attr, defStyleAttr) {

    @ColorInt
    private var listColors: List<Int> = listOf(
        Color.GREEN,
        Color.RED,
        Color.YELLOW
    ) as MutableList<Int>

    @ColorInt
    private val defaultFigureColor: Int = Color.GREEN

    @ColorInt
    private var bgColor: Int = Color.WHITE

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val figurePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val viewRect = Rect()
    private lateinit var resBitmap: Bitmap

    private val listFigures: MutableList<Bitmap> = ArrayList()
    private val listFigureType: List<FigureType> = listOf(
        FigureType.CIRCLE, FigureType.RECT, FigureType.ROUND_RECT
    )

    private enum class FigureType {
        CIRCLE, RECT, ROUND_RECT
    }

    init {
        if (attr != null) {
            val typedArray = context.obtainStyledAttributes(attr, R.styleable.SimpleCustomView)
            bgColor = typedArray.getColor(
                R.styleable.SimpleCustomView_scv_background_color,
                Color.WHITE
            )
            typedArray.recycle()
        }
        setup()
    }

    @JvmName("setFiguresColorsInt")
    fun setFiguresColors(list: List<Int>) {
        listColors = list
    }

    @JvmName("setFiguresColorsHex")
    fun setFiguresColors(list: List<String>) {
        listColors = list.map {
            Log.d("TAG", "setFiguresColors: ${Color.parseColor(it)}")
            Color.parseColor(it)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == 0) return

        with(viewRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }
        prepareBitmaps(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawBitmap(resBitmap, viewRect, viewRect, null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (listFigures.size < 10) {
                listFigures.add(createRandomFigure(event.x, event.y))
            } else {
                listFigures.clear()
                Toast.makeText(context, "Game over", Toast.LENGTH_SHORT).show()
            }
        }
        prepareBitmaps(viewRect.right, viewRect.bottom)
        invalidate()
        return false
    }

    private fun setup() {
        with(bgPaint) {
            color = bgColor
            style = Paint.Style.FILL
        }
        with(figurePaint) {
            color = defaultFigureColor
            style = Paint.Style.FILL
        }
        with(textPaint) {
            color = Color.WHITE
            style = Paint.Style.FILL
            textSize = 50f
        }
    }

    private fun prepareBitmaps(w: Int, h: Int) {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawBitmap(
            prepareBackgroundBitmap(w, h),
            0f,
            0f,
            null
        )

        for (bm in listFigures) {
            canvas.drawBitmap(bm, viewRect, viewRect, null)
        }

        canvas.drawBitmap(
            prepareTextBitmap(w, h),
            0f,
            0f,
            null
        )

        resBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun prepareBackgroundBitmap(w: Int, h: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawRect(viewRect, bgPaint)

        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun prepareTextBitmap(w: Int, h: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawText("Количество фигур: ${listFigures.size}", 50f, 50f, textPaint)

        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun createRandomFigure(x: Float, y: Float): Bitmap {
        val figureBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val figureCanvas = Canvas(figureBitmap)
        if (listColors.size != 0) {
            figurePaint.color = listColors.random()
        } else {
            figurePaint.color = defaultFigureColor
        }

        val width = (20..100).random().toFloat()
        val height = (20..100).random().toFloat()

        when (listFigureType.random()) {
            FigureType.CIRCLE -> figureCanvas.drawCircle(x, y, max(width, height), figurePaint)
            FigureType.RECT -> figureCanvas.drawRect(
                x - width / 2,
                y - height / 2,
                x + width / 2,
                y + height / 2,
                figurePaint
            )
            FigureType.ROUND_RECT -> figureCanvas.drawRoundRect(
                RectF(
                    x - width / 2,
                    y - height / 2,
                    x + width / 2,
                    y + height / 2
                ),
                10f,
                10f,
                figurePaint
            )
        }

        return figureBitmap.copy(Bitmap.Config.ARGB_8888, true)
    }
}