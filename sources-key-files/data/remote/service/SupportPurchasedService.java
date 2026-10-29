package com.android.ntduc.chatgpt.data.remote.service;

import com.android.ntduc.chatgpt.data.dto.support.ResponseHistorySupport;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Metadata(m52837d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J!\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H§@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\b"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/data/remote/service/SupportPurchasedService;", "", "getHistorySupport", "Lretrofit2/Response;", "Lcom/android/ntduc/chatgpt/data/dto/support/ResponseHistorySupport;", "token", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
public interface SupportPurchasedService {
    @GET("gpt-service/message/{token}")
    @Nullable
    /* renamed from: a */
    Object m2712a(@Path("token") @NotNull String str, @NotNull Continuation<? super Response<ResponseHistorySupport>> continuation);
}
