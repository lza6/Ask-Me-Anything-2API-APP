package com.android.ntduc.chatgpt.data.dto.now_ai;

import com.fyber.inneractive.sdk.external.InneractiveMediationNameConsts;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.Serializable;
import kotlinx.serialization.internal.PluginExceptionsKt;
import kotlinx.serialization.internal.SerializationConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import p002a0.C0003a;

@Metadata(m52837d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0087\b\u0018\u0000 \u001b2\u00020\u0001:\u0002\u001a\u001bB#\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bB\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\tJ\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u0013\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0005HÖ\u0001J!\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00002\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019HÇ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u001c"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/data/dto/now_ai/NowAiError;", "", "seen1", "", "detail", "", "serializationConstructorMarker", "Lkotlinx/serialization/internal/SerializationConstructorMarker;", "(ILjava/lang/String;Lkotlinx/serialization/internal/SerializationConstructorMarker;)V", "(Ljava/lang/String;)V", "getDetail", "()Ljava/lang/String;", "component1", "copy", "equals", "", InneractiveMediationNameConsts.OTHER, "hashCode", "toString", "write$Self", "", "self", "output", "Lkotlinx/serialization/encoding/CompositeEncoder;", "serialDesc", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "$serializer", "Companion", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
@Serializable
/* loaded from: D:\apk-analysis\ama\raw\classes2.dex */
public final /* data */ class NowAiError {

    /* renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    private final String detail;

    @Metadata(m52837d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004HÆ\u0001¨\u0006\u0006"}, m52838d2 = {"Lcom/android/ntduc/chatgpt/data/dto/now_ai/NowAiError$Companion;", "", "()V", "serializer", "Lkotlinx/serialization/KSerializer;", "Lcom/android/ntduc/chatgpt/data/dto/now_ai/NowAiError;", "Now_AI_V4.3.0.0_26.07.2024_12h45_release"}, m52839k = 1, m52840mv = {1, 9, 0}, m52842xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @NotNull
        public final KSerializer<NowAiError> serializer() {
            return NowAiError$$serializer.INSTANCE;
        }
    }

    @Deprecated
    public /* synthetic */ NowAiError(int i, String str, SerializationConstructorMarker serializationConstructorMarker) {
        if (1 == (i & 1)) {
            this.detail = str;
        } else {
            PluginExceptionsKt.m55797a(i, 1, NowAiError$$serializer.INSTANCE.getDescriptor());
            throw null;
        }
    }

    public NowAiError(@NotNull String detail) {
        Intrinsics.checkNotNullParameter(detail, "detail");
        this.detail = detail;
    }

    public static /* synthetic */ NowAiError copy$default(NowAiError nowAiError, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = nowAiError.detail;
        }
        return nowAiError.copy(str);
    }

    @NotNull
    /* renamed from: component1, reason: from getter */
    public final String getDetail() {
        return this.detail;
    }

    @NotNull
    public final NowAiError copy(@NotNull String detail) {
        Intrinsics.checkNotNullParameter(detail, "detail");
        return new NowAiError(detail);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof NowAiError) && Intrinsics.areEqual(this.detail, ((NowAiError) other).detail);
    }

    @NotNull
    public final String getDetail() {
        return this.detail;
    }

    public int hashCode() {
        return this.detail.hashCode();
    }

    @NotNull
    public String toString() {
        return C0003a.m32p("NowAiError(detail=", this.detail, ")");
    }
}
