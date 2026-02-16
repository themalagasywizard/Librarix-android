package com.librarix.presentation.viewmodel;

import com.librarix.data.remote.BookAPIClient;
import com.librarix.data.remote.OpenLibraryClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DiscoverViewModel_Factory implements Factory<DiscoverViewModel> {
  private final Provider<OpenLibraryClient> openLibraryClientProvider;

  private final Provider<BookAPIClient> bookAPIClientProvider;

  public DiscoverViewModel_Factory(Provider<OpenLibraryClient> openLibraryClientProvider,
      Provider<BookAPIClient> bookAPIClientProvider) {
    this.openLibraryClientProvider = openLibraryClientProvider;
    this.bookAPIClientProvider = bookAPIClientProvider;
  }

  @Override
  public DiscoverViewModel get() {
    return newInstance(openLibraryClientProvider.get(), bookAPIClientProvider.get());
  }

  public static DiscoverViewModel_Factory create(
      Provider<OpenLibraryClient> openLibraryClientProvider,
      Provider<BookAPIClient> bookAPIClientProvider) {
    return new DiscoverViewModel_Factory(openLibraryClientProvider, bookAPIClientProvider);
  }

  public static DiscoverViewModel newInstance(OpenLibraryClient openLibraryClient,
      BookAPIClient bookAPIClient) {
    return new DiscoverViewModel(openLibraryClient, bookAPIClient);
  }
}
