-if class com.librarix.data.remote.APIHealth
-keepnames class com.librarix.data.remote.APIHealth
-if class com.librarix.data.remote.APIHealth
-keep class com.librarix.data.remote.APIHealthJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
