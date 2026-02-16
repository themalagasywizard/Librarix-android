package com.librarix.presentation.viewmodel;

import com.librarix.data.local.ReadingStatsStore;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<LibraryRepository> libraryRepositoryProvider;

  private final Provider<ReadingStatsStore> readingStatsStoreProvider;

  public HomeViewModel_Factory(Provider<LibraryRepository> libraryRepositoryProvider,
      Provider<ReadingStatsStore> readingStatsStoreProvider) {
    this.libraryRepositoryProvider = libraryRepositoryProvider;
    this.readingStatsStoreProvider = readingStatsStoreProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(libraryRepositoryProvider.get(), readingStatsStoreProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<LibraryRepository> libraryRepositoryProvider,
      Provider<ReadingStatsStore> readingStatsStoreProvider) {
    return new HomeViewModel_Factory(libraryRepositoryProvider, readingStatsStoreProvider);
  }

  public static HomeViewModel newInstance(LibraryRepository libraryRepository,
      ReadingStatsStore readingStatsStore) {
    return new HomeViewModel(libraryRepository, readingStatsStore);
  }
}
