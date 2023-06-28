/**
 * Copyright (C) 2020 SINTEF
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.sintef.fiskinfo.ui.tools

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import no.sintef.fiskinfo.R
import no.sintef.fiskinfo.model.fishingfacility.ResponseStatus
import no.sintef.fiskinfo.model.fishingfacility.ToolTypeCode
import no.sintef.fiskinfo.model.fishingfacility.ToolViewModel
import no.sintef.fiskinfo.util.formatLocation
import no.sintef.fiskinfo.util.isToolOld
import java.text.SimpleDateFormat
import java.util.*


// TODO: Add attribution
// Icons made by Freepik from www.flaticon.com

/**
 * [RecyclerView.Adapter] that can display a [FishingFacilityChange] and makes call to the
 * specified {@OnToolInteractionListener} callbacks on interaction
 */
class ToolsRecyclerViewAdapter(private val mListener: OnToolInteractionListener?, private val mConfirmed : Boolean) :
    RecyclerView.Adapter<ToolsRecyclerViewAdapter.ViewHolder>() {

    private var tools: List<ToolViewModel>? = null

    init {
        tools = ArrayList()
    }

    fun setTools(tools: List<ToolViewModel>) {
        this.tools = tools
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tool_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (tools == null)
            return
        val tool = tools!![position]
        val context = holder.mView.context

        if (mConfirmed) {
            val bkCol = if (isToolOld(tool, context)) R.color.colorBarentsOrange else R.color.colorBarentsLightBlue
            holder.removeButton.setBackgroundColor(ContextCompat.getColor(context, bkCol))
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        val title = if(tool.lastChangedDateTime != null) tool.lastChangedDateTime?.let { sdf.format(it) } else tool.responseStatus.toString() // TODO: Check if this should be setupDateTime and check for null
        holder.titleView.text = title

        val textStyle =  Typeface.BOLD//if (holder.mItem!!.seen) Typeface.NORMAL else Typeface.BOLD
        holder.titleView.setTypeface(null, textStyle)
        //holder.detail1View.setTypeface(null, textStyle)

        when (tool.toolTypeCode) {
            ToolTypeCode.CRABPOT -> holder.iconView.setBackgroundResource(R.drawable.ic_crabpot)
            ToolTypeCode.DANPURSEINE -> holder.iconView.setBackgroundResource(R.drawable.ic_purseseine)
            ToolTypeCode.LONGLINE -> holder.iconView.setBackgroundResource(R.drawable.ic_hook)
            ToolTypeCode.NETS -> holder.iconView.setBackgroundResource(R.drawable.ic_net)
            else -> { holder.iconView.setBackgroundResource(R.drawable.ic_help) }
        }

        holder.detail1View.text = tool.toolTypeCode?.getLocalizedName(context)

        var locationString = ""
        if (tool.getLocations().isNotEmpty())
            locationString = formatLocation(tool.getLocations()[0], context)

/*        for (loc in tool.getLocations()) {
            locationString += formatLocation(loc, holder.mView.context) + ", "
        }*/

        holder.detail2View.text = locationString //tool.geometryWKT
//            sdf.format(holder.mItem!!.snapMetadata!!.timestamp) //holder.mItem.getEchogramInfo().latitude);
        //holder.shareButton.setOnClickListener({mListener?.onViewSnapInMapClicked(it, holder.mItem)})

        holder.statusButton.visibility = if (mConfirmed) View.GONE else View.VISIBLE
        holder.removeButton.visibility = if (mConfirmed) View.VISIBLE else View.GONE


        if (mConfirmed) {
            holder.removeButton.setOnClickListener({mListener?.onRemoveToolClicked(it, tools!![holder.adapterPosition])})
        } else {
            //holder.statusButton.setOnClickListener({mListener?.onToolStatusClicked(it, tools!![holder.adapterPosition])})
            //val responseStatus = ResponseStatus.values().random() // For testing
            val responseStatus = tool.responseStatus

            val statusIcon = when (responseStatus) {
                ResponseStatus.RESPONSE_APPROVED -> R.drawable.ic_baseline_check_circle_outline_24
                ResponseStatus.RESPONSE_REJECTED -> R.drawable.ic_baseline_error_outline_24
                ResponseStatus.NO_RESPONSE -> R.drawable.ic_baseline_hourglass_top_24
                else -> R.drawable.ic_baseline_help_outline_24
            }
            holder.statusButton.setBackgroundResource(statusIcon)
        }

        holder.mView.setOnClickListener { v ->
            //holder.mItem?.seen = true
            mListener?.onViewToolClicked(v, tools!![holder.adapterPosition] )
        }
    }

    override fun getItemCount(): Int {
        return tools!!.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val iconView: ImageView
        val titleView: TextView
        val detail1View: TextView
        val detail2View: TextView
        val statusButton: ImageView
        val removeButton: ImageButton

        init {
            iconView = mView.findViewById<View>(R.id.tool_item_image_view) as ImageView
            titleView = mView.findViewById<View>(R.id.tool_item_title_view) as TextView
            detail1View = mView.findViewById<View>(R.id.tool_item_detail_1_view) as TextView
            detail2View = mView.findViewById<View>(R.id.tool_item_detail_2_view) as TextView
            statusButton = mView.findViewById<View>(R.id.tool_status_button) as ImageView
            removeButton = mView.findViewById<View>(R.id.tool_remove_button) as ImageButton
        }

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }

    interface OnToolInteractionListener {
        fun onViewToolClicked(v: View, tool: ToolViewModel?)
        fun onRemoveToolClicked(v: View, tool: ToolViewModel?)
        fun onToolStatusClicked(v: View, tool: ToolViewModel?)
        //fun onViewSnapInMapClicked(v: View, snap: SnapMessage?)
    }
}
