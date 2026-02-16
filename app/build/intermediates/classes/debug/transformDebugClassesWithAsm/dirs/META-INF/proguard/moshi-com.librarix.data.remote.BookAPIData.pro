-if class com.librarix.data.remote.BookAPIData
-keepnames class com.librarix.data.remote.BookAPIData
-if class com.librarix.data.remote.BookAPIData
-keep class com.librarix.data.remote.BookAPIDataJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.librarix.data.remote.BookAPIData
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.librarix.data.remote.BookAPIData
-keepclassmembers class com.librarix.data.remote.BookAPIData {
    public synthetic <init>(java.lang.String,java.lang.String,java.util.List,java.lang.String,java.util.List,java.lang.Integer,java.lang.String,com.librarix.data.remote.PublisherData,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,com.librarix.data.remote.APISources,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
