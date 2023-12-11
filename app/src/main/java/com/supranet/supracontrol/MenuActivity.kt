package com.supranet.supracontrol

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class MenuActivity : AppCompatActivity() {

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
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val buttonKeys = arrayOf(
                "button1_url", "button2_url", "button3_url",
                "button4_url", "button5_url", "button6_url",
                "button7_url", "button8_url", "button9_url"
            )

            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

            // Obtener el valor actual de los URL para actualizar los summary
            for (buttonKey in buttonKeys) {
                val buttonUrl = sharedPrefs.getString(buttonKey, "default_url")

                val editTextPreference = findPreference<EditTextPreference>(buttonKey)

                editTextPreference?.summary = "$buttonUrl"

                editTextPreference?.setOnPreferenceChangeListener { _, newValue ->
                    sharedPrefs.edit().putString(buttonKey, newValue.toString()).apply()
                    editTextPreference?.summary = "$newValue"

                    true
                }
            }
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

            when (preference?.key) {
                "button1_url" -> {
                    handleButtonClick("button1_url", sharedPrefs)
                }
                "button2_url" -> {
                    handleButtonClick("button2_url", sharedPrefs)
                }
                "button3_url" -> {
                    handleButtonClick("button3_url", sharedPrefs)
                }
                "button4_url" -> {
                    handleButtonClick("button4_url", sharedPrefs)
                }
                "button5_url" -> {
                    handleButtonClick("button5_url", sharedPrefs)
                }
                "button6_url" -> {
                    handleButtonClick("button6_url", sharedPrefs)
                }
                "button7_url" -> {
                    handleButtonClick("button7_url", sharedPrefs)
                }
                "button8_url" -> {
                    handleButtonClick("button8_url", sharedPrefs)
                }
                "button9_url" -> {
                    handleButtonClick("button9_url", sharedPrefs)
                }
            }

            return super.onPreferenceTreeClick(preference)
        }

        private fun handleButtonClick(buttonKey: String, sharedPrefs: SharedPreferences) {
            val editTextPreference = findPreference<EditTextPreference>(buttonKey)

            editTextPreference?.setOnPreferenceChangeListener { preference, newValue ->
                sharedPrefs.edit().putString(buttonKey, newValue.toString()).apply()
                true
            }
        }
    }
}