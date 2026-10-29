package com.android.ntduc.chatgpt.data;

import android.util.Log;
import com.android.ntduc.chatgpt.data.dto.now_ai.NowAIResponse;
import com.android.ntduc.chatgpt.data.dto.now_ai.NowAiError;
import com.android.ntduc.chatgpt.data.dto.now_ai.NowAiRequest;
import io.ktor.client.HttpClient;
import io.ktor.client.request.HttpRequestBuilder;
import io.ktor.client.request.HttpRequestKt;
import io.ktor.client.request.UtilsKt;
import io.ktor.client.statement.HttpResponse;
import io.ktor.client.statement.HttpResponseKt;
import io.ktor.client.statement.HttpStatement;
import io.ktor.http.ContentType;
import io.ktor.http.HttpMessagePropertiesKt;
import io.ktor.http.HttpMethod;
import io.ktor.http.HttpStatusCode;
import io.ktor.http.content.NullBody;
import io.ktor.http.content.OutgoingContent;
import io.ktor.util.reflect.TypeInfoJvmKt;
import io.ktor.utils.p299io.ByteReadChannel;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.reflect.KType;
import kotlin.reflect.TypesJVMKt;
import kotlin.text.Charsets;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.ProducerScope;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.internal.ChannelFlow;
import kotlinx.serialization.json.Json;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(m52837d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, m52838d2 = {"<anonymous>", "", "Lkotlinx/coroutines/channels/ProducerScope;", "Lcom/android/ntduc/chatgpt/data/dto/now_ai/NowAIResponse;"}, m52839k = 3, m52840mv = {1, 9, 0}, m52842xi = 48)
@DebugMetadata(m53051c = "com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$2", m53052f = "DataRepository.kt", m53053l = {}, m53054m = "invokeSuspend")
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
final class DataRepository$startStreamNowAiServer$2 extends SuspendLambda implements Function2<ProducerScope<? super NowAIResponse>, Continuation<? super Unit>, Object> {

    /* renamed from: i */
    public /* synthetic */ Object f2928i;

    /* renamed from: j */
    public final /* synthetic */ DataRepository f2929j;

    /* renamed from: k */
    public final /* synthetic */ String f2930k;

    /* renamed from: l */
    public final /* synthetic */ long f2931l;

    /* renamed from: m */
    public final /* synthetic */ NowAiRequest f2932m;

    @Metadata(m52837d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m52838d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m52839k = 3, m52840mv = {1, 9, 0}, m52842xi = 48)
    @DebugMetadata(m53051c = "com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$2$1", m53052f = "DataRepository.kt", m53053l = {212}, m53054m = "invokeSuspend")
    @SourceDebugExtension
    /* renamed from: com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$2$1 */
    public static final class C15001 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {

        /* renamed from: i */
        public int f2933i;

        /* renamed from: j */
        public final /* synthetic */ DataRepository f2934j;

        /* renamed from: k */
        public final /* synthetic */ String f2935k;

        /* renamed from: l */
        public final /* synthetic */ long f2936l;

        /* renamed from: m */
        public final /* synthetic */ NowAiRequest f2937m;

        /* renamed from: n */
        public final /* synthetic */ ProducerScope<NowAIResponse> f2938n;

        @Metadata(m52837d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, m52838d2 = {"<anonymous>", "", "it", "Lio/ktor/client/statement/HttpResponse;"}, m52839k = 3, m52840mv = {1, 9, 0}, m52842xi = 48)
        @DebugMetadata(m53051c = "com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$2$1$2", m53052f = "DataRepository.kt", m53053l = {214, 225, 228}, m53054m = "invokeSuspend")
        @SourceDebugExtension
        /* renamed from: com.android.ntduc.chatgpt.data.DataRepository$startStreamNowAiServer$2$1$2, reason: invalid class name */
        public static final class AnonymousClass2 extends SuspendLambda implements Function2<HttpResponse, Continuation<? super Unit>, Object> {

            /* renamed from: i */
            public int f2939i;

            /* renamed from: j */
            public /* synthetic */ Object f2940j;

            /* renamed from: k */
            public final /* synthetic */ DataRepository f2941k;

            /* renamed from: l */
            public final /* synthetic */ ProducerScope<NowAIResponse> f2942l;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            public AnonymousClass2(DataRepository dataRepository, ProducerScope<? super NowAIResponse> producerScope, Continuation<? super AnonymousClass2> continuation) {
                super(2, continuation);
                this.f2941k = dataRepository;
                this.f2942l = producerScope;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @NotNull
            public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
                AnonymousClass2 anonymousClass2 = new AnonymousClass2(this.f2941k, this.f2942l, continuation);
                anonymousClass2.f2940j = obj;
                return anonymousClass2;
            }

            @Override // kotlin.jvm.functions.Function2
            /* renamed from: invoke */
            public final Object mo59645invoke(HttpResponse httpResponse, Continuation<? super Unit> continuation) {
                return ((AnonymousClass2) create(httpResponse, continuation)).invokeSuspend(Unit.f78760a);
            }

            /* JADX WARN: Removed duplicated region for block: B:15:0x009f A[RETURN] */
            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @Nullable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public final Object invokeSuspend(@NotNull Object obj) {
                Flow m55491f;
                FlowCollector flowCollector;
                CoroutineSingletons coroutineSingletons = CoroutineSingletons.f78910b;
                int i = this.f2939i;
                DataRepository dataRepository = this.f2941k;
                if (i == 0) {
                    ResultKt.m52845a(obj);
                    HttpResponse httpResponse = (HttpResponse) this.f2940j;
                    HttpStatusCode f76010c = httpResponse.getF76010c();
                    HttpStatusCode.f77135d.getClass();
                    if (!Intrinsics.areEqual(f76010c, HttpStatusCode.f77137g)) {
                        this.f2939i = 1;
                        obj = HttpResponseKt.m52290b(httpResponse, Charsets.UTF_8, this);
                        if (obj == coroutineSingletons) {
                            return coroutineSingletons;
                        }
                        String str = (String) obj;
                        Json json = dataRepository.f2802d;
                        json.getClass();
                        str = ((NowAiError) json.mo55672b(NowAiError.INSTANCE.serializer(), str)).getDetail();
                        Log.i("ServerNowAi", "Error in calling to server Now AI: " + str);
                        throw new Exception(str);
                    }
                    this.f2939i = 2;
                    obj = HttpResponseKt.m52289a(httpResponse, this);
                    if (obj == coroutineSingletons) {
                        return coroutineSingletons;
                    }
                    m55491f = FlowKt.m55491f(new C1493x87293db5(dataRepository, (ByteReadChannel) obj, dataRepository.f2802d, null));
                    final ProducerScope<NowAIResponse> producerScope = this.f2942l;
                    flowCollector = new FlowCollector() { // from class: com.android.ntduc.chatgpt.data.DataRepository.startStreamNowAiServer.2.1.2.1
                        @Override // kotlinx.coroutines.flow.FlowCollector
                        public final Object emit(Object obj2, Continuation continuation) {
                            Object mo55398L = producerScope.mo55398L((NowAIResponse) obj2, continuation);
                            return mo55398L == CoroutineSingletons.f78910b ? mo55398L : Unit.f78760a;
                        }
                    };
                    this.f2939i = 3;
                    if (((ChannelFlow) m55491f).collect(flowCollector, this) == coroutineSingletons) {
                    }
                } else {
                    if (i == 1) {
                        ResultKt.m52845a(obj);
                        String str2 = (String) obj;
                        try {
                            Json json2 = dataRepository.f2802d;
                            json2.getClass();
                            str2 = ((NowAiError) json2.mo55672b(NowAiError.INSTANCE.serializer(), str2)).getDetail();
                        } catch (Throwable unused) {
                        }
                        Log.i("ServerNowAi", "Error in calling to server Now AI: " + str2);
                        throw new Exception(str2);
                    }
                    if (i == 2) {
                        ResultKt.m52845a(obj);
                        m55491f = FlowKt.m55491f(new C1493x87293db5(dataRepository, (ByteReadChannel) obj, dataRepository.f2802d, null));
                        final ProducerScope<? super NowAIResponse> producerScope2 = this.f2942l;
                        flowCollector = new FlowCollector() { // from class: com.android.ntduc.chatgpt.data.DataRepository.startStreamNowAiServer.2.1.2.1
                            @Override // kotlinx.coroutines.flow.FlowCollector
                            public final Object emit(Object obj2, Continuation continuation) {
                                Object mo55398L = producerScope2.mo55398L((NowAIResponse) obj2, continuation);
                                return mo55398L == CoroutineSingletons.f78910b ? mo55398L : Unit.f78760a;
                            }
                        };
                        this.f2939i = 3;
                        if (((ChannelFlow) m55491f).collect(flowCollector, this) == coroutineSingletons) {
                            return coroutineSingletons;
                        }
                    } else {
                        if (i != 3) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.m52845a(obj);
                    }
                }
                return Unit.f78760a;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public C15001(DataRepository dataRepository, String str, long j, NowAiRequest nowAiRequest, ProducerScope<? super NowAIResponse> producerScope, Continuation<? super C15001> continuation) {
            super(2, continuation);
            this.f2934j = dataRepository;
            this.f2935k = str;
            this.f2936l = j;
            this.f2937m = nowAiRequest;
            this.f2938n = producerScope;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
            return new C15001(this.f2934j, this.f2935k, this.f2936l, this.f2937m, this.f2938n, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        /* renamed from: invoke */
        public final Object mo59645invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C15001) create(coroutineScope, continuation)).invokeSuspend(Unit.f78760a);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            CoroutineSingletons coroutineSingletons = CoroutineSingletons.f78910b;
            int i = this.f2933i;
            if (i == 0) {
                ResultKt.m52845a(obj);
                DataRepository dataRepository = this.f2934j;
                HttpClient httpClient = dataRepository.f2803e;
                HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder();
                HttpRequestKt.m52285a(httpRequestBuilder, "http://pass-gpt.nowtechai.com/api/v1/pass");
                UtilsKt.m52287a(httpRequestBuilder, "Key", this.f2935k);
                UtilsKt.m52287a(httpRequestBuilder, "TimeStamps", new Long(this.f2936l));
                NowAiRequest nowAiRequest = this.f2937m;
                if (nowAiRequest == null) {
                    httpRequestBuilder.m52279c(NullBody.f77230a);
                    KType m53145d = Reflection.m53145d(NowAiRequest.class);
                    httpRequestBuilder.m52280d(TypeInfoJvmKt.m52458a(TypesJVMKt.m53214d(m53145d), Reflection.m53142a(NowAiRequest.class), m53145d));
                } else if (nowAiRequest instanceof OutgoingContent) {
                    Intrinsics.checkNotNullParameter(nowAiRequest, "<set-?>");
                    httpRequestBuilder.f76881d = nowAiRequest;
                    httpRequestBuilder.m52280d(null);
                } else {
                    Intrinsics.checkNotNullParameter(nowAiRequest, "<set-?>");
                    httpRequestBuilder.f76881d = nowAiRequest;
                    KType m53145d2 = Reflection.m53145d(NowAiRequest.class);
                    httpRequestBuilder.m52280d(TypeInfoJvmKt.m52458a(TypesJVMKt.m53214d(m53145d2), Reflection.m53142a(NowAiRequest.class), m53145d2));
                }
                ContentType.Application.f77011a.getClass();
                HttpMessagePropertiesKt.m52340e(httpRequestBuilder, ContentType.Application.f77012b);
                HttpMethod.f77120b.getClass();
                httpRequestBuilder.m52282f(HttpMethod.f77122d);
                HttpStatement httpStatement = new HttpStatement(httpRequestBuilder, httpClient);
                AnonymousClass2 anonymousClass2 = new AnonymousClass2(dataRepository, this.f2938n, null);
                this.f2933i = 1;
                if (httpStatement.m52295b(anonymousClass2, this) == coroutineSingletons) {
                    return coroutineSingletons;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.m52845a(obj);
            }
            return Unit.f78760a;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DataRepository$startStreamNowAiServer$2(DataRepository dataRepository, String str, long j, NowAiRequest nowAiRequest, Continuation<? super DataRepository$startStreamNowAiServer$2> continuation) {
        super(2, continuation);
        this.f2929j = dataRepository;
        this.f2930k = str;
        this.f2931l = j;
        this.f2932m = nowAiRequest;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        DataRepository$startStreamNowAiServer$2 dataRepository$startStreamNowAiServer$2 = new DataRepository$startStreamNowAiServer$2(this.f2929j, this.f2930k, this.f2931l, this.f2932m, continuation);
        dataRepository$startStreamNowAiServer$2.f2928i = obj;
        return dataRepository$startStreamNowAiServer$2;
    }

    @Override // kotlin.jvm.functions.Function2
    /* renamed from: invoke */
    public final Object mo59645invoke(ProducerScope<? super NowAIResponse> producerScope, Continuation<? super Unit> continuation) {
        return ((DataRepository$startStreamNowAiServer$2) create(producerScope, continuation)).invokeSuspend(Unit.f78760a);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.f78910b;
        ResultKt.m52845a(obj);
        ProducerScope producerScope = (ProducerScope) this.f2928i;
        DataRepository dataRepository = this.f2929j;
        BuildersKt.m55252c(producerScope, dataRepository.f2801c, null, new C15001(dataRepository, this.f2930k, this.f2931l, this.f2932m, producerScope, null), 2);
        return Unit.f78760a;
    }
}
