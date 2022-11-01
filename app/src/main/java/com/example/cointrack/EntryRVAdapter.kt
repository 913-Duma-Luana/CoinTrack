package com.example.cointrack

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class EntryRVAdapter(val context: Context,
                     val entryClickInterface: EntryClickInterface,
                     val entryLongClickInterface: EntryLongClickInterface)
    : RecyclerView.Adapter<EntryRVAdapter.ViewHolder>() {

        private val allEntries = ArrayList<Entry>()

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val entryTypeOption = itemView.findViewById<ImageView>(R.id.EntryTypeOption)
            val entryTitle = itemView.findViewById<TextView>(R.id.EntryTitle)
            val entrySum = itemView.findViewById<TextView>(R.id.EntrySum)
            val entryDate = itemView.findViewById<TextView>(R.id.Date)
            val entryCategory = itemView.findViewById<TextView>(R.id.Category)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.entry_rv_item, parent, false);
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.entryTypeOption.setImageResource(R.drawable.ic_add_circle)
        holder.entryTypeOption.setColorFilter(this.context.getResources().getColor(R.color.teal_200));

        if(allEntries.get(position).type.equals("expense")){
            holder.entryTypeOption.setImageResource(R.drawable.ic_remove_circle)
            holder.entryTypeOption.setColorFilter(this.context.getResources().getColor(R.color.pink));
        }
        holder.entryTitle.setText(allEntries.get(position).name)
        holder.entrySum.setText(allEntries.get(position).sum.toString())
        holder.entryDate.setText(allEntries.get(position).date)
        holder.entryCategory.setText(allEntries.get(position).category)
        holder.itemView.setOnClickListener{
            entryClickInterface.onEntryClick(allEntries.get(position))
        }
        holder.itemView.setOnLongClickListener{
            val builder = AlertDialog.Builder(this.context, R.style.MyDialogTheme)
            builder.setCancelable(true);
            builder.setTitle("ENTRY REMOVAL");
            builder.setMessage("Do you really want to delete the selected entry?");

            builder.setPositiveButton(android.R.string.yes)
            { dialog, which -> entryLongClickInterface.onEntryLongClick(allEntries.get(position)) }

            builder.setNegativeButton(android.R.string.no, null)

            val dialog = builder.create();
            dialog.show()

            val btnPositive: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnNegative: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 10f
            btnPositive.layoutParams = layoutParams
            btnNegative.layoutParams = layoutParams

            true
        }
    }

    override fun getItemCount(): Int {
        return allEntries.size
    }

    fun updateList(newList: List<Entry>){
        allEntries.clear()
        allEntries.addAll(newList)
        notifyDataSetChanged()
    }
}


interface EntryLongClickInterface{
    fun onEntryLongClick(entry: Entry)
}

interface EntryClickInterface{
    fun onEntryClick(entry: Entry)
}