package com.android.ntduc.chatgpt.data.remote.service;

import com.android.ntduc.chatgpt.data.dto.tracking.BodyTrackingLike;
import com.android.ntduc.chatgpt.data.dto.tracking.ResponseTrackingLike;
import com.applovin.sdk.AppLovinEventTypes;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@Metadata(m52837d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J!\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H§@ø\u0001\u0000¢\u0006\u0002\u0010\u0007J!\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\b\b\u0001\u0010\n\u001a\u00020\u000bH§@ø\u0001\u0000¢\u0006\u0002\u0010\f\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\r"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/data/remote/service/TrackingSeverErrorService;", "", "trackingLike", "Lretrofit2/Response;", "Lcom/android/ntduc/chatgpt/data/dto/tracking/ResponseTrackingLike;", "bodyTrackingLike", "Lcom/android/ntduc/chatgpt/data/dto/tracking/BodyTrackingLike;", "(Lcom/android/ntduc/chatgpt/data/dto/tracking/BodyTrackingLike;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "trackingSeverError", "Lokhttp3/ResponseBody;", AppLovinEventTypes.USER_VIEWED_CONTENT, "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
public interface TrackingSeverErrorService {
    @GET("gpt-error")
    @Nullable
    /* renamed from: c */
    Object m2716c(@NotNull @Query("content") String str, @NotNull Continuation<? super Response<ResponseBody>> continuation);

    @POST("gpt-report")
    @Nullable
    /* renamed from: e */
    Object m2717e(@Body @NotNull BodyTrackingLike bodyTrackingLike, @NotNull Continuation<? super Response<ResponseTrackingLike>> continuation);
}
