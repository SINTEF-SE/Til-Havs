package no.sintef.fiskinfo.ui.sprice

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView

class DropDownMenuArrayAdapter<T : IDropDownMenu>  (context: Context, resource: Int, objects: Array<T>) :
    ArrayAdapter<T>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : TextView = super.getView(position, convertView, parent) as TextView
        view.text = getItem(position)?.getLocalizedName(context)
        return view
    }

    private val allValuesFilter = object: Filter() {
        override fun convertResultToString(resultValue: Any?): CharSequence {
            resultValue?.let {
                val code = it as T
                return code.getLocalizedName(context)
            }
            return ""
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            results.values = objects
            results.count = objects.size
            return results
        }
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return allValuesFilter
    }
}