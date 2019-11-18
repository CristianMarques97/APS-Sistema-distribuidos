package br.com.unip.aps.comunidadeambientalurbana.mainActivityFragments.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.unip.aps.comunidadeambientalurbana.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        val prefs = activity?.getSharedPreferences("appConfig", Context.MODE_PRIVATE)
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)
            ?.menu?.findItem(R.id.navigation_home)?.isEnabled = false

        activity?.findViewById<TextView>(R.id.welcome)?.text =
            "${getText(R.string.welcome)} ${if (prefs?.getString(
                    "user-name",
                    getText(R.string.no_user_name) as String?
                ) != ""
            ) {
                prefs?.getString(
                    "user-name",
                    getText(R.string.no_user_name) as String?
                )
            } else {
                getText(R.string.no_user_name)
            }}"

    }

    override fun onStop() {
        super.onStop()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)
            ?.menu?.findItem(R.id.navigation_home)?.isEnabled = true
    }
}