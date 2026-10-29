package com.android.ntduc.chatgpt.data.remote.service;

import com.android.ntduc.chatgpt.data.dto.gather.BodyGather;
import com.android.ntduc.chatgpt.data.dto.gather.ResponseGather;
import com.android.ntduc.chatgpt.data.dto.tracking.ResponseTracking;
import com.unity3d.ads.metadata.MediationMetaData;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.C8386l8;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@Metadata(m52837d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J+\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH§@ø\u0001\u0000¢\u0006\u0002\u0010\tJ+\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00032\b\b\u0001\u0010\f\u001a\u00020\u00062\b\b\u0001\u0010\r\u001a\u00020\u0006H§@ø\u0001\u0000¢\u0006\u0002\u0010\u000e\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000f"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/data/remote/service/TrackingChatService;", "", "putGather", "Lretrofit2/Response;", "Lcom/android/ntduc/chatgpt/data/dto/gather/ResponseGather;", C8386l8.a.f36196e, "", "bodyGather", "Lcom/android/ntduc/chatgpt/data/dto/gather/BodyGather;", "(Ljava/lang/String;Lcom/android/ntduc/chatgpt/data/dto/gather/BodyGather;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "trackingChat", "Lcom/android/ntduc/chatgpt/data/dto/tracking/ResponseTracking;", "time", MediationMetaData.KEY_VERSION, "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
public interface TrackingChatService {
    @GET("chat/send")
    @Nullable
    /* renamed from: a */
    Object m2713a(@NotNull @Query("time") String str, @NotNull @Query("version") String str2, @NotNull Continuation<? super Response<ResponseTracking>> continuation);

    @POST("gpt-service/gather")
    @Nullable
    /* renamed from: b */
    Object m2714b(@NotNull @Query("type") String str, @Body @NotNull BodyGather bodyGather, @NotNull Continuation<? super Response<ResponseGather>> continuation);
}
