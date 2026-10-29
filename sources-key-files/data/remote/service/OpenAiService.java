package com.android.ntduc.chatgpt.data.remote.service;

import com.android.ntduc.chatgpt.data.dto.chat.ResponseModerations;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

@Metadata(m52837d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J+\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH§@ø\u0001\u0000¢\u0006\u0002\u0010\tJ\"\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH'\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\r"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/data/remote/service/OpenAiService;", "", "checkModeration", "Lretrofit2/Response;", "Lcom/android/ntduc/chatgpt/data/dto/chat/ResponseModerations;", "authHeader", "", "requestBody", "Lokhttp3/RequestBody;", "(Ljava/lang/String;Lokhttp3/RequestBody;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "postChatTurbo", "Lretrofit2/Call;", "Lokhttp3/ResponseBody;", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
public interface OpenAiService {
    @POST("moderations")
    @Nullable
    /* renamed from: a */
    Object m2711a(@Header("Authorization") @NotNull String str, @Body @NotNull RequestBody requestBody, @NotNull Continuation<? super Response<ResponseModerations>> continuation);
}
