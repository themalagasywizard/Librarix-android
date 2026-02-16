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
public final class BookAPIClient_Factory implements Factory<BookAPIClient> {
  private final Provider<BookBrainApi> apiProvider;

  public BookAPIClient_Factory(Provider<BookBrainApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public BookAPIClient get() {
    return newInstance(apiProvider.get());
  }

  public static BookAPIClient_Factory create(Provider<BookBrainApi> apiProvider) {
    return new BookAPIClient_Factory(apiProvider);
  }

  public static BookAPIClient newInstance(BookBrainApi api) {
    return new BookAPIClient(api);
  }
}
