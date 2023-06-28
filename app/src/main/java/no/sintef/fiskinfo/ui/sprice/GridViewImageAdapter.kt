package no.sintef.fiskinfo.ui.sprice

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import no.sintef.fiskinfo.R

class GridViewImageAdapter(var context: Context, var arrayListImage: ArrayList<Uri>, var name: ArrayList<String>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myView = convertView
        val holder: ViewHolder

        if (myView == null) {

            val mInflater = (context as Activity).layoutInflater
            myView = mInflater.inflate(R.layout.sprice_image_grid_view_item, parent, false)
            holder = ViewHolder()

            holder.mImageView = myView!!.findViewById(R.id.image_grid_view_item_image_view) as ImageView
            holder.mTextView = myView.findViewById(R.id.image_grid_view_item_text_view) as TextView
            myView.tag = holder
        } else {
            holder = myView.tag as ViewHolder

        }

        holder.mImageView!!.setImageURI(arrayListImage.get(position))
        holder.mTextView!!.text = name[position]

        return myView

    }

    override fun getItem(p0: Int): Any {
        return arrayListImage[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return arrayListImage.size
    }
    class ViewHolder {
        var mImageView: ImageView? = null
        var mTextView: TextView? = null

    }
}