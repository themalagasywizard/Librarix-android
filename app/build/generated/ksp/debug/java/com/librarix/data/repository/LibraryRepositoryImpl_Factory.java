package com.librarix.data.repository;

import com.librarix.data.local.dao.BookDao;
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
public final class LibraryRepositoryImpl_Factory implements Factory<LibraryRepositoryImpl> {
  private final Provider<BookDao> bookDaoProvider;

  public LibraryRepositoryImpl_Factory(Provider<BookDao> bookDaoProvider) {
    this.bookDaoProvider = bookDaoProvider;
  }

  @Override
  public LibraryRepositoryImpl get() {
    return newInstance(bookDaoProvider.get());
  }

  public static LibraryRepositoryImpl_Factory create(Provider<BookDao> bookDaoProvider) {
    return new LibraryRepositoryImpl_Factory(bookDaoProvider);
  }

  public static LibraryRepositoryImpl newInstance(BookDao bookDao) {
    return new LibraryRepositoryImpl(bookDao);
  }
}
