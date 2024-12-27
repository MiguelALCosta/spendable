package com.app.spendable.presentation.components

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.content.ContextCompat.getSystemService
import com.app.spendable.R
import com.app.spendable.databinding.ComponentRewardPopupBinding
import com.app.spendable.domain.Avatar
import com.app.spendable.utils.IStringsManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class RewardPopupComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ComponentRewardPopupBinding

    @Inject
    lateinit var stringsManager: IStringsManager

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentRewardPopupBinding.inflate(inflater, this, true)

        alpha = 0.0f
    }

    data class SetupConfig(
        val title: String,
        val initialPoints: Int,
        val gainedPoints: Int
    )

    private fun getNextAvatar(points: Int, finalPoints: Int): Avatar? {
        return if (points == finalPoints) {
            Avatar.progressList.firstOrNull { it.requiredPoints >= points }
        } else {
            Avatar.progressList.firstOrNull { it.requiredPoints > points }
        }
    }

    private fun updateProgress(floatPoints: Float, finalPoints: Int) {
        val points = floatPoints.roundToInt()
        getNextAvatar(points, finalPoints)?.let {
            binding.avatarWrapper.visibility = View.VISIBLE
            binding.next.visibility = View.VISIBLE
            binding.avatar.setImageResource(it.drawableRes)
            binding.points.text = stringsManager.getString(R.string.x_points)
                .format("${points}/${it.requiredPoints}")
            binding.progress.progress = (floatPoints * 100 / it.requiredPoints).toInt()
        } ?: run {
            binding.avatarWrapper.visibility = View.GONE
            binding.next.visibility = View.GONE
            binding.points.text = stringsManager.getString(R.string.x_points).format(points)
            binding.progress.progress = 100
        }
    }

    private fun showEnterAnimation(onStart: () -> Unit, onEnd: () -> Unit) {
        val anim = AnimationUtils.loadAnimation(context, R.anim.reward_popup_enter_animation)
        anim.startOffset = 1000
        anim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) = onStart()
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) = onEnd()
        })
        startAnimation(anim)
    }

    private fun showPointIncreaseAnimation(setupConfig: SetupConfig, onEnd: () -> Unit) {
        val finalPoints = setupConfig.initialPoints + setupConfig.gainedPoints
        ValueAnimator.ofFloat(
            setupConfig.initialPoints.toFloat(),
            finalPoints.toFloat()
        ).apply {
            duration = 700
            startDelay = 200
            addUpdateListener { updateProgress(it.animatedValue as Float, finalPoints) }
            addListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) = onEnd()
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            start()
        }
    }

    private fun showExitAnimation(onEnd: () -> Unit) {
        val anim = AnimationUtils.loadAnimation(context, R.anim.reward_popup_exit_animation)
        anim.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) = onEnd()
        })
        anim.startOffset = 2000
        startAnimation(anim)
    }

    private fun announceAvatarUnlocked(initialPoints: Int, gainedPoints: Int) {
        val finalPoints = initialPoints + gainedPoints
        if (Avatar.progressList.any { it.requiredPoints == finalPoints }) {
            // make avatar have color
            binding.avatar.colorFilter = ColorMatrixColorFilter(
                ColorMatrix().apply { setSaturation(1f) }
            )
            val vibration = VibrationEffect.createOneShot(200L, 200)
            getSystemService(context, Vibrator::class.java)?.vibrate(vibration)
        }

    }

    fun show(setupConfig: SetupConfig) {
        binding.title.text = setupConfig.title
        binding.gainedPoints.text = stringsManager.getString(R.string.x_points)
            .format("+${setupConfig.gainedPoints}")

        // make avatar black and white
        binding.avatar.colorFilter = ColorMatrixColorFilter(
            ColorMatrix().apply { setSaturation(0f) }
        )

        val finalPoints = setupConfig.initialPoints + setupConfig.gainedPoints
        updateProgress(setupConfig.initialPoints.toFloat(), finalPoints)

        showEnterAnimation(onStart = { alpha = 1.0f }) {
            showPointIncreaseAnimation(setupConfig) {
                announceAvatarUnlocked(setupConfig.initialPoints, setupConfig.gainedPoints)
                showExitAnimation { alpha = 0.0f }
            }
        }
    }

}
