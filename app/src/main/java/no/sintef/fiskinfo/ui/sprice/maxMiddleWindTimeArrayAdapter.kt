package no.sintef.fiskinfo.ui.sprice

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import no.sintef.fiskinfo.model.sprice.MaxMiddleWindTimeEnum

class maxMiddleWindTimeArrayAdapter (context: Context, resource: Int, objects: Array<MaxMiddleWindTimeEnum>) :
    ArrayAdapter<MaxMiddleWindTimeEnum>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val theView : TextView = super.getView(position, convertView, parent) as TextView
        theView.text = getItem(position)?.getLocalizedName(context)
        return theView
    }

    private val allWindTimesFilter = object: Filter() {
        override fun convertResultToString(resultValue: Any?): CharSequence {
            resultValue?.let {
                var code = it as MaxMiddleWindTimeEnum
                return code.getLocalizedName(context!!)
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
        return allWindTimesFilter
    }
}