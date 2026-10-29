package com.android.ntduc.chatgpt.data;

import android.util.Base64;
import com.android.ntduc.chatgpt.data.dto.gather.BodyGather;
import com.android.ntduc.chatgpt.data.dto.now_ai.NowAIResponse;
import com.android.ntduc.chatgpt.data.dto.now_ai.NowAiRequest;
import com.android.ntduc.chatgpt.data.dto.tracking.BodyTrackingLike;
import com.android.ntduc.chatgpt.data.local.LocalData;
import com.android.ntduc.chatgpt.data.remote.RemoteData;
import com.android.ntduc.chatgpt.utils.RemoteConfigManager;
import com.applovin.sdk.AppLovinEventTypes;
import com.appsflyer.AppsFlyerProperties;
import com.google.gson.Gson;
import com.unity3d.ads.metadata.MediationMetaData;
import io.ktor.client.HttpClient;
import io.ktor.utils.p299io.ByteReadChannel;
import java.io.File;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Charsets;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.serialization.json.Json;
import okhttp3.internal._RequestBodyCommonKt$commonToRequestBody$1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.C8386l8;
import org.json.C8624r7;

@Metadata(m52837d1 = {"\u0000ò\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ'\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0013J-\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00150\u000e2\u0006\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u0019H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u001aJ-\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\u00150\u000e2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u001eJ%\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020 0\u00150\u000e2\u0006\u0010!\u001a\u00020\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\"J#\u0010#\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0$0\u00150\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J#\u0010'\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020(0$0\u00150\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J#\u0010)\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020*0$0\u00150\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J%\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020,0\u00150\u000e2\u0006\u0010\u001d\u001a\u00020\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\"J#\u0010-\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020.0$0\u00150\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J#\u0010/\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002000$0\u00150\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J\u0010\u00101\u001a\u00020\u000f2\u0006\u00102\u001a\u00020\u000fH\u0002J!\u00103\u001a\u0002042\u0006\u00105\u001a\u00020\u000f2\u0006\u00106\u001a\u000207H\u0096@ø\u0001\u0000¢\u0006\u0002\u00108J1\u00109\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020:0$0\u00150\u000e2\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u000f0$H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010<J\u001d\u0010=\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020>0\u00150\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&J\u001f\u0010?\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010@\u001a\u00020AH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010BJ\u001f\u0010C\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010D\u001a\u00020EH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010FJ'\u0010G\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010H\u001a\u00020I2\u0006\u0010\u0012\u001a\u00020\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010JJ!\u0010K\u001a\u0002042\u0006\u0010L\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010MJ%\u0010N\u001a\u0002042\b\u0010O\u001a\u0004\u0018\u00010\u000f2\b\u0010P\u001a\u0004\u0018\u00010\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010MJ\u0019\u0010Q\u001a\u0002042\u0006\u0010R\u001a\u00020SH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010TJ!\u0010U\u001a\u0002042\u0006\u0010V\u001a\u00020\u000f2\u0006\u0010W\u001a\u00020\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010MJ\u0019\u0010X\u001a\u0002042\u0006\u0010Y\u001a\u00020\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\"J-\u0010Z\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020[0\u00150\u000e2\u0006\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\\\u001a\u00020]H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010^J#\u0010_\u001a\b\u0012\u0004\u0012\u0002H`0\u000e\"\u0006\b\u0000\u0010`\u0018\u0001*\u00020\t2\u0006\u0010a\u001a\u00020bH\u0082\bJ9\u0010c\u001a\u000204*\u00020b2\"\u0010d\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002040f\u0012\u0006\u0012\u0004\u0018\u00010g0eH\u0082@ø\u0001\u0000¢\u0006\u0002\u0010hR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006i"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/data/DataRepository;", "Lcom/android/ntduc/chatgpt/data/DataRepositorySource;", "remoteRepository", "Lcom/android/ntduc/chatgpt/data/remote/RemoteData;", "localRepository", "Lcom/android/ntduc/chatgpt/data/local/LocalData;", "ioDispatcher", "Lkotlin/coroutines/CoroutineContext;", "json", "Lkotlinx/serialization/json/Json;", "client", "Lio/ktor/client/HttpClient;", "(Lcom/android/ntduc/chatgpt/data/remote/RemoteData;Lcom/android/ntduc/chatgpt/data/local/LocalData;Lkotlin/coroutines/CoroutineContext;Lkotlinx/serialization/json/Json;Lio/ktor/client/HttpClient;)V", "callStreamGemini", "Lkotlinx/coroutines/flow/Flow;", "", "generateContentRequest", "Lcom/android/ntduc/chatgpt/data/dto/gemini/GenerateContentRequest;", C8624r7.h.f38041W, "(Lcom/android/ntduc/chatgpt/data/dto/gemini/GenerateContentRequest;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "chatWithPDF", "Lcom/android/ntduc/chatgpt/data/Resource;", "Lcom/android/ntduc/chatgpt/data/dto/pdf/ResponseChatPDF;", MediationMetaData.KEY_VERSION, "requestBody", "Lokhttp3/RequestBody;", "(Ljava/lang/String;Lokhttp3/RequestBody;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkMessage", "Lcom/android/ntduc/chatgpt/data/dto/chat/ResponseModerations;", "token", "(Lokhttp3/RequestBody;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "genImageNowTech", "Lcom/android/ntduc/chatgpt/data/dto/art/ResponseResultAiArt;", AppLovinEventTypes.USER_EXECUTED_SEARCH, "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllBot", "", "Lcom/android/ntduc/chatgpt/data/dto/country/Bot;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getArt", "Lcom/android/ntduc/chatgpt/data/dto/art/ExploreAiArt;", "getCharacter", "Lcom/android/ntduc/chatgpt/data/dto/character/Character;", "getHistorySupport", "Lcom/android/ntduc/chatgpt/data/dto/support/ResponseHistorySupport;", "getReview", "Lcom/android/ntduc/chatgpt/data/dto/onboard/Review;", "getTopic", "Lcom/android/ntduc/chatgpt/data/dto/topic/Topic;", "lookupDataFromResponseTurbo", "jsonString", "putGather", "", C8386l8.a.f36196e, "bodyGather", "Lcom/android/ntduc/chatgpt/data/dto/gather/BodyGather;", "(Ljava/lang/String;Lcom/android/ntduc/chatgpt/data/dto/gather/BodyGather;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "requestAllFiles", "Lcom/android/ntduc/chatgpt/data/dto/file/BaseFile;", "types", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "requestFrames", "Lcom/android/ntduc/chatgpt/data/dto/frames/DataFrames;", "startStreamNowAiServer", "nowAiRequest", "Lcom/android/ntduc/chatgpt/data/dto/now_ai/NowAiRequest;", "(Lcom/android/ntduc/chatgpt/data/dto/now_ai/NowAiRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startStreamOpenAiServer", "openAIRequest", "Lcom/android/ntduc/chatgpt/data/dto/open_ai/OpenAIRequest;", "(Lcom/android/ntduc/chatgpt/data/dto/open_ai/OpenAIRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startStreamPoeAiServer", "poeAIContentRequest", "Lcom/android/ntduc/chatgpt/data/dto/poe_ai/PoeAIContentRequest;", "(Lcom/android/ntduc/chatgpt/data/dto/poe_ai/PoeAIContentRequest;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "trackingChat", "time", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "trackingError", "pass", "gpt", "trackingLike", "bodyTrackingLike", "Lcom/android/ntduc/chatgpt/data/dto/tracking/BodyTrackingLike;", "(Lcom/android/ntduc/chatgpt/data/dto/tracking/BodyTrackingLike;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "trackingMessageNumber", "id", "number", "trackingSeverError", AppLovinEventTypes.USER_VIEWED_CONTENT, "uploadFile", "Lcom/android/ntduc/chatgpt/data/dto/pdf/ResponseUploadFile;", C8624r7.h.f38046b, "Ljava/io/File;", "(Ljava/lang/String;Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "decodeToFlow", "T", AppsFlyerProperties.CHANNEL, "Lio/ktor/utils/io/ByteReadChannel;", "onEachLine", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "(Lio/ktor/utils/io/ByteReadChannel;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
@SourceDebugExtension
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
public final class DataRepository implements DataRepositorySource {

    /* renamed from: a */
    @NotNull
    public final RemoteData f2799a;

    /* renamed from: b */
    @NotNull
    public final LocalData f2800b;

    /* renamed from: c */
    @NotNull
    public final CoroutineContext f2801c;

    /* renamed from: d */
    @NotNull
    public final Json f2802d;

    /* renamed from: e */
    @NotNull
    public final HttpClient f2803e;

    @Inject
    public DataRepository(@NotNull RemoteData remoteRepository, @NotNull LocalData localRepository, @NotNull CoroutineContext ioDispatcher, @NotNull Json json, @NotNull HttpClient client) {
        Intrinsics.checkNotNullParameter(remoteRepository, "remoteRepository");
        Intrinsics.checkNotNullParameter(localRepository, "localRepository");
        Intrinsics.checkNotNullParameter(ioDispatcher, "ioDispatcher");
        Intrinsics.checkNotNullParameter(json, "json");
        Intrinsics.checkNotNullParameter(client, "client");
        this.f2799a = remoteRepository;
        this.f2800b = localRepository;
        this.f2801c = ioDispatcher;
        this.f2802d = json;
        this.f2803e = client;
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x004c, code lost:
    
        if (r6 != null) goto L39;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0026  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:17:0x0074 -> B:18:0x004c). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:34:0x0090 -> B:18:0x004c). Please report as a decompilation issue!!! */
    /* renamed from: q */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object m2656q(DataRepository dataRepository, ByteReadChannel byteReadChannel, Function2 function2, Continuation continuation) {
        DataRepository$onEachLine$1 dataRepository$onEachLine$1;
        int i;
        ByteReadChannel byteReadChannel2;
        Function2 function22;
        String str;
        dataRepository.getClass();
        if (continuation instanceof DataRepository$onEachLine$1) {
            dataRepository$onEachLine$1 = (DataRepository$onEachLine$1) continuation;
            int i2 = dataRepository$onEachLine$1.f2920m;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                dataRepository$onEachLine$1.f2920m = i2 - Integer.MIN_VALUE;
                Object obj = dataRepository$onEachLine$1.f2918k;
                CoroutineSingletons coroutineSingletons = CoroutineSingletons.f78910b;
                i = dataRepository$onEachLine$1.f2920m;
                if (i != 0) {
                    if (i == 1) {
                        function22 = dataRepository$onEachLine$1.f2917j;
                        byteReadChannel2 = dataRepository$onEachLine$1.f2916i;
                        ResultKt.m52845a(obj);
                        dataRepository$onEachLine$1.f2916i = byteReadChannel2;
                        dataRepository$onEachLine$1.f2917j = function22;
                        dataRepository$onEachLine$1.f2920m = 2;
                        obj = byteReadChannel2.mo52562t(dataRepository$onEachLine$1);
                        if (obj == coroutineSingletons) {
                        }
                        ByteReadChannel byteReadChannel3 = byteReadChannel2;
                        function2 = function22;
                        byteReadChannel = byteReadChannel3;
                        str = (String) obj;
                        if (str != null) {
                        }
                        if (!byteReadChannel.mo52501O()) {
                        }
                    } else if (i == 2) {
                        function22 = dataRepository$onEachLine$1.f2917j;
                        byteReadChannel2 = dataRepository$onEachLine$1.f2916i;
                        ResultKt.m52845a(obj);
                        ByteReadChannel byteReadChannel32 = byteReadChannel2;
                        function2 = function22;
                        byteReadChannel = byteReadChannel32;
                        str = (String) obj;
                        if (str != null) {
                            if (str.length() == 0) {
                            }
                            if (0 != 0) {
                                dataRepository$onEachLine$1.f2916i = byteReadChannel;
                                dataRepository$onEachLine$1.f2917j = function2;
                                dataRepository$onEachLine$1.f2920m = 3;
                                if (function2.mo59645invoke(null, dataRepository$onEachLine$1) == coroutineSingletons) {
                                    return coroutineSingletons;
                                }
                            }
                        }
                        if (!byteReadChannel.mo52501O()) {
                            dataRepository$onEachLine$1.f2916i = byteReadChannel;
                            dataRepository$onEachLine$1.f2917j = function2;
                            dataRepository$onEachLine$1.f2920m = 1;
                            if (byteReadChannel.mo52554p(dataRepository$onEachLine$1) == coroutineSingletons) {
                                return coroutineSingletons;
                            }
                            Function2 function23 = function2;
                            byteReadChannel2 = byteReadChannel;
                            function22 = function23;
                            dataRepository$onEachLine$1.f2916i = byteReadChannel2;
                            dataRepository$onEachLine$1.f2917j = function22;
                            dataRepository$onEachLine$1.f2920m = 2;
                            obj = byteReadChannel2.mo52562t(dataRepository$onEachLine$1);
                            if (obj == coroutineSingletons) {
                                return coroutineSingletons;
                            }
                            ByteReadChannel byteReadChannel322 = byteReadChannel2;
                            function2 = function22;
                            byteReadChannel = byteReadChannel322;
                            str = (String) obj;
                            if (str != null) {
                            }
                            if (!byteReadChannel.mo52501O()) {
                                return Unit.f78760a;
                            }
                        }
                    } else {
                        if (i != 3) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        function2 = dataRepository$onEachLine$1.f2917j;
                        byteReadChannel = dataRepository$onEachLine$1.f2916i;
                    }
                }
                ResultKt.m52845a(obj);
                if (!byteReadChannel.mo52501O()) {
                }
            }
        }
        dataRepository$onEachLine$1 = new DataRepository$onEachLine$1(dataRepository, continuation);
        Object obj2 = dataRepository$onEachLine$1.f2918k;
        CoroutineSingletons coroutineSingletons2 = CoroutineSingletons.f78910b;
        i = dataRepository$onEachLine$1.f2920m;
        if (i != 0) {
        }
        ResultKt.m52845a(obj2);
        if (!byteReadChannel.mo52501O()) {
        }
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: a */
    public final Object mo2657a(@NotNull String str, @NotNull String str2, @NotNull Continuation<? super Unit> continuation) {
        Object m2701h = this.f2799a.m2701h(str, str2, continuation);
        return m2701h == CoroutineSingletons.f78910b ? m2701h : Unit.f78760a;
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: b */
    public final Object mo2658b(@NotNull String str, @NotNull BodyGather bodyGather, @NotNull Continuation<? super Unit> continuation) {
        Object m2699f = this.f2799a.m2699f(str, bodyGather, continuation);
        return m2699f == CoroutineSingletons.f78910b ? m2699f : Unit.f78760a;
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: c */
    public final Object mo2659c(@NotNull String str, @NotNull Continuation<? super Unit> continuation) {
        Object m2705l = this.f2799a.m2705l(str, continuation);
        return m2705l == CoroutineSingletons.f78910b ? m2705l : Unit.f78760a;
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: d */
    public final Object mo2660d(@NotNull String str, @NotNull String str2, @NotNull Continuation<? super Unit> continuation) {
        Object m2704k = this.f2799a.m2704k(str, str2, continuation);
        return m2704k == CoroutineSingletons.f78910b ? m2704k : Unit.f78760a;
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: e */
    public final Object mo2661e(@NotNull BodyTrackingLike bodyTrackingLike, @NotNull Continuation<? super Unit> continuation) {
        Object m2703j = this.f2799a.m2703j(bodyTrackingLike, continuation);
        return m2703j == CoroutineSingletons.f78910b ? m2703j : Unit.f78760a;
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: f */
    public final Flow mo2662f() {
        return FlowKt.m55504s(FlowKt.m55503r(new DataRepository$getArt$2(this, null)), this.f2801c);
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: g */
    public final Flow mo2663g(@NotNull String str) {
        return FlowKt.m55504s(FlowKt.m55503r(new DataRepository$getHistorySupport$2(this, str, null)), this.f2801c);
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: h */
    public final Flow mo2664h(@NotNull String str, @NotNull File file) {
        return FlowKt.m55504s(FlowKt.m55503r(new DataRepository$uploadFile$2(this, str, file, null)), this.f2801c);
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: i */
    public final Flow mo2665i() {
        return FlowKt.m55504s(FlowKt.m55503r(new DataRepository$getReview$2(this, null)), this.f2801c);
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: j */
    public final Flow mo2666j(@NotNull String str, @NotNull _RequestBodyCommonKt$commonToRequestBody$1 _requestbodycommonkt_commontorequestbody_1) {
        return FlowKt.m55504s(FlowKt.m55503r(new DataRepository$chatWithPDF$2(this, str, null, _requestbodycommonkt_commontorequestbody_1)), this.f2801c);
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x0030, code lost:
    
        if (r0 == null) goto L8;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0091  */
    /* JADX WARN: Type inference failed for: r0v13, types: [com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$$inlined$filter$1] */
    /* JADX WARN: Type inference failed for: r10v3, types: [com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$$inlined$map$1] */
    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: k */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final DataRepository$startStreamNowAiServer$$inlined$map$1 mo2667k(@NotNull NowAiRequest nowAiRequest) {
        String secretKey;
        String str;
        String str2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
        long timeInMillis = calendar.getTimeInMillis();
        String m22120d = new RemoteConfigManager().f5523a.m22120d("key_hash_hmac_sha512");
        Intrinsics.checkNotNullExpressionValue(m22120d, "getString(...)");
        try {
            secretKey = (String) new Gson().fromJson(m22120d, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        secretKey = "<REDACTED: 见 docs/PROTOCOL.md §3.2，密钥通过 NOWAI_HMAC_SECRET 或 Remote Config 提供>";
        Intrinsics.checkNotNullParameter(secretKey, "secretKey");
        try {
            String valueOf = String.valueOf(timeInMillis);
            Charset charset = Charsets.UTF_8;
            byte[] bytes = valueOf.getBytes(charset);
            Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
            Mac mac = Mac.getInstance("HmacSHA512");
            byte[] bytes2 = secretKey.getBytes(charset);
            Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
            mac.init(new SecretKeySpec(bytes2, "HmacSHA512"));
            str = Base64.encodeToString(mac.doFinal(bytes), 2);
        } catch (InvalidKeyException e2) {
            e2.printStackTrace();
            str = null;
            str2 = str;
            if (str2 == null) {
                throw new Exception("token null");
            }
            final Flow m55491f = FlowKt.m55491f(new DataRepository$startStreamNowAiServer$2(this, str2, timeInMillis, nowAiRequest, null));
            final ?? r02 = new Flow<NowAIResponse>() { // from class: com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$$inlined$filter$1

                @Metadata(m52837d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\b"}, m52838d2 = {"<anonymous>", "", "T", "R", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1", "kotlinx/coroutines/flow/FlowKt__TransformKt$filter$$inlined$unsafeTransform$1$2"}, m52839k = 3, m52840mv = {1, 9, 0}, m52842xi = 48)
                @SourceDebugExtension
                /* renamed from: com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$$inlined$filter$1$2 */
                public static final class C14912<T> implements FlowCollector {

                    /* renamed from: b */
                    public final /* synthetic */ FlowCollector f2818b;

                    @Metadata(m52839k = 3, m52840mv = {1, 9, 0}, m52842xi = 48)
                    @DebugMetadata(m53051c = "com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$$inlined$filter$1$2", m53052f = "DataRepository.kt", m53053l = {223}, m53054m = "emit")
                    @SourceDebugExtension
                    /* renamed from: com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$$inlined$filter$1$2$1, reason: invalid class name */
                    public static final class AnonymousClass1 extends ContinuationImpl {

                        /* renamed from: i */
                        public /* synthetic */ Object f2819i;

                        /* renamed from: j */
                        public int f2820j;

                        public AnonymousClass1(Continuation continuation) {
                            super(continuation);
                        }

                        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                        @Nullable
                        public final Object invokeSuspend(@NotNull Object obj) {
                            this.f2819i = obj;
                            this.f2820j |= Integer.MIN_VALUE;
                            return C14912.this.emit(null, this);
                        }
                    }

                    public C14912(FlowCollector flowCollector) {
                        this.f2818b = flowCollector;
                    }

                    /* JADX WARN: Removed duplicated region for block: B:15:0x002f  */
                    /* JADX WARN: Removed duplicated region for block: B:8:0x0021  */
                    @Override // kotlinx.coroutines.flow.FlowCollector
                    @Nullable
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                    */
                    public final Object emit(Object obj, @NotNull Continuation continuation) {
                        AnonymousClass1 anonymousClass1;
                        int i;
                        if (continuation instanceof AnonymousClass1) {
                            anonymousClass1 = (AnonymousClass1) continuation;
                            int i2 = anonymousClass1.f2820j;
                            if ((i2 & Integer.MIN_VALUE) != 0) {
                                anonymousClass1.f2820j = i2 - Integer.MIN_VALUE;
                                Object obj2 = anonymousClass1.f2819i;
                                CoroutineSingletons coroutineSingletons = CoroutineSingletons.f78910b;
                                i = anonymousClass1.f2820j;
                                if (i != 0) {
                                    ResultKt.m52845a(obj2);
                                    if (Intrinsics.areEqual(((NowAIResponse) obj).getStatus(), "stream")) {
                                        anonymousClass1.f2820j = 1;
                                        if (this.f2818b.emit(obj, anonymousClass1) == coroutineSingletons) {
                                            return coroutineSingletons;
                                        }
                                    }
                                } else {
                                    if (i != 1) {
                                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                    }
                                    ResultKt.m52845a(obj2);
                                }
                                return Unit.f78760a;
                            }
                        }
                        anonymousClass1 = new AnonymousClass1(continuation);
                        Object obj22 = anonymousClass1.f2819i;
                        CoroutineSingletons coroutineSingletons2 = CoroutineSingletons.f78910b;
                        i = anonymousClass1.f2820j;
                        if (i != 0) {
                        }
                        return Unit.f78760a;
                    }
                }

                @Override // kotlinx.coroutines.flow.Flow
                @Nullable
                public final Object collect(@NotNull FlowCollector<? super NowAIResponse> flowCollector, @NotNull Continuation continuation) {
                    Object collect = Flow.this.collect(new C14912(flowCollector), continuation);
                    return collect == CoroutineSingletons.f78910b ? collect : Unit.f78760a;
                }
            };
            return new Flow<String>() { // from class: com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$$inlined$map$1

                @Metadata(m52837d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\b"}, m52838d2 = {"<anonymous>", "", "T", "R", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1", "kotlinx/coroutines/flow/FlowKt__TransformKt$map$$inlined$unsafeTransform$1$2"}, m52839k = 3, m52840mv = {1, 9, 0}, m52842xi = 48)
                @SourceDebugExtension
                /* renamed from: com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$$inlined$map$1$2 */
                public static final class C14922<T> implements FlowCollector {

                    /* renamed from: b */
                    public final /* synthetic */ FlowCollector f2823b;

                    @Metadata(m52839k = 3, m52840mv = {1, 9, 0}, m52842xi = 48)
                    @DebugMetadata(m53051c = "com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$$inlined$map$1$2", m53052f = "DataRepository.kt", m53053l = {223}, m53054m = "emit")
                    @SourceDebugExtension
                    /* renamed from: com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$$inlined$map$1$2$1, reason: invalid class name */
                    public static final class AnonymousClass1 extends ContinuationImpl {

                        /* renamed from: i */
                        public /* synthetic */ Object f2824i;

                        /* renamed from: j */
                        public int f2825j;

                        public AnonymousClass1(Continuation continuation) {
                            super(continuation);
                        }

                        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                        @Nullable
                        public final Object invokeSuspend(@NotNull Object obj) {
                            this.f2824i = obj;
                            this.f2825j |= Integer.MIN_VALUE;
                            return C14922.this.emit(null, this);
                        }
                    }

                    public C14922(FlowCollector flowCollector) {
                        this.f2823b = flowCollector;
                    }

                    /* JADX WARN: Removed duplicated region for block: B:15:0x002f  */
                    /* JADX WARN: Removed duplicated region for block: B:8:0x0021  */
                    @Override // kotlinx.coroutines.flow.FlowCollector
                    @Nullable
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                    */
                    public final Object emit(Object obj, @NotNull Continuation continuation) {
                        AnonymousClass1 anonymousClass1;
                        int i;
                        if (continuation instanceof AnonymousClass1) {
                            anonymousClass1 = (AnonymousClass1) continuation;
                            int i2 = anonymousClass1.f2825j;
                            if ((i2 & Integer.MIN_VALUE) != 0) {
                                anonymousClass1.f2825j = i2 - Integer.MIN_VALUE;
                                Object obj2 = anonymousClass1.f2824i;
                                CoroutineSingletons coroutineSingletons = CoroutineSingletons.f78910b;
                                i = anonymousClass1.f2825j;
                                if (i != 0) {
                                    ResultKt.m52845a(obj2);
                                    String content = ((NowAIResponse) obj).getContent();
                                    anonymousClass1.f2825j = 1;
                                    if (this.f2823b.emit(content, anonymousClass1) == coroutineSingletons) {
                                        return coroutineSingletons;
                                    }
                                } else {
                                    if (i != 1) {
                                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                    }
                                    ResultKt.m52845a(obj2);
                                }
                                return Unit.f78760a;
                            }
                        }
                        anonymousClass1 = new AnonymousClass1(continuation);
                        Object obj22 = anonymousClass1.f2824i;
                        CoroutineSingletons coroutineSingletons2 = CoroutineSingletons.f78910b;
                        i = anonymousClass1.f2825j;
                        if (i != 0) {
                        }
                        return Unit.f78760a;
                    }
                }

                @Override // kotlinx.coroutines.flow.Flow
                @Nullable
                public final Object collect(@NotNull FlowCollector<? super String> flowCollector, @NotNull Continuation continuation) {
                    Object collect = r02.collect(new C14922(flowCollector), continuation);
                    return collect == CoroutineSingletons.f78910b ? collect : Unit.f78760a;
                }
            };
        } catch (NoSuchAlgorithmException e3) {
            e3.printStackTrace();
            str = null;
            str2 = str;
            if (str2 == null) {
            }
        }
        str2 = str;
        if (str2 == null) {
        }
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: l */
    public final Flow mo2668l() {
        return FlowKt.m55504s(FlowKt.m55503r(new DataRepository$getTopic$2(this, null)), this.f2801c);
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: m */
    public final Flow mo2669m(@NotNull String str) {
        return FlowKt.m55504s(FlowKt.m55503r(new DataRepository$genImageNowTech$2(this, str, null)), this.f2801c);
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: n */
    public final Object mo2670n(@Nullable String str, @NotNull Continuation continuation) {
        Object m2702i = this.f2799a.m2702i(str, continuation);
        return m2702i == CoroutineSingletons.f78910b ? m2702i : Unit.f78760a;
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: o */
    public final Flow mo2671o() {
        return FlowKt.m55504s(FlowKt.m55503r(new DataRepository$getCharacter$2(this, null)), this.f2801c);
    }

    @Override // com.android.ntduc.chatgpt.data.DataRepositorySource
    @Nullable
    /* renamed from: p */
    public final Flow mo2672p() {
        return FlowKt.m55504s(FlowKt.m55503r(new DataRepository$getAllBot$2(this, null)), this.f2801c);
    }
}
