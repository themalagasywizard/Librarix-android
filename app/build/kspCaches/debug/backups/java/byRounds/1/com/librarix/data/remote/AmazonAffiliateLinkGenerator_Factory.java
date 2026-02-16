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
public final class AmazonAffiliateLinkGenerator_Factory implements Factory<AmazonAffiliateLinkGenerator> {
  @Override
  public AmazonAffiliateLinkGenerator get() {
    return newInstance();
  }

  public static AmazonAffiliateLinkGenerator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AmazonAffiliateLinkGenerator newInstance() {
    return new AmazonAffiliateLinkGenerator();
  }

  private static final class InstanceHolder {
    private static final AmazonAffiliateLinkGenerator_Factory INSTANCE = new AmazonAffiliateLinkGenerator_Factory();
  }
}
