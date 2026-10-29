package com.android.ntduc.chatgpt.data.remote.service;

import com.android.ntduc.chatgpt.data.dto.pdf.ResponseChatPDF;
import com.android.ntduc.chatgpt.data.dto.pdf.ResponseUploadFile;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.C8386l8;
import org.json.C8624r7;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

@Metadata(m52837d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J+\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH§@ø\u0001\u0000¢\u0006\u0002\u0010\tJ+\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\rH§@ø\u0001\u0000¢\u0006\u0002\u0010\u000e\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000f"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/data/remote/service/ChatPDFService;", "", "chatWithPDF", "Lretrofit2/Response;", "Lcom/android/ntduc/chatgpt/data/dto/pdf/ResponseChatPDF;", C8386l8.a.f36196e, "", "requestBody", "Lokhttp3/RequestBody;", "(Ljava/lang/String;Lokhttp3/RequestBody;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "uploadFile", "Lcom/android/ntduc/chatgpt/data/dto/pdf/ResponseUploadFile;", C8624r7.h.f38046b, "Lokhttp3/MultipartBody$Part;", "(Ljava/lang/String;Lokhttp3/MultipartBody$Part;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
public interface ChatPDFService {
    @POST("{version}/chatpdf")
    @Nullable
    /* renamed from: a */
    Object m2707a(@Path(encoded = true, value = "version") @NotNull String str, @Body @NotNull RequestBody requestBody, @NotNull Continuation<? super Response<ResponseChatPDF>> continuation);

    @POST("{version}/file/upload")
    @Nullable
    @Multipart
    /* renamed from: b */
    Object m2708b(@Path(encoded = true, value = "version") @NotNull String str, @NotNull @Part MultipartBody.Part part, @NotNull Continuation<? super Response<ResponseUploadFile>> continuation);
}
