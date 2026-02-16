-if class com.librarix.data.remote.APISources
-keepnames class com.librarix.data.remote.APISources
-if class com.librarix.data.remote.APISources
-keep class com.librarix.data.remote.APISourcesJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.librarix.data.remote.APISources
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.librarix.data.remote.APISources
-keepclassmembers class com.librarix.data.remote.APISources {
    public synthetic <init>(boolean,boolean,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
