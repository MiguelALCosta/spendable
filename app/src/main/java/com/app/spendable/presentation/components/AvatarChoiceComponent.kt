package com.app.spendable.presentation.components

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.spendable.R
import com.app.spendable.databinding.ComponentAvatarChoiceBinding
import com.app.spendable.domain.Avatar
import com.app.spendable.utils.IStringsManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AvatarChoiceComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    @Inject
    lateinit var stringsManager: IStringsManager

    private var binding: ComponentAvatarChoiceBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentAvatarChoiceBinding.inflate(inflater, this, true)
    }

    data class SetupConfig(
        val avatar: Avatar,
        val userPoints: Int,
        val selected: Boolean
    )

    fun setup(setupConfig: SetupConfig) {
        val unlocked = setupConfig.avatar.requiredPoints <= setupConfig.userPoints

        val avatarSaturation = if (unlocked) 1f else 0f
        binding.avatar.setImageResource(setupConfig.avatar.drawableRes)
        binding.avatar.colorFilter = ColorMatrixColorFilter(
            ColorMatrix().apply { setSaturation(avatarSaturation) }
        )

        binding.label.text = when {
            setupConfig.selected -> stringsManager.getString(R.string.current_avatar)
            unlocked -> stringsManager.getString(R.string.unlocked_avatar)
            else -> stringsManager.getString(R.string.x_pts)
                .format(setupConfig.avatar.requiredPoints)
        }

        isSelected = setupConfig.selected
        isEnabled = unlocked
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        binding.wrapper.isSelected = selected
        binding.label.isSelected = selected
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.label.isEnabled = enabled
    }

}
