package com.android.ntduc.chatgpt.data.dto.now_ai;

import com.fyber.inneractive.sdk.external.InneractiveMediationNameConsts;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.Serializable;
import kotlinx.serialization.internal.ArrayListSerializer;
import kotlinx.serialization.internal.PluginExceptionsKt;
import kotlinx.serialization.internal.SerializationConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(m52837d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0087\b\u0018\u0000 \u001d2\u00020\u0001:\u0002\u001c\u001dB)\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\u0002\u0010\tB\u0013\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\nJ\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0003J\u0019\u0010\u000e\u001a\u00020\u00002\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J!\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bHÇ\u0001R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u001e"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/data/dto/now_ai/NowAiRequest;", "", "seen1", "", "contents", "", "Lcom/android/ntduc/chatgpt/data/dto/now_ai/NowAIContent;", "serializationConstructorMarker", "Lkotlinx/serialization/internal/SerializationConstructorMarker;", "(ILjava/util/List;Lkotlinx/serialization/internal/SerializationConstructorMarker;)V", "(Ljava/util/List;)V", "getContents", "()Ljava/util/List;", "component1", "copy", "equals", "", InneractiveMediationNameConsts.OTHER, "hashCode", "toString", "", "write$Self", "", "self", "output", "Lkotlinx/serialization/encoding/CompositeEncoder;", "serialDesc", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "$serializer", "Companion", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
@Serializable
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
public final /* data */ class NowAiRequest {

    @NotNull
    private final List<NowAIContent> contents;

    /* renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @JvmField
    @NotNull
    private static final KSerializer<Object>[] $childSerializers = {new ArrayListSerializer(NowAIContent$$serializer.INSTANCE)};

    @Metadata(m52837d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004HÆ\u0001¨\u0006\u0006"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/data/dto/now_ai/NowAiRequest$Companion;", "", "()V", "serializer", "Lkotlinx/serialization/KSerializer;", "Lcom/android/ntduc/chatgpt/data/dto/now_ai/NowAiRequest;", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @NotNull
        public final KSerializer<NowAiRequest> serializer() {
            return NowAiRequest$$serializer.INSTANCE;
        }
    }

    @Deprecated
    public /* synthetic */ NowAiRequest(int i, List list, SerializationConstructorMarker serializationConstructorMarker) {
        if (1 == (i & 1)) {
            this.contents = list;
        } else {
            PluginExceptionsKt.m55797a(i, 1, NowAiRequest$$serializer.INSTANCE.getDescriptor());
            throw null;
        }
    }

    public NowAiRequest(@NotNull List<NowAIContent> contents) {
        Intrinsics.checkNotNullParameter(contents, "contents");
        this.contents = contents;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ NowAiRequest copy$default(NowAiRequest nowAiRequest, List list, int i, Object obj) {
        if ((i & 1) != 0) {
            list = nowAiRequest.contents;
        }
        return nowAiRequest.copy(list);
    }

    @NotNull
    public final List<NowAIContent> component1() {
        return this.contents;
    }

    @NotNull
    public final NowAiRequest copy(@NotNull List<NowAIContent> contents) {
        Intrinsics.checkNotNullParameter(contents, "contents");
        return new NowAiRequest(contents);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof NowAiRequest) && Intrinsics.areEqual(this.contents, ((NowAiRequest) other).contents);
    }

    @NotNull
    public final List<NowAIContent> getContents() {
        return this.contents;
    }

    public int hashCode() {
        return this.contents.hashCode();
    }

    @NotNull
    public String toString() {
        return "NowAiRequest(contents=" + this.contents + ")";
    }
}
