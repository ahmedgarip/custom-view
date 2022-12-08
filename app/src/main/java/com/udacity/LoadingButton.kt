package com.udacity

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import androidx.lifecycle.MutableLiveData
import java.lang.Integer.min
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0

) : View(context, attrs) {
    private var widthSize = 0
    private var heightSize = 0

    private var text = "DOWNLOAD"
    private var _wasCompleted = MutableLiveData<Boolean>()
    val wasCompleted get() = _wasCompleted
    private val valueAnimator = ValueAnimator.ofInt(0, 100)

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        if(new == ButtonState.Completed){
            text = "Completed"
            _wasCompleted.value = true


        }


    }


    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 75.0f
        typeface = Typeface.create( "", Typeface.BOLD)
        color = Color.WHITE


    }

    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.colorPrimaryDark)
    }
    private val radiusPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.white)
    }
    private var buttonRect : Rect = Rect()
    private var progressRectF : RectF = RectF()
    private var progressRect : Rect = Rect()
    private var  radius = 0.0f
    var progress =  0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = ((h/2) / 2.0 * 0.8).toFloat()
        buttonRect.apply {
            widthSize = ((w*progress)/100).toInt()
            heightSize = h
            left = 0
            top = 0
            right = left + ((w*progress)/100).toInt()
            bottom = top + h

        }
        progressRect.apply {
            widthSize = heightSize
            heightSize = (h/2).toInt()
            left = (w - radius* 4).toInt()
            top = (h/4).toInt()
            right = left + heightSize
            bottom = top + heightSize

        }
        progressRectF.apply {
            widthSize = heightSize
            heightSize = (h/2).toInt()
            left = (w - radius* 4).toFloat()
            top = (h/4).toFloat()
            right = left + heightSize
            bottom = top + heightSize

        }
        radiusPaint.strokeWidth = radius
        this.contentDescription = "Download"

    }

    init {

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {

            text = getString(R.styleable.LoadingButton_text)!!
            progress = getInt(R.styleable.LoadingButton_progress, 0)
        }

        isClickable = true







    }

    override fun performClick(): Boolean {
//        if (super.performClick()) return true
        super.performClick()
        text = context.getString(R.string.button_loading)
        this.contentDescription = "Download progress $progress %"
        animateInAction()
        buttonState = ButtonState.Clicked

        invalidate()
        return true

    }

    private fun animateInAction() {
        valueAnimator.addUpdateListener { valueAnimator ->

            val animatedValue = valueAnimator.animatedValue as Int

            progress = animatedValue
            invalidate()

        }
        valueAnimator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(p0: Animator?) {
                buttonState = ButtonState.Loading
            }

            override fun onAnimationEnd(p0: Animator?) {
                buttonState = ButtonState.Completed


            }

            override fun onAnimationCancel(p0: Animator?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(p0: Animator?) {
                TODO("Not yet implemented")
            }
        })
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 3000L
        valueAnimator.start()



    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(resources.getColor(R.color.colorPrimary))
        canvas?.drawRect(buttonRect.left.toFloat(),buttonRect.top.toFloat(),(buttonRect.left + ((canvas.width*progress)/100)).toFloat(),buttonRect.bottom.toFloat(), rectPaint)
        canvas?.drawText(text, (canvas.width/2).toFloat(), (canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2).toFloat(),textPaint)
//        canvas?.drawCircle((width - radius*3).toFloat(), (height / 2).toFloat(), radius, radiusPaint)
        canvas?.drawArc(progressRectF, -90F, ((progress*360)/100).toFloat(), true,radiusPaint)




    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}