-if class com.librarix.data.remote.TopSellersResponse
-keepnames class com.librarix.data.remote.TopSellersResponse
-if class com.librarix.data.remote.TopSellersResponse
-keep class com.librarix.data.remote.TopSellersResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
