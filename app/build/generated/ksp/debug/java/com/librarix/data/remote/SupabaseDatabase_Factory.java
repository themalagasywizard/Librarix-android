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
public final class SupabaseDatabase_Factory implements Factory<SupabaseDatabase> {
  private final Provider<SupabaseAuthManager> supabaseAuthManagerProvider;

  public SupabaseDatabase_Factory(Provider<SupabaseAuthManager> supabaseAuthManagerProvider) {
    this.supabaseAuthManagerProvider = supabaseAuthManagerProvider;
  }

  @Override
  public SupabaseDatabase get() {
    return newInstance(supabaseAuthManagerProvider.get());
  }

  public static SupabaseDatabase_Factory create(
      Provider<SupabaseAuthManager> supabaseAuthManagerProvider) {
    return new SupabaseDatabase_Factory(supabaseAuthManagerProvider);
  }

  public static SupabaseDatabase newInstance(SupabaseAuthManager supabaseAuthManager) {
    return new SupabaseDatabase(supabaseAuthManager);
  }
}
