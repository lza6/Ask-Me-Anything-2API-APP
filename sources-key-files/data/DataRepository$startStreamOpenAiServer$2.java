package com.android.ntduc.chatgpt.data;

import com.android.ntduc.chatgpt.data.dto.open_ai.OpenAIError;
import com.android.ntduc.chatgpt.data.dto.open_ai.OpenAIRequest;
import com.android.ntduc.chatgpt.data.dto.open_ai.OpenAIResponse;
import com.android.ntduc.chatgpt.data.dto.remoteconfig.OpenAiAcc;
import com.android.ntduc.chatgpt.utils.RemoteConfigManager;
import com.google.gson.Gson;
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
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
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

@Metadata(m52837d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, m52838d2 = {"<anonymous>", "", "Lkotlinx/coroutines/channels/ProducerScope;", "Lcom/android/ntduc/chatgpt/data/dto/open_ai/OpenAIResponse;"}, m52839k = 3, m52840mv = {1, 9, 0}, m52842xi = 48)
@DebugMetadata(m53051c = "com.android.ntduc.chatgpt.data.DataRepository$startStreamOpenAiServer$2", m53052f = "DataRepository.kt", m53053l = {}, m53054m = "invokeSuspend")
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
final class DataRepository$startStreamOpenAiServer$2 extends SuspendLambda implements Function2<ProducerScope<? super OpenAIResponse>, Continuation<? super Unit>, Object> {

    /* renamed from: i */
    public /* synthetic */ Object f2944i;

    /* renamed from: j */
    public final /* synthetic */ DataRepository f2945j;

    /* renamed from: k */
    public final /* synthetic */ OpenAIRequest f2946k;

    @Metadata(m52837d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, m52838d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, m52839k = 3, m52840mv = {1, 9, 0}, m52842xi = 48)
    @DebugMetadata(m53051c = "com.android.ntduc.chatgpt.data.DataRepository$startStreamOpenAiServer$2$1", m53052f = "DataRepository.kt", m53053l = {167}, m53054m = "invokeSuspend")
    @SourceDebugExtension
    /* renamed from: com.android.ntduc.chatgpt.data.DataRepository$startStreamOpenAiServer$2$1 */
    public static final class C15011 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {

        /* renamed from: i */
        public int f2947i;

        /* renamed from: j */
        public final /* synthetic */ DataRepository f2948j;

        /* renamed from: k */
        public final /* synthetic */ OpenAIRequest f2949k;

        /* renamed from: l */
        public final /* synthetic */ ProducerScope<OpenAIResponse> f2950l;

        @Metadata(m52837d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, m52838d2 = {"<anonymous>", "", "it", "Lio/ktor/client/statement/HttpResponse;"}, m52839k = 3, m52840mv = {1, 9, 0}, m52842xi = 48)
        @DebugMetadata(m53051c = "com.android.ntduc.chatgpt.data.DataRepository$startStreamOpenAiServer$2$1$2", m53052f = "DataRepository.kt", m53053l = {169, 179, 182}, m53054m = "invokeSuspend")
        @SourceDebugExtension
        /* renamed from: com.android.ntduc.chatgpt.data.DataRepository$startStreamOpenAiServer$2$1$2, reason: invalid class name */
        public static final class AnonymousClass2 extends SuspendLambda implements Function2<HttpResponse, Continuation<? super Unit>, Object> {

            /* renamed from: i */
            public int f2951i;

            /* renamed from: j */
            public /* synthetic */ Object f2952j;

            /* renamed from: k */
            public final /* synthetic */ DataRepository f2953k;

            /* renamed from: l */
            public final /* synthetic */ ProducerScope<OpenAIResponse> f2954l;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            public AnonymousClass2(DataRepository dataRepository, ProducerScope<? super OpenAIResponse> producerScope, Continuation<? super AnonymousClass2> continuation) {
                super(2, continuation);
                this.f2953k = dataRepository;
                this.f2954l = producerScope;
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @NotNull
            public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
                AnonymousClass2 anonymousClass2 = new AnonymousClass2(this.f2953k, this.f2954l, continuation);
                anonymousClass2.f2952j = obj;
                return anonymousClass2;
            }

            @Override // kotlin.jvm.functions.Function2
            /* renamed from: invoke */
            public final Object mo59645invoke(HttpResponse httpResponse, Continuation<? super Unit> continuation) {
                return ((AnonymousClass2) create(httpResponse, continuation)).invokeSuspend(Unit.f78760a);
            }

            /* JADX WARN: Removed duplicated region for block: B:15:0x0090 A[RETURN] */
            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @Nullable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public final Object invokeSuspend(@NotNull Object obj) {
                Flow m55491f;
                FlowCollector flowCollector;
                CoroutineSingletons coroutineSingletons = CoroutineSingletons.f78910b;
                int i = this.f2951i;
                DataRepository dataRepository = this.f2953k;
                if (i == 0) {
                    ResultKt.m52845a(obj);
                    HttpResponse httpResponse = (HttpResponse) this.f2952j;
                    HttpStatusCode f76010c = httpResponse.getF76010c();
                    HttpStatusCode.f77135d.getClass();
                    if (!Intrinsics.areEqual(f76010c, HttpStatusCode.f77137g)) {
                        this.f2951i = 1;
                        obj = HttpResponseKt.m52290b(httpResponse, Charsets.UTF_8, this);
                        if (obj == coroutineSingletons) {
                            return coroutineSingletons;
                        }
                        String str = (String) obj;
                        Json json = dataRepository.f2802d;
                        json.getClass();
                        str = ((OpenAIError) json.mo55672b(OpenAIError.INSTANCE.serializer(), str)).getError().getMessage();
                        throw new Exception(str);
                    }
                    this.f2951i = 2;
                    obj = HttpResponseKt.m52289a(httpResponse, this);
                    if (obj == coroutineSingletons) {
                        return coroutineSingletons;
                    }
                    m55491f = FlowKt.m55491f(new C1495x5da7a2cd(dataRepository, (ByteReadChannel) obj, dataRepository.f2802d, null));
                    final ProducerScope<OpenAIResponse> producerScope = this.f2954l;
                    flowCollector = new FlowCollector() { // from class: com.android.ntduc.chatgpt.data.DataRepository.startStreamOpenAiServer.2.1.2.1
                        @Override // kotlinx.coroutines.flow.FlowCollector
                        public final Object emit(Object obj2, Continuation continuation) {
                            Object mo55398L = producerScope.mo55398L((OpenAIResponse) obj2, continuation);
                            return mo55398L == CoroutineSingletons.f78910b ? mo55398L : Unit.f78760a;
                        }
                    };
                    this.f2951i = 3;
                    if (((ChannelFlow) m55491f).collect(flowCollector, this) == coroutineSingletons) {
                    }
                } else {
                    if (i == 1) {
                        ResultKt.m52845a(obj);
                        String str2 = (String) obj;
                        try {
                            Json json2 = dataRepository.f2802d;
                            json2.getClass();
                            str2 = ((OpenAIError) json2.mo55672b(OpenAIError.INSTANCE.serializer(), str2)).getError().getMessage();
                        } catch (Throwable unused) {
                        }
                        throw new Exception(str2);
                    }
                    if (i == 2) {
                        ResultKt.m52845a(obj);
                        m55491f = FlowKt.m55491f(new C1495x5da7a2cd(dataRepository, (ByteReadChannel) obj, dataRepository.f2802d, null));
                        final ProducerScope<? super OpenAIResponse> producerScope2 = this.f2954l;
                        flowCollector = new FlowCollector() { // from class: com.android.ntduc.chatgpt.data.DataRepository.startStreamOpenAiServer.2.1.2.1
                            @Override // kotlinx.coroutines.flow.FlowCollector
                            public final Object emit(Object obj2, Continuation continuation) {
                                Object mo55398L = producerScope2.mo55398L((OpenAIResponse) obj2, continuation);
                                return mo55398L == CoroutineSingletons.f78910b ? mo55398L : Unit.f78760a;
                            }
                        };
                        this.f2951i = 3;
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
        public C15011(DataRepository dataRepository, OpenAIRequest openAIRequest, ProducerScope<? super OpenAIResponse> producerScope, Continuation<? super C15011> continuation) {
            super(2, continuation);
            this.f2948j = dataRepository;
            this.f2949k = openAIRequest;
            this.f2950l = producerScope;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
            return new C15011(this.f2948j, this.f2949k, this.f2950l, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        /* renamed from: invoke */
        public final Object mo59645invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C15011) create(coroutineScope, continuation)).invokeSuspend(Unit.f78760a);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            OpenAiAcc openAiAcc;
            CoroutineSingletons coroutineSingletons = CoroutineSingletons.f78910b;
            int i = this.f2947i;
            if (i == 0) {
                ResultKt.m52845a(obj);
                DataRepository dataRepository = this.f2948j;
                HttpClient httpClient = dataRepository.f2803e;
                HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder();
                HttpRequestKt.m52285a(httpRequestBuilder, "https://api.openai.com/v1/chat/completions");
                OpenAIRequest openAIRequest = this.f2949k;
                if (openAIRequest == null) {
                    httpRequestBuilder.m52279c(NullBody.f77230a);
                    KType m53145d = Reflection.m53145d(OpenAIRequest.class);
                    httpRequestBuilder.m52280d(TypeInfoJvmKt.m52458a(TypesJVMKt.m53214d(m53145d), Reflection.m53142a(OpenAIRequest.class), m53145d));
                } else if (openAIRequest instanceof OutgoingContent) {
                    Intrinsics.checkNotNullParameter(openAIRequest, "<set-?>");
                    httpRequestBuilder.f76881d = openAIRequest;
                    httpRequestBuilder.m52280d(null);
                } else {
                    Intrinsics.checkNotNullParameter(openAIRequest, "<set-?>");
                    httpRequestBuilder.f76881d = openAIRequest;
                    KType m53145d2 = Reflection.m53145d(OpenAIRequest.class);
                    httpRequestBuilder.m52280d(TypeInfoJvmKt.m52458a(TypesJVMKt.m53214d(m53145d2), Reflection.m53142a(OpenAIRequest.class), m53145d2));
                }
                String m22120d = new RemoteConfigManager().f5523a.m22120d("remote_key_openai");
                Intrinsics.checkNotNullExpressionValue(m22120d, "getString(...)");
                try {
                    openAiAcc = (OpenAiAcc) new Gson().fromJson(m22120d, OpenAiAcc.class);
                    if (openAiAcc == null) {
                        openAiAcc = new OpenAiAcc(false, null, 3, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    openAiAcc = new OpenAiAcc(false, null, 3, null);
                }
                UtilsKt.m52287a(httpRequestBuilder, "Authorization", "Bearer " + CollectionsKt.first((List<? extends Object>) CollectionsKt.shuffled(openAiAcc.getKeys())));
                ContentType.Application.f77011a.getClass();
                HttpMessagePropertiesKt.m52340e(httpRequestBuilder, ContentType.Application.f77012b);
                HttpMethod.f77120b.getClass();
                httpRequestBuilder.m52282f(HttpMethod.f77122d);
                HttpStatement httpStatement = new HttpStatement(httpRequestBuilder, httpClient);
                AnonymousClass2 anonymousClass2 = new AnonymousClass2(dataRepository, this.f2950l, null);
                this.f2947i = 1;
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
    public DataRepository$startStreamOpenAiServer$2(DataRepository dataRepository, OpenAIRequest openAIRequest, Continuation<? super DataRepository$startStreamOpenAiServer$2> continuation) {
        super(2, continuation);
        this.f2945j = dataRepository;
        this.f2946k = openAIRequest;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        DataRepository$startStreamOpenAiServer$2 dataRepository$startStreamOpenAiServer$2 = new DataRepository$startStreamOpenAiServer$2(this.f2945j, this.f2946k, continuation);
        dataRepository$startStreamOpenAiServer$2.f2944i = obj;
        return dataRepository$startStreamOpenAiServer$2;
    }

    @Override // kotlin.jvm.functions.Function2
    /* renamed from: invoke */
    public final Object mo59645invoke(ProducerScope<? super OpenAIResponse> producerScope, Continuation<? super Unit> continuation) {
        return ((DataRepository$startStreamOpenAiServer$2) create(producerScope, continuation)).invokeSuspend(Unit.f78760a);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.f78910b;
        ResultKt.m52845a(obj);
        ProducerScope producerScope = (ProducerScope) this.f2944i;
        DataRepository dataRepository = this.f2945j;
        BuildersKt.m55252c(producerScope, dataRepository.f2801c, null, new C15011(dataRepository, this.f2946k, producerScope, null), 2);
        return Unit.f78760a;
    }
}
