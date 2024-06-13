package org.godotengine.plugin.android.template;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.InitializationListener;
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class GodotAndroidPlugin extends GodotPlugin {

    private int instance_id = -1;
    private String godotMethodName = "";
    public boolean Initialize = false;
    private boolean InterstitialAvailable = IronSource.isInterstitialReady();
    private boolean RewardVideoAvailable = IronSource.isRewardedVideoAvailable();

    public static String Signal_SetInterstitialAvailable = "SetInterstitialAvailable";
    public static String Signal_Interstitial_onAdClosed = "Interstitial_onAdClosed";
    public static String Signal_GetConsentSettings = "GetConsentSettings";

    @Override
    public Set<SignalInfo> getPluginSignals() {
        Set<SignalInfo> signals = new HashSet<>();
        signals.add(new SignalInfo(Signal_SetInterstitialAvailable, Boolean.class));
        signals.add(new SignalInfo(Signal_Interstitial_onAdClosed));
        signals.add(new SignalInfo(Signal_GetConsentSettings, Boolean.class, Boolean.class));
        return signals;
    }

    @Nullable
    @Override
    public View onMainCreate(Activity activity) {
        return super.onMainCreate(activity);
    }

    @Override
    public void onMainResume() {
        super.onMainResume();
        IronSource.onResume(getActivity());
    }

    @Override
    public void onMainPause() {
        super.onMainPause();
        IronSource.onPause(getActivity());
    }

    public GodotAndroidPlugin(Godot godot) {
        super(godot);
    }

    @Override
    public String getPluginName() {
        return BuildConfig.GODOT_PLUGIN_NAME;
    }

    @UsedByGodot
    private void InitIronSource(int instance_id, String APP_KEY) {
        if (!Initialize) {
            this.instance_id = instance_id;
            IronSource.shouldTrackNetworkState(getActivity(), true);
            runOnUiThread(() -> IronSource.init(getActivity(), APP_KEY, () -> {
                Initialize = true;
                InitializeRewardVideoListener();
                InitializeInterstitialListener();
                LoadInterstitial();
                LoadRewardVideo();
                ShowToast("Iron source Initialized");
            }));
        } else {
            ShowToast("Iron source is ready", Toast.LENGTH_SHORT);
        }
    }

    @UsedByGodot
    private void ShowInterstitial() {
        if (InterstitialAvailable) {
            ShowToast("Show Interstitial here");
            IronSource.showInterstitial();
        } else {
            ShowToast("Interstitial not available");
            LoadInterstitial();
        }
    }

    @UsedByGodot
    private void ShowRewardVideo(String signal) {
        if (IronSource.isRewardedVideoAvailable()) {
            this.godotMethodName = signal;
            IronSource.showRewardedVideo();
        }
    }

    @UsedByGodot
    private void RefreshInterstitialReady() {
        emitSignal(Signal_SetInterstitialAvailable, IronSource.isInterstitialReady());
    }

    private void InitializeRewardVideoListener() {
        IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
            @Override
            public void onAdAvailable(AdInfo adInfo) {
                ShowToast("Reward video ready");
            }

            @Override
            public void onAdUnavailable() {
            }

            @Override
            public void onAdOpened(AdInfo adInfo) {
            }

            @Override
            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
            }

            @Override
            public void onAdClicked(Placement placement, AdInfo adInfo) {
            }

            @Override
            public void onAdRewarded(Placement placement, AdInfo adInfo) {
            }

            @Override
            public void onAdClosed(AdInfo adInfo) {
                ShowToast("Android: Reward closed");
            }
        });
    }

    private void InitializeInterstitialListener() {
        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
            @Override
            public void onAdReady(AdInfo adInfo) {
                ShowToast("Interstitial is Ready");
                InterstitialAvailable = true;
                emitSignal(Signal_SetInterstitialAvailable, Boolean.TRUE);
            }

            @Override
            public void onAdLoadFailed(IronSourceError ironSourceError) {
                emitSignal(Signal_SetInterstitialAvailable, Boolean.FALSE);
            }

            @Override
            public void onAdOpened(AdInfo adInfo) {
            }

            @Override
            public void onAdShowSucceeded(AdInfo adInfo) {
            }

            @Override
            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
            }

            @Override
            public void onAdClicked(AdInfo adInfo) {
            }

            @Override
            public void onAdClosed(AdInfo adInfo) {
                InterstitialAvailable = false;
                emitSignal(Signal_SetInterstitialAvailable, Boolean.FALSE);
                emitSignal(Signal_Interstitial_onAdClosed);
                LoadInterstitial();
            }
        });
    }

    private void LoadRewardVideo() {
        if (!RewardVideoAvailable)
            IronSource.loadRewardedVideo();
    }

    private void LoadInterstitial() {
        if (!InterstitialAvailable)
            IronSource.loadInterstitial();
    }

    @UsedByGodot
    private void helloWorld() {
        ShowToast("Hello World !!!", Toast.LENGTH_SHORT);
    }

    private void ShowToast(String message, int duration) {
        runOnUiThread(() -> {
            Toast.makeText(getActivity(), message, duration).show();
            Log.v(getPluginName(), message);
        });
    }

    @UsedByGodot
    private void ShowToast(String message) {
        ShowToast(message, Toast.LENGTH_SHORT);
    }

    @UsedByGodot
    private void SetConsentGDPR(boolean consent) {
        IronSource.setConsent(consent);
    }

    @UsedByGodot
    private void SubmitConsent(int userAge)
    {
        SetChildDirect(userAge);
    }

    @UsedByGodot
    private void SetChildDirect(int age) {
        String country = CountryUtils.getSimCountryIso(getActivity());

        if (CountryUtils.isCoppaCountry(country) && age < 13) {
            IronSource.setMetaData("is_child_directed", "true");
        } else {
            IronSource.setMetaData("is_child_directed", "false");
        }

        if (CountryUtils.isEuropeanCountry(country)) {
            SetConsentGDPR(age >= 16);
        }
    }

    @UsedByGodot
    private void GatherConsentSettings() {
        String deviceCountry = CountryUtils.getDeviceCountry();
        boolean isGDPR = CountryUtils.isEuropeanCountry(deviceCountry);
        boolean isCOPPA = CountryUtils.isCoppaCountry(deviceCountry);
        emitSignal(Signal_GetConsentSettings, isGDPR, isCOPPA);
    }
}