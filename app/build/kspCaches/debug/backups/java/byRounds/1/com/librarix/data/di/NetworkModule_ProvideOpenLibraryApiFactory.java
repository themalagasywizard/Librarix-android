package com.librarix.data.di;

import com.librarix.data.remote.OpenLibraryApi;
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
public final class NetworkModule_ProvideOpenLibraryApiFactory implements Factory<OpenLibraryApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideOpenLibraryApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public OpenLibraryApi get() {
    return provideOpenLibraryApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideOpenLibraryApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideOpenLibraryApiFactory(retrofitProvider);
  }

  public static OpenLibraryApi provideOpenLibraryApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideOpenLibraryApi(retrofit));
  }
}
