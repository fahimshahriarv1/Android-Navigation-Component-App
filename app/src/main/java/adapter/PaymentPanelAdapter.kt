package adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fahim.navapp2.R
import data.Data

class PaymentPanelAdapter(val context: Context, val listOfInfo: List<Data>?) :
    RecyclerView.Adapter<PaymentPanelAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.payment_history_view,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int =listOfInfo!!.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val paymentMode= itemView.findViewById<TextView>(R.id.tvPaymentMode)
        private val depositDate= itemView.findViewById<TextView>(R.id.tvDepositDate)
        private val ref= itemView.findViewById<TextView>(R.id.tvRef)
        private val currentStatus= itemView.findViewById<TextView>(R.id.tvStatus)
        private val amount= itemView.findViewById<TextView>(R.id.tvBDT)
        private val cardView=itemView.findViewById<CardView>(R.id.cvMainView)
        fun bind(position: Int)
        {
            paymentMode.text= listOfInfo!![position].paymentStatus
            depositDate.text="Date: " + listOfInfo!![position].depositDate
            ref.text="Ref: "+ listOfInfo!![position].reference
            currentStatus.text= listOfInfo!![position].paymentStatus
            amount.text="BDT " + listOfInfo!![position].amount.toString()+" "
            cardView.setOnClickListener { Log.i("RvAdapterClass", "Clicked at: $position") }

        }

    }


}
