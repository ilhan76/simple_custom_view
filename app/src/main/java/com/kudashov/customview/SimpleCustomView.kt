package com.kudashov.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt

class SimpleCustomView @JvmOverloads constructor(
    context: Context,
    private val attr: AttributeSet? = null,
    private val defStyleAttr: Int = -1
) : View(context, attr, defStyleAttr) {

    @ColorInt
    private val listColors: MutableList<Int> = listOf(Color.GREEN) as MutableList<Int>

    @ColorInt
    private var bgColor: Int = Color.WHITE

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val figurePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val viewRect = Rect()
    private lateinit var resBitmap: Bitmap
    private val listFigures: MutableList<Bitmap> = ArrayList()

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
            when (event.action){
                MotionEvent.ACTION_DOWN ->{
                    val x = event.x
                    val y = event.y

                    listFigures.add(createFigureBitmap(x, y))
                    prepareBitmaps(viewRect.right, viewRect.bottom)

                    Toast.makeText(context, "$x ||| $y", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return false
    }

    private fun setup() {
        with(bgPaint) {
            color = bgColor
            style = Paint.Style.FILL
        }
        with(figurePaint) {
            color = Color.GREEN
            style = Paint.Style.FILL
        }
        with(bitmapPaint) {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
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

        canvas.drawBitmap(createFigureBitmap(10f, 100f), viewRect, viewRect, null)
        canvas.drawBitmap(createFigureBitmap(50f, 150f), viewRect, viewRect, null)
        canvas.drawBitmap(createFigureBitmap(150f, 100f), viewRect, viewRect, null)
        canvas.drawBitmap(createFigureBitmap(150f, 300f), viewRect, viewRect, null)

        Log.d("TAG", "prepareBitmaps: $listFigures")
        for (bm in listFigures) {
            canvas.drawBitmap(bm, viewRect, viewRect, null)
        }

        resBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun prepareBackgroundBitmap(w: Int, h: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawRect(viewRect, bgPaint)

        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun createFigureBitmap(x: Float, y: Float): Bitmap {
        val figureBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val figureCanvas = Canvas(figureBitmap)
        figureCanvas.drawRect(x, y, x + 50f, y + 50f, figurePaint)

        return figureBitmap.copy(Bitmap.Config.ARGB_8888, true)
    }
}