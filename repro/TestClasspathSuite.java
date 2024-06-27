package com.repro;

import java.util.Set;
import java.util.HashSet;
import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;

public final class TestClasspathSuite extends Suite {

  /** Only called reflectively. Do not use programmatically. */
  public TestClasspathSuite(Class<?> klass, RunnerBuilder builder) throws Throwable {
    super(builder, klass, getClasses(klass));
  }

  private static Class<?>[] getClasses(Class<?> klass) {
    Set<Class<?>> result = new HashSet();
    result.add(GoodTest.class);
    result.add(BadTest.class);
    return result.toArray(new Class<?>[result.size()]);
  }
}
