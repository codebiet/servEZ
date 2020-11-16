package com.example.services

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_messaging.*

class Messaging_fragment : Fragment() {

    companion object {
        fun newInstance() : Messaging_fragment = Messaging_fragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_messaging,container,false)
        return view ;
    }

    val adaptor = GroupAdapter<ViewHolder>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Logs","Msg called")
        recyclerview_latest_msg.adapter = adaptor
        setDummy()
    }

    private fun setDummy(){
        adaptor.add(LatestMsg())
        adaptor.add(LatestMsg())
        adaptor.add(LatestMsg())
        adaptor.add(LatestMsg())

    }

    class LatestMsg(): Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {

        }

        override fun getLayout(): Int {
            return R.layout.latest_msg_tile
        }
    }

}