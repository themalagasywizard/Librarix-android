-if class com.librarix.data.remote.CollectionsResponse
-keepnames class com.librarix.data.remote.CollectionsResponse
-if class com.librarix.data.remote.CollectionsResponse
-keep class com.librarix.data.remote.CollectionsResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
