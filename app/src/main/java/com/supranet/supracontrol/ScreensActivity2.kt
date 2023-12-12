package com.supranet.supracontrol

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class ScreensActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.screen_preferences, rootKey)
            val buttonKeys = arrayOf(
                "screen1_ip", "screen2_ip", "screen3_ip",
                "screen4_ip", "screen5_ip", "screen6_ip",
                "screen7_ip", "screen8_ip", "screen9_ip"
            )

            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(requireContext())

            for (buttonKey in buttonKeys) {
                val ipAddress = sharedPreferences.getString(buttonKey, "")

                val editTextPreference = findPreference<EditTextPreference>(buttonKey)

                editTextPreference?.summary = ipAddress

                editTextPreference?.setOnPreferenceChangeListener { _, newValue ->
                    sharedPreferences.edit().putString(buttonKey, newValue.toString()).apply()
                    editTextPreference?.summary = newValue.toString()

                    true
                }
            }
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

            when (preference?.key) {
                "screen1_ip", "screen2_ip", "screen3_ip", "screen4_ip", "screen5_ip",
                "screen6_ip", "screen7_ip", "screen8_ip", "screen9_ip" -> {
                    handleButtonClick(preference.key, sharedPrefs)
                }
            }

            return super.onPreferenceTreeClick(preference)
        }

        private fun handleButtonClick(buttonKey: String, sharedPreferences: SharedPreferences) {
            val editTextPreference = findPreference<EditTextPreference>(buttonKey)

            editTextPreference?.setOnPreferenceChangeListener { _, newValue ->
                sharedPreferences.edit().putString(buttonKey, newValue.toString()).apply()
                true
            }
        }
    }
}