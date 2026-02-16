package com.librarix.data.di;

import com.librarix.data.remote.BookBrainApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("javax.inject.Named")
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
public final class NetworkModule_ProvideBookBrainApiFactory implements Factory<BookBrainApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideBookBrainApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public BookBrainApi get() {
    return provideBookBrainApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideBookBrainApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideBookBrainApiFactory(retrofitProvider);
  }

  public static BookBrainApi provideBookBrainApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideBookBrainApi(retrofit));
  }
}
