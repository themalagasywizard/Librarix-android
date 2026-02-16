-if class com.librarix.data.remote.PublisherData
-keepnames class com.librarix.data.remote.PublisherData
-if class com.librarix.data.remote.PublisherData
-keep class com.librarix.data.remote.PublisherDataJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.librarix.data.remote.PublisherData
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.librarix.data.remote.PublisherData
-keepclassmembers class com.librarix.data.remote.PublisherData {
    public synthetic <init>(com.librarix.data.remote.PublisherName,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
