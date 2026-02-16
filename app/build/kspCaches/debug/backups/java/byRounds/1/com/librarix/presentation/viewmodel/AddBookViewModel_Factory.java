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
public final class AddBookViewModel_Factory implements Factory<AddBookViewModel> {
  private final Provider<OpenLibraryClient> openLibraryClientProvider;

  public AddBookViewModel_Factory(Provider<OpenLibraryClient> openLibraryClientProvider) {
    this.openLibraryClientProvider = openLibraryClientProvider;
  }

  @Override
  public AddBookViewModel get() {
    return newInstance(openLibraryClientProvider.get());
  }

  public static AddBookViewModel_Factory create(
      Provider<OpenLibraryClient> openLibraryClientProvider) {
    return new AddBookViewModel_Factory(openLibraryClientProvider);
  }

  public static AddBookViewModel newInstance(OpenLibraryClient openLibraryClient) {
    return new AddBookViewModel(openLibraryClient);
  }
}
