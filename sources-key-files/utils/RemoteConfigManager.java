package com.android.ntduc.chatgpt.utils;

import com.android.ntduc.chatgpt.data.dto.remoteconfig.ConfigCountPrompt;
import com.android.ntduc.chatgpt.data.dto.remoteconfig.IAPMessage;
import com.android.ntduc.chatgpt.data.dto.remoteconfig.SaleOffConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.util.concurrent.CallableC6633a;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.RemoteConfigComponent;
import com.google.gson.Gson;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(m52837d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\bJ\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\u000eJ\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u000eJ\u0006\u0010\u0017\u001a\u00020\u0010J\u0006\u0010\u0018\u001a\u00020\u0015J\u000e\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u001a\u001a\u00020\u001bJ\u0006\u0010\u001c\u001a\u00020\u0010J\u0006\u0010\u001d\u001a\u00020\u0010J\u0006\u0010\u001e\u001a\u00020\u001fJ\u0006\u0010 \u001a\u00020\u001fJ\u0006\u0010!\u001a\u00020\u001fJ\u0006\u0010\"\u001a\u00020\u000eJ\u0006\u0010#\u001a\u00020\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/utils/RemoteConfigManager;", "", "()V", "remoteConfig", "Lcom/google/firebase/remoteconfig/FirebaseRemoteConfig;", "fetchRemoteConfig", "", "onComplete", "Lkotlin/Function0;", "getCMP", "Lcom/android/ntduc/chatgpt/data/dto/cmp/CMPModel;", "getConfigCountPrompt", "Lcom/android/ntduc/chatgpt/data/dto/remoteconfig/ConfigCountPrompt;", "getCreditFree", "", "getGeminiKey", "Lcom/android/ntduc/chatgpt/data/dto/remoteconfig/OpenAiAcc;", "getIAPMessage", "Lcom/android/ntduc/chatgpt/data/dto/remoteconfig/IAPMessage;", "getInterSplashConfig", "getKeyHashHmacSha512", "", "getMessageEarnedInFirst5Times", "getOpenAiAcc", "getPoeKey", "getPreventInstall", "context", "Landroid/content/Context;", "getRemoteKeyGpt4", "getRemoteKeyOpenAi", "getSaleAudienceConfig", "Lcom/android/ntduc/chatgpt/data/dto/remoteconfig/SaleOffConfig;", "getSaleOffConfig", "getSaleSecondPopupConfig", "getStyleSaleOff", "getTimeoutSplash", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
public final class RemoteConfigManager {

    /* renamed from: a */
    @NotNull
    public final FirebaseRemoteConfig f5523a;

    public RemoteConfigManager() {
        FirebaseRemoteConfig m22127b = ((RemoteConfigComponent) FirebaseApp.m21013d().m21018c(RemoteConfigComponent.class)).m22127b("firebase");
        Intrinsics.checkNotNullExpressionValue(m22127b, "getInstance(...)");
        this.f5523a = m22127b;
    }

    /* renamed from: a */
    public final void m2982a(@NotNull final Function0<Unit> onComplete) {
        Intrinsics.checkNotNullParameter(onComplete, "onComplete");
        RemoteConfigManager$fetchRemoteConfig$configSettings$1 init = RemoteConfigManager$fetchRemoteConfig$configSettings$1.f5524f;
        Intrinsics.checkNotNullParameter(init, "init");
        FirebaseRemoteConfigSettings.Builder builder = new FirebaseRemoteConfigSettings.Builder();
        init.invoke(builder);
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings = new FirebaseRemoteConfigSettings(builder);
        Intrinsics.checkNotNullExpressionValue(firebaseRemoteConfigSettings, "builder.build()");
        FirebaseRemoteConfig firebaseRemoteConfig = this.f5523a;
        firebaseRemoteConfig.getClass();
        Tasks.call(firebaseRemoteConfig.f30697c, new CallableC6633a(1, firebaseRemoteConfig, firebaseRemoteConfigSettings));
        firebaseRemoteConfig.m22117a().addOnCompleteListener(new OnCompleteListener() { // from class: com.android.ntduc.chatgpt.utils.a
            @Override // com.google.android.gms.tasks.OnCompleteListener
            public final void onComplete(Task it) {
                Function0 onComplete2 = Function0.this;
                Intrinsics.checkNotNullParameter(onComplete2, "$onComplete");
                Intrinsics.checkNotNullParameter(it, "it");
                onComplete2.invoke();
            }
        });
    }

    @NotNull
    /* renamed from: b */
    public final ConfigCountPrompt m2983b() {
        String m22120d = this.f5523a.m22120d("config_count_prompt");
        Intrinsics.checkNotNullExpressionValue(m22120d, "getString(...)");
        try {
            ConfigCountPrompt configCountPrompt = (ConfigCountPrompt) new Gson().fromJson(m22120d, ConfigCountPrompt.class);
            return configCountPrompt == null ? new ConfigCountPrompt(false, false, 0, 7, null) : configCountPrompt;
        } catch (Exception e) {
            e.printStackTrace();
            return new ConfigCountPrompt(false, false, 0, 7, null);
        }
    }

    /* renamed from: c */
    public final int m2984c() {
        String m22120d = this.f5523a.m22120d("credit_free");
        Intrinsics.checkNotNullExpressionValue(m22120d, "getString(...)");
        try {
            Integer num = (Integer) new Gson().fromJson(m22120d, Integer.TYPE);
            if (num == null) {
                return 5;
            }
            return num.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 5;
        }
    }

    @NotNull
    /* renamed from: d */
    public final IAPMessage m2985d() {
        String m22120d = this.f5523a.m22120d("iap_message");
        Intrinsics.checkNotNullExpressionValue(m22120d, "getString(...)");
        try {
            IAPMessage iAPMessage = (IAPMessage) new Gson().fromJson(m22120d, IAPMessage.class);
            return iAPMessage == null ? new IAPMessage(null, null, null, 7, null) : iAPMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return new IAPMessage(null, null, null, 7, null);
        }
    }

    /* renamed from: e */
    public final int m2986e() {
        String m22120d = this.f5523a.m22120d("config_inter_splash");
        Intrinsics.checkNotNullExpressionValue(m22120d, "getString(...)");
        try {
            Integer num = (Integer) new Gson().fromJson(m22120d, Integer.TYPE);
            if (num == null) {
                return 1;
            }
            return num.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /* renamed from: f */
    public final int m2987f() {
        String m22120d = this.f5523a.m22120d("message_earned_in_first_5_times");
        Intrinsics.checkNotNullExpressionValue(m22120d, "getString(...)");
        try {
            Integer num = (Integer) new Gson().fromJson(m22120d, Integer.TYPE);
            if (num == null) {
                return 3;
            }
            return num.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 3;
        }
    }

    @NotNull
    /* renamed from: g */
    public final SaleOffConfig m2988g() {
        String m22120d = this.f5523a.m22120d("Sale_audience_config");
        Intrinsics.checkNotNullExpressionValue(m22120d, "getString(...)");
        try {
            SaleOffConfig saleOffConfig = (SaleOffConfig) new Gson().fromJson(m22120d, SaleOffConfig.class);
            return saleOffConfig == null ? new SaleOffConfig(false, 120, 0, false, false, 0, false, 0, 252, null) : saleOffConfig;
        } catch (Exception e) {
            e.printStackTrace();
            return new SaleOffConfig(false, 120, 0, false, false, 0, false, 0, 252, null);
        }
    }

    @NotNull
    /* renamed from: h */
    public final SaleOffConfig m2989h() {
        String m22120d = this.f5523a.m22120d("sale_off_config");
        Intrinsics.checkNotNullExpressionValue(m22120d, "getString(...)");
        try {
            SaleOffConfig saleOffConfig = (SaleOffConfig) new Gson().fromJson(m22120d, SaleOffConfig.class);
            return saleOffConfig == null ? new SaleOffConfig(false, 0, 0, false, false, 0, false, 0, 255, null) : saleOffConfig;
        } catch (Exception e) {
            e.printStackTrace();
            return new SaleOffConfig(false, 0, 0, false, false, 0, false, 0, 255, null);
        }
    }

    @NotNull
    /* renamed from: i */
    public final SaleOffConfig m2990i() {
        String m22120d = this.f5523a.m22120d("Sale_second_popup_config");
        Intrinsics.checkNotNullExpressionValue(m22120d, "getString(...)");
        try {
            SaleOffConfig saleOffConfig = (SaleOffConfig) new Gson().fromJson(m22120d, SaleOffConfig.class);
            return saleOffConfig == null ? new SaleOffConfig(false, 120, 0, false, false, 0, false, 0, 252, null) : saleOffConfig;
        } catch (Exception e) {
            e.printStackTrace();
            return new SaleOffConfig(false, 120, 0, false, false, 0, false, 0, 252, null);
        }
    }

    /* renamed from: j */
    public final int m2991j() {
        String m22120d = this.f5523a.m22120d("Style_sale_off");
        Intrinsics.checkNotNullExpressionValue(m22120d, "getString(...)");
        try {
            Integer num = (Integer) new Gson().fromJson(m22120d, Integer.TYPE);
            if (num == null) {
                return 3;
            }
            return num.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 3;
        }
    }
}
