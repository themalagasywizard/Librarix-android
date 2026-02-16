-if class com.librarix.data.remote.CuratedCollection
-keepnames class com.librarix.data.remote.CuratedCollection
-if class com.librarix.data.remote.CuratedCollection
-keep class com.librarix.data.remote.CuratedCollectionJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.librarix.data.remote.CuratedCollection
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.librarix.data.remote.CuratedCollection
-keepclassmembers class com.librarix.data.remote.CuratedCollection {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
