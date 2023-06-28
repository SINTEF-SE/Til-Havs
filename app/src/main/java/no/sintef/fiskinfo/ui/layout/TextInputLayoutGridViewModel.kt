package no.sintef.fiskinfo.ui.layout

import android.app.ActionBar
import android.text.InputType
import android.text.TextWatcher
import android.view.View.TEXT_ALIGNMENT_VIEW_END
import android.view.ViewGroup
import android.widget.AdapterView
import no.sintef.fiskinfo.ui.sprice.DropDownMenuArrayAdapter
import no.sintef.fiskinfo.ui.sprice.IDropDownMenu

class TextInputLayoutGridViewModel<T : IDropDownMenu>() {
    lateinit var fieldName: String
    lateinit var hint: String
    var textAlignment: Int = TEXT_ALIGNMENT_VIEW_END
    var suffixText: String? = null
    var selectAllOnFocus: Boolean = true
    var maxLines: Int = 3
    var inputType: Int = InputType.TYPE_CLASS_NUMBER
    var onclickListener: AdapterView.OnItemClickListener? = null
    var dropDownAdapter: DropDownMenuArrayAdapter<T>? = null
    var textChangedListener: TextWatcher? = null
    var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT

    constructor(fieldName: String, hint: String) : this() {
        this.fieldName = fieldName
        this.hint = hint
    }

    constructor(
        fieldName: String,
        hint: String,
        textAlignment: Int = TEXT_ALIGNMENT_VIEW_END,
        suffixText: String? = null,
        selectAllOnFocus: Boolean = true,
        maxLines: Int = 3,
        onClickListener: AdapterView.OnItemClickListener? = null,
        dropDownAdapter: DropDownMenuArrayAdapter<T>? = null,
        inputType: Int = if(onClickListener == null) InputType.TYPE_CLASS_NUMBER else InputType.TYPE_NULL,
        textChangedListener: TextWatcher? = null
    )
            : this(fieldName, hint) {
        this.fieldName = fieldName
        this.hint = hint
        this.textAlignment = textAlignment
        this.suffixText = suffixText
        this.selectAllOnFocus = selectAllOnFocus
        this.maxLines = maxLines
        this.inputType = inputType
        this.onclickListener = onClickListener
        this.dropDownAdapter = dropDownAdapter
        this.textChangedListener = textChangedListener
    }
}