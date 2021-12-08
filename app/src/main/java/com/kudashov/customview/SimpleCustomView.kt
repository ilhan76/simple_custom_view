package com.kudashov.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
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
            val x = event.x
            val y = event.y
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawRect(x, y, 50f, 50f, figurePaint)
            listFigures.add(bitmap.copy(Bitmap.Config.ARGB_8888, true))
            prepareBitmaps(width, height)

            Toast.makeText(context, "$x ||| $y", Toast.LENGTH_SHORT).show()
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
    }

    private fun prepareBitmaps(w: Int, h: Int) {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        //bgPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.ADD)
        canvas.drawRect(viewRect, bgPaint)
        canvas.drawRect(50f, 50f, 50f, 50f, figurePaint)

        for (bm in listFigures){
            canvas.drawBitmap(bm, viewRect, viewRect, null)
        }
        Toast.makeText(context, "${listFigures.size}", Toast.LENGTH_SHORT).show()
/*        val fbm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val fc = Canvas(fbm)
        fc.drawRect(50f, 50f, 50f, 50f, paint)
        canvas.drawBitmap(fbm, null, viewRect, paint)*/

        resBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }
}