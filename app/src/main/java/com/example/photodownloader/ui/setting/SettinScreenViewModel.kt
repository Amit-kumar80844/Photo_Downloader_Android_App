package com.example.photodownloader.ui.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photodownloader.data.local.Setting
import com.example.photodownloader.domain.room.PhotoDownloaderDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// ---------------- UiState ----------------

sealed class SettingUiState {
    object Idle : SettingUiState()
    object Loading : SettingUiState()
    data class Error(val message: String) : SettingUiState()
}

// ---------------- ViewModel ----------------

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val photoDownloaderDao: PhotoDownloaderDao
) : ViewModel() {

    /** UI State for loading/error */
    private val _uiState = MutableStateFlow<SettingUiState>(SettingUiState.Loading)
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    /** Current settings in memory */
    private val _settings = MutableStateFlow(
        Setting(
            id = 1,
            safeSearch = true,
            onlyEditorsChoice = false,
            minHeight = 0,
            minWidth = 0,
            perPage = 20
        )
    )

    val settings: StateFlow<Setting> = _settings.asStateFlow()

    init {
        loadSettings()
    }

    // ----- UI State Setters -----

    fun setLoading() {
        _uiState.value = SettingUiState.Loading
    }

    fun setIdle() {
        _uiState.value = SettingUiState.Idle
    }

    fun setError(message: String) {
        _uiState.value = SettingUiState.Error(message)
    }

    // ----- Load Settings from DB -----

    private fun loadSettings() {
        viewModelScope.launch {
            setLoading()
            try {
                // 1) Try to load existing setting on IO dispatcher
                val savedSetting = withContext(Dispatchers.IO) {
                    photoDownloaderDao.getAllSettingData()
                }

                if (savedSetting != null) {
                    // Use loaded setting
                    _settings.value = savedSetting
                } else {
                    // 2) Not found → create default and save it on IO
                    val defaultSetting = Setting(
                        id = 1,
                        safeSearch = true,
                        onlyEditorsChoice = false,
                        minHeight = 0,
                        minWidth = 0,
                        perPage = 20
                    )

                    withContext(Dispatchers.IO) {
                        // Save default to DB
                        photoDownloaderDao.save(defaultSetting)
                    }

                    // 3) Re-read from DB (preferred) to make sure what we show is exactly what's stored
                    val reloaded = withContext(Dispatchers.IO) {
                        photoDownloaderDao.getAllSettingData()
                    }

                    // If for some reason reloaded is null, fall back to the default object we created
                    _settings.value = reloaded ?: defaultSetting
                }

                setIdle()
            } catch (e: Exception) {
                Log.e("Settings", "Error loading settings", e)
                setError("Failed to load settings: ${e.localizedMessage ?: e}")
            }
        }
    }


    // ----- Save Custom Setting -----

    fun saveSetting(setting: Setting) {
        viewModelScope.launch {
            try {
                setLoading()
                photoDownloaderDao.save(setting)
                _settings.value = setting
                setIdle()
            } catch (e: Exception) {
                Log.e("Settings", "Error saving setting", e)
                setError("Failed to save")
            }
        }
    }

    // ----- Reset to Default -----

    fun saveDefaultSetting() {
        val default = Setting(
            id = 1,
            safeSearch = true,
            onlyEditorsChoice = false,
            minHeight = 0,
            minWidth = 0,
            perPage = 20
        )

        viewModelScope.launch {
            try {
                setLoading()
                photoDownloaderDao.save(default)
                _settings.value = default
                setIdle()
            } catch (e: Exception) {
                Log.e("Settings", "Error resetting setting", e)
                setError("Failed to reset")
            }
        }
    }
}