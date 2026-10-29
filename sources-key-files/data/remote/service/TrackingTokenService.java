package com.android.ntduc.chatgpt.data.remote.service;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.C8624r7;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

@Metadata(m52837d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J/\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\n\b\u0001\u0010\u0005\u001a\u0004\u0018\u00010\u00042\n\b\u0001\u0010\u0006\u001a\u0004\u0018\u00010\u0004H§@ø\u0001\u0000¢\u0006\u0002\u0010\u0007J+\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\t\u001a\u00020\u00042\b\b\u0001\u0010\n\u001a\u00020\u000bH§@ø\u0001\u0000¢\u0006\u0002\u0010\f\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\r"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/data/remote/service/TrackingTokenService;", "", "trackingError", "Lretrofit2/Response;", "", "pass", "gpt", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "trackingToken", C8624r7.h.f38041W, "value", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
public interface TrackingTokenService {
    @GET("checking/gpt/pass")
    @Nullable
    /* renamed from: a */
    Object m2718a(@Nullable @Query("pass") String str, @Nullable @Query("gpt") String str2, @NotNull Continuation<? super Response<String>> continuation);

    @GET("checking/gpt/token")
    @Nullable
    /* renamed from: b */
    Object m2719b(@NotNull @Query("key") String str, @Query("value") int i, @NotNull Continuation<? super Response<String>> continuation);
}
