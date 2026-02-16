-if class com.librarix.data.remote.CollectionBookItem
-keepnames class com.librarix.data.remote.CollectionBookItem
-if class com.librarix.data.remote.CollectionBookItem
-keep class com.librarix.data.remote.CollectionBookItemJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
