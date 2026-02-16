-if class com.librarix.data.remote.TrendingResponse
-keepnames class com.librarix.data.remote.TrendingResponse
-if class com.librarix.data.remote.TrendingResponse
-keep class com.librarix.data.remote.TrendingResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
