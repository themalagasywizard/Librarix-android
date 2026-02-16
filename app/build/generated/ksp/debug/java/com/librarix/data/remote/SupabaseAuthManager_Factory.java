package com.librarix.data.remote;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class SupabaseAuthManager_Factory implements Factory<SupabaseAuthManager> {
  @Override
  public SupabaseAuthManager get() {
    return newInstance();
  }

  public static SupabaseAuthManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SupabaseAuthManager newInstance() {
    return new SupabaseAuthManager();
  }

  private static final class InstanceHolder {
    private static final SupabaseAuthManager_Factory INSTANCE = new SupabaseAuthManager_Factory();
  }
}
