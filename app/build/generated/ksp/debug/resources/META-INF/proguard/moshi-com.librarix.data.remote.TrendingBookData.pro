-if class com.librarix.data.remote.TrendingBookData
-keepnames class com.librarix.data.remote.TrendingBookData
-if class com.librarix.data.remote.TrendingBookData
-keep class com.librarix.data.remote.TrendingBookDataJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.librarix.data.remote.TrendingBookData
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.librarix.data.remote.TrendingBookData
-keepclassmembers class com.librarix.data.remote.TrendingBookData {
    public synthetic <init>(java.lang.Integer,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,com.librarix.data.remote.BookAPIData,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
