package br.com.unip.aps.comunidadeambientalurbana.mainActivityFragments.preferences

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.unip.aps.comunidadeambientalurbana.R

class PreferencesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preferences, container, false)

    }

    override fun onStart() {
        super.onStart()

        val prefs = activity?.getSharedPreferences("appConfig", Context.MODE_PRIVATE)
        val editName = activity?.findViewById<TextView>(R.id.editNameShow)
        editName?.text = prefs!!.getString("user-name", "")
        editName?.addTextChangedListener(object:TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    prefs.edit().putString("user-name", s.toString())?.apply()
            }
        })
        val darkThemeSwitch = activity?.findViewById<Switch>(R.id.changeTheme)
        darkThemeSwitch?.isChecked = prefs!!.getBoolean("darkTheme", false)
        darkThemeSwitch?.setOnCheckedChangeListener { _, _ ->
            prefs?.edit()?.putBoolean("darkTheme", darkThemeSwitch.isChecked)?.apply()
            activity?.recreate()
        }
    }
}