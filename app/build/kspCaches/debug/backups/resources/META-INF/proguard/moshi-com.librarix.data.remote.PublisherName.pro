-if class com.librarix.data.remote.PublisherName
-keepnames class com.librarix.data.remote.PublisherName
-if class com.librarix.data.remote.PublisherName
-keep class com.librarix.data.remote.PublisherNameJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.librarix.data.remote.PublisherName
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.librarix.data.remote.PublisherName
-keepclassmembers class com.librarix.data.remote.PublisherName {
    public synthetic <init>(java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
