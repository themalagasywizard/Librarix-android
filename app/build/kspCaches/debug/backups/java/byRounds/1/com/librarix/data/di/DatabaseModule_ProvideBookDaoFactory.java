package com.librarix.data.di;

import com.librarix.data.local.LibrarixDatabase;
import com.librarix.data.local.dao.BookDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideBookDaoFactory implements Factory<BookDao> {
  private final Provider<LibrarixDatabase> databaseProvider;

  public DatabaseModule_ProvideBookDaoFactory(Provider<LibrarixDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BookDao get() {
    return provideBookDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideBookDaoFactory create(
      Provider<LibrarixDatabase> databaseProvider) {
    return new DatabaseModule_ProvideBookDaoFactory(databaseProvider);
  }

  public static BookDao provideBookDao(LibrarixDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideBookDao(database));
  }
}
