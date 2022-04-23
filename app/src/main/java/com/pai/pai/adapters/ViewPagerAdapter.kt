package com.pai.pai.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pai.pai.HomeFragment
import com.pai.pai.ProfileFragment
import com.pai.pai.RegisterFragment
import com.pai.pai.fragments.ContactosFragment
import com.pai.pai.fragments.GruposFragment

//Extiende de FragmentStateAdapter

class ViewPagerAdapater(fragment: FragmentActivity) : FragmentStateAdapter(fragment)  {

    //Constante a nivel de clase
    companion object{
        private  const val ARG_OBJECT = "object"
    }

    //cuantos fragments va a tener el swipe
    override fun getItemCount(): Int  = 3

    override fun createFragment(position: Int): Fragment {
        //Vamos a crear el fragmente
        lateinit var fragment: Fragment

        when(position){
            0 -> {
                fragment = GruposFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt(ARG_OBJECT, position + 1)
                }
            }
            1 -> {
                fragment = ContactosFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt(ARG_OBJECT, position + 1)
                }
            }
            2 -> {
                fragment = RegisterFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt(ARG_OBJECT, position + 1)
                }
            }
        }

        return fragment
    }

}