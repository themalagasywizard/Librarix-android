package com.librarix.presentation.viewmodel;

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
public final class AuthorDetailViewModel_Factory implements Factory<AuthorDetailViewModel> {
  private final Provider<OpenLibraryClient> openLibraryClientProvider;

  public AuthorDetailViewModel_Factory(Provider<OpenLibraryClient> openLibraryClientProvider) {
    this.openLibraryClientProvider = openLibraryClientProvider;
  }

  @Override
  public AuthorDetailViewModel get() {
    return newInstance(openLibraryClientProvider.get());
  }

  public static AuthorDetailViewModel_Factory create(
      Provider<OpenLibraryClient> openLibraryClientProvider) {
    return new AuthorDetailViewModel_Factory(openLibraryClientProvider);
  }

  public static AuthorDetailViewModel newInstance(OpenLibraryClient openLibraryClient) {
    return new AuthorDetailViewModel(openLibraryClient);
  }
}
