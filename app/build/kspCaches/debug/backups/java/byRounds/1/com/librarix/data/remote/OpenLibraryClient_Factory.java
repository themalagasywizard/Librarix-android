package com.librarix.data.remote;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class OpenLibraryClient_Factory implements Factory<OpenLibraryClient> {
  private final Provider<OpenLibraryApi> apiProvider;

  public OpenLibraryClient_Factory(Provider<OpenLibraryApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public OpenLibraryClient get() {
    return newInstance(apiProvider.get());
  }

  public static OpenLibraryClient_Factory create(Provider<OpenLibraryApi> apiProvider) {
    return new OpenLibraryClient_Factory(apiProvider);
  }

  public static OpenLibraryClient newInstance(OpenLibraryApi api) {
    return new OpenLibraryClient(api);
  }
}
