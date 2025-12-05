package com.example.photodownloader.ui.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photodownloader.data.local.Setting
import com.example.photodownloader.domain.room.PhotoDownloaderDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
            try {
                setLoading()
                val savedSetting = photoDownloaderDao.getAllSettingData()

                if (savedSetting != null) {
                    _settings.value = savedSetting
                } else {
                    val defaultSetting = Setting(
                        id = 1,
                        safeSearch = true,
                        onlyEditorsChoice = false,
                        minHeight = 0,
                        minWidth = 0,
                        perPage = 20
                    )
                    photoDownloaderDao.save(defaultSetting)
                    _settings.value = defaultSetting
                }
                setIdle()
            } catch (e: Exception) {
                Log.e("Settings", "Error loading settings", e)
                setError("Failed to load settings")
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