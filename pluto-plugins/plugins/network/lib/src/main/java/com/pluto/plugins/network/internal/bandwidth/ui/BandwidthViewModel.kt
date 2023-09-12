package com.pluto.plugins.network.internal.bandwidth.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluto.utilities.settings.SettingsPreferences

class BandwidthViewModel : ViewModel() {

    val dnsResolutionTimeout: MutableLiveData<Long> = MutableLiveData(SettingsPreferences.bandWidthDnsResolutionDelay)
    val uploadSpeedInMbps: MutableLiveData<Long> = MutableLiveData(SettingsPreferences.bandWidthLimitUploadMbps)
    val downloadSpeedInMbps: MutableLiveData<Long> = MutableLiveData(SettingsPreferences.bandWidthLimitDownloadMbps)
    val isBandwidthLimitEnabled: MutableLiveData<Boolean> = MutableLiveData(SettingsPreferences.isBandwidthLimitEnabled)

    fun areValuesChanged(): Boolean {
        return SettingsPreferences.bandWidthDnsResolutionDelay != dnsResolutionTimeout.value ||
            SettingsPreferences.bandWidthLimitUploadMbps != uploadSpeedInMbps.value ||
            SettingsPreferences.bandWidthLimitDownloadMbps != downloadSpeedInMbps.value ||
            SettingsPreferences.isBandwidthLimitEnabled != isBandwidthLimitEnabled.value
    }

    fun reset() {
        uploadSpeedInMbps.postValue(SettingsPreferences.bandWidthLimitUploadMbps)
        downloadSpeedInMbps.postValue(SettingsPreferences.bandWidthLimitDownloadMbps)
        dnsResolutionTimeout.postValue(SettingsPreferences.bandWidthDnsResolutionDelay)
    }
}
