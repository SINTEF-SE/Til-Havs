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

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import no.sintef.fiskinfo.databinding.LocationListItemBinding
import no.sintef.fiskinfo.util.formatLocation

class LocationRecyclerViewAdapter(private val mListener: OnLocationInteractionListener?) : RecyclerView.Adapter<LocationRecyclerViewAdapter.ViewHolder>() {

    var locations =  listOf<Location>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = locations[position]
        holder.bind(item)
        holder.binding.root
            .setOnClickListener { v ->
            mListener?.onEditLocationClicked(v, holder.adapterPosition )
        }
    }

    class ViewHolder private constructor(val binding : LocationListItemBinding ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Location) {
            binding.locationItemTextView.text = formatLocation(item, binding.root.context)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LocationListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    interface OnLocationInteractionListener {
        fun onEditLocationClicked(v: View, itemClicked : Int)
    }


}