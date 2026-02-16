-if class com.librarix.data.remote.OpenLibraryEdition
-keepnames class com.librarix.data.remote.OpenLibraryEdition
-if class com.librarix.data.remote.OpenLibraryEdition
-keep class com.librarix.data.remote.OpenLibraryEditionJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
