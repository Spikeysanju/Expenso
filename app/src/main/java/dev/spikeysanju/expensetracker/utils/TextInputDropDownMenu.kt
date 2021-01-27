package dev.spikeysanju.expensetracker.utils

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

class TextInputDropDownMenu : AppCompatAutoCompleteTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getFreezesText(): Boolean {
        return false
    }

    override fun onSaveInstanceState(): Parcelable? {
        val parcelable = super.onSaveInstanceState()
        if (TextUtils.isEmpty(text)) {
            return parcelable
        }
        val customSavedState = CustomSavedState(parcelable)
        customSavedState.text = text.toString()
        return customSavedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is CustomSavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        setText(state.text, false)
        super.onRestoreInstanceState(state.superState)
    }

    private class CustomSavedState : BaseSavedState {
        var text: String? = null

        constructor(superState: Parcelable?) : super(superState)
        constructor(source: Parcel) : super(source) {
            text = source.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(text)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<CustomSavedState?> =
                object : Parcelable.Creator<CustomSavedState?> {
                    override fun createFromParcel(source: Parcel): CustomSavedState {
                        return CustomSavedState(source)
                    }

                    override fun newArray(size: Int): Array<CustomSavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    init {
        inputType = InputType.TYPE_NULL
    }
}
