// TODO: Update to match your plugin's package name.
package org.godotengine.plugin.android.template;

import android.util.ArraySet;
import android.util.Log;
import android.widget.Toast;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.InitializationListener;
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;

import org.godotengine.godot.Godot;
import org.godotengine.godot.GodotActivity;
import org.godotengine.godot.GodotHost;
import org.godotengine.godot.GodotLib;
import org.godotengine.godot.gl.GodotRenderer;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import java.util.HashSet;
import java.util.Set;

public class GodotAndroidPlugin extends GodotPlugin {

    private int instance_id = -1;
    private String godotMethodName = "";
    public boolean Initialize=false;

    private boolean InterstitialAvailable=IronSource.isInterstitialReady();
    private boolean RewardVideoAvailable=IronSource.isRewardedVideoAvailable();

    @Override
    public Set<SignalInfo> getPluginSignals()
    {
        Set<SignalInfo> signals = null;
        signals = new HashSet<>();
        signals.add(new SignalInfo("testSignal", String.class));
        signals.add(new SignalInfo("CloseReward", String.class));

        signals.add(new SignalInfo("PauseGodot"));
        
        return signals;
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
    private void InitIronSource(int instance_id, String APP_KEY)
    {
        if(Initialize==false) {
            this.instance_id=instance_id;
            IronSource.shouldTrackNetworkState(getActivity(),true);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    IronSource.init(getActivity(), APP_KEY, new InitializationListener() {
                        @Override
                        public void onInitializationComplete() {
                            // ironSource SDK is initialized
                            Initialize = true;
                            InitializeRewardVideoListener();
                            InitializeInterstitialListener();
                            LoadInterstitial();
                            LoadRewardVideo();
                            //IntegrationHelper.validateIntegration(activity);
                            ShowToast("Iron source Initialized");
                        }
                    });
                }
            });
        }
        else
        {
            ShowToast("Iron source is ready", Toast.LENGTH_SHORT);
        }
    }
    @UsedByGodot
    private void ShowInterstitial()
    {
        if(InterstitialAvailable)
        {
            ShowToast("Show Interstitial here");
            IronSource.showInterstitial();
        }
        else
        {
            ShowToast("Interstitial not available");
            LoadInterstitial();
        }
    }

    @UsedByGodot
    private void ShowRewardVideo(String signal)
    {
        if(IronSource.isRewardedVideoAvailable())
        {
            this.godotMethodName=signal;
            IronSource.showRewardedVideo();
        }
    }

    private void InitializeRewardVideoListener()
    {
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
                emitSignal("testSignal", "Finally reward claimed");
            }
        });
    }

    private void InitializeInterstitialListener()
    {
        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
            @Override
            public void onAdReady(AdInfo adInfo) {
                ShowToast("Interstitial is Ready");
                InterstitialAvailable =true;
            }

            @Override
            public void onAdLoadFailed(IronSourceError ironSourceError) {

            }

            @Override
            public void onAdOpened(AdInfo adInfo) {
                ShowToast("Here the game should be paused");
                //Just send a signal
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
                InterstitialAvailable=false;
                LoadInterstitial();
                //getGodot().getRenderView().onActivityResumed();
            }
        });
    }

    private void LoadRewardVideo()
    {
        if(RewardVideoAvailable==false)
            IronSource.loadRewardedVideo();
    }
    private void LoadInterstitial()
    {
        if(InterstitialAvailable==false)
            IronSource.loadInterstitial();
    }

    /**
     * Example showing how to declare a method that's used by Godot.
     *
     * Shows a 'Hello World' toast.
     */
    @UsedByGodot
    private void helloWorld() {
        ShowToast("Hello World !!!", Toast.LENGTH_SHORT);
    }

    private void ShowToast(String message, int duration )
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), message, duration).show();
                Log.v(getPluginName(), message);
            }
        });
    }
    @UsedByGodot
    private void ShowToast(String message)
    {
        ShowToast(message, Toast.LENGTH_SHORT);
    }
}