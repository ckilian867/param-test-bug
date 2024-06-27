# param-test-bug

## Summary

If a java_test target runs two test classes that each have a parameterized test with the same name, both the test.xml and and test summary can misreport which test is failing.

## Reproduction

In this reproduction, we have two parameterized test classes: GoodTest.java and BadTest.java. The tests are identical, including the name of the parameterized test, but BadTest.java has a bad assertion which causes the test to fail.

When run with `bazel test repro:repro_test`, GoodTest.java is reported as failing:

```
$ bazel test repro:repro_test
INFO: Analyzed target //repro:repro_test (0 packages loaded, 0 targets configured).
FAIL: //repro:repro_test (see /home/user/.cache/bazel/_bazel_cjk/1249c2b3e8a01f2e196b44c56398532f/execroot/_main/bazel-out/k8-fastbuild/testlogs/repro/repro_test/test.log)
INFO: Found 1 test target...
Target //repro:repro_test up-to-date:
  bazel-bin/repro/repro_test
  bazel-bin/repro/repro_test.jar
INFO: Elapsed time: 0.511s, Critical Path: 0.36s
INFO: 2 processes: 1 internal, 1 processwrapper-sandbox.
INFO: Build completed, 1 test FAILED, 2 total actions
//repro:repro_test                                                       FAILED in 0.4s
    FAILED  com.repro.GoodTest.[0] a (test) (0.0s)
    FAILED  com.repro.GoodTest.[1] b (test) (0.0s)
Test cases: finished with 0 passing and 2 failing out of 2 test cases

Executed 1 out of 1 test: 1 fails locally.
```

The test.xml file also says GoodTest.java failed:

```
$ cat bazel-testlogs/repro/repro_test/test.xml
<?xml version='1.0' encoding='UTF-8'?>
<testsuites>
  <testsuite name='com.repro.AllTests' timestamp='2024-06-27T20:04:10.482Z' hostname='localhost' tests='4' failures='2' errors='0' time='0.009' package='' id='0'>
    <properties />
    <system-out />
    <system-err /></testsuite>
  <testsuite name='com.repro.BadTest' timestamp='' hostname='localhost' tests='2' failures='0' errors='0' time='0.0' package='' id='1'>
    <properties />
    <system-out />
    <system-err /></testsuite>
  <testsuite name='test' timestamp='' hostname='localhost' tests='2' failures='0' errors='0' time='0.0' package='' id='2'>
    <properties />
    <system-out />
    <system-err /></testsuite>
  <testsuite name='com.repro.GoodTest' timestamp='2024-06-27T20:04:10.482Z' hostname='localhost' tests='2' failures='2' errors='0' time='0.009' package='' id='3'>
    <properties />
    <system-out />
    <system-err /></testsuite>
  <testsuite name='test' timestamp='2024-06-27T20:04:10.482Z' hostname='localhost' tests='2' failures='2' errors='0' time='0.009' package='' id='4'>
    <properties />
    <testcase name='[0] a (test)' classname='com.repro.GoodTest' time='0.008'>
      <failure message='expected:&lt;1&gt; but was:&lt;2&gt;' type='java.lang.AssertionError'>java.lang.AssertionError: expected:&lt;1&gt; but was:&lt;2&gt;
	at org.junit.Assert.fail(Assert.java:89)
	at org.junit.Assert.failNotEquals(Assert.java:835)
	at org.junit.Assert.assertEquals(Assert.java:647)
	at org.junit.Assert.assertEquals(Assert.java:633)
	at com.repro.BadTest.test(BadTest.java:17)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
	at junitparams.internal.InvokeParameterisedMethod.evaluate(InvokeParameterisedMethod.java:234)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at junitparams.internal.ParameterisedTestMethodRunner.runMethodInvoker(ParameterisedTestMethodRunner.java:47)
	at junitparams.internal.ParameterisedTestMethodRunner.runTestMethod(ParameterisedTestMethodRunner.java:40)
	at junitparams.internal.ParameterisedTestClassRunner.runParameterisedTest(ParameterisedTestClassRunner.java:146)
	at junitparams.JUnitParamsRunner.runChild(JUnitParamsRunner.java:417)
	at junitparams.JUnitParamsRunner.runChild(JUnitParamsRunner.java:386)
	at org.junit.runners.ParentRunner$4.run(ParentRunner.java:331)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:79)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:329)
	at org.junit.runners.ParentRunner.access$100(ParentRunner.java:66)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:293)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:413)
	at org.junit.runners.Suite.runChild(Suite.java:128)
	at org.junit.runners.Suite.runChild(Suite.java:27)
	at org.junit.runners.ParentRunner$4.run(ParentRunner.java:331)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:79)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:329)
	at org.junit.runners.ParentRunner.access$100(ParentRunner.java:66)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:293)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:413)
	at com.google.testing.junit.runner.internal.junit4.CancellableRequestFactory$CancellableRunner.run(CancellableRequestFactory.java:108)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:115)
	at com.google.testing.junit.runner.junit4.JUnit4Runner.run(JUnit4Runner.java:116)
	at com.google.testing.junit.runner.BazelTestRunner.runTestsInSuite(BazelTestRunner.java:145)
	at com.google.testing.junit.runner.BazelTestRunner.main(BazelTestRunner.java:76)
</failure></testcase>
    <testcase name='[1] b (test)' classname='com.repro.GoodTest' time='0.0'>
      <failure message='expected:&lt;1&gt; but was:&lt;2&gt;' type='java.lang.AssertionError'>java.lang.AssertionError: expected:&lt;1&gt; but was:&lt;2&gt;
	at org.junit.Assert.fail(Assert.java:89)
	at org.junit.Assert.failNotEquals(Assert.java:835)
	at org.junit.Assert.assertEquals(Assert.java:647)
	at org.junit.Assert.assertEquals(Assert.java:633)
	at com.repro.BadTest.test(BadTest.java:17)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
	at junitparams.internal.InvokeParameterisedMethod.evaluate(InvokeParameterisedMethod.java:234)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at junitparams.internal.ParameterisedTestMethodRunner.runMethodInvoker(ParameterisedTestMethodRunner.java:47)
	at junitparams.internal.ParameterisedTestMethodRunner.runTestMethod(ParameterisedTestMethodRunner.java:40)
	at junitparams.internal.ParameterisedTestClassRunner.runParameterisedTest(ParameterisedTestClassRunner.java:146)
	at junitparams.JUnitParamsRunner.runChild(JUnitParamsRunner.java:417)
	at junitparams.JUnitParamsRunner.runChild(JUnitParamsRunner.java:386)
	at org.junit.runners.ParentRunner$4.run(ParentRunner.java:331)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:79)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:329)
	at org.junit.runners.ParentRunner.access$100(ParentRunner.java:66)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:293)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:413)
	at org.junit.runners.Suite.runChild(Suite.java:128)
	at org.junit.runners.Suite.runChild(Suite.java:27)
	at org.junit.runners.ParentRunner$4.run(ParentRunner.java:331)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:79)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:329)
	at org.junit.runners.ParentRunner.access$100(ParentRunner.java:66)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:293)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:413)
	at com.google.testing.junit.runner.internal.junit4.CancellableRequestFactory$CancellableRunner.run(CancellableRequestFactory.java:108)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:115)
	at com.google.testing.junit.runner.junit4.JUnit4Runner.run(JUnit4Runner.java:116)
	at com.google.testing.junit.runner.BazelTestRunner.runTestsInSuite(BazelTestRunner.java:145)
	at com.google.testing.junit.runner.BazelTestRunner.main(BazelTestRunner.java:76)
</failure></testcase>
    <system-out />
    <system-err /></testsuite></testsuites>
```

However, the test.log correctly states that BadTest.java is the test that failed:

```
$ cat bazel-testlogs/repro/repro_test/test.log
exec ${PAGER:-/usr/bin/less} "$0" || exit 1
Executing tests from //repro:repro_test
-----------------------------------------------------------------------------
JUnit4 Test Runner
.E.E..
Time: 0.02
There were 2 failures:
1) [0] a (test)(com.repro.BadTest)
java.lang.AssertionError: expected:<1> but was:<2>
	at org.junit.Assert.fail(Assert.java:89)
	at org.junit.Assert.failNotEquals(Assert.java:835)
	at org.junit.Assert.assertEquals(Assert.java:647)
	at org.junit.Assert.assertEquals(Assert.java:633)
	at com.repro.BadTest.test(BadTest.java:17)
2) [1] b (test)(com.repro.BadTest)
java.lang.AssertionError: expected:<1> but was:<2>
	at org.junit.Assert.fail(Assert.java:89)
	at org.junit.Assert.failNotEquals(Assert.java:835)
	at org.junit.Assert.assertEquals(Assert.java:647)
	at org.junit.Assert.assertEquals(Assert.java:633)
	at com.repro.BadTest.test(BadTest.java:17)

FAILURES!!!
Tests run: 4,  Failures: 2


BazelTestRunner exiting with a return value of 1
JVM shutdown hooks (if any) will run now.
The JVM will exit once they complete.

-- JVM shutdown starting at 2024-06-27 20:04:10 --
```

After renaming the test in BadTest.java to `test2`, the test summary is correct:

```
$ bazel test repro:repro_test
INFO: Analyzed target //repro:repro_test (0 packages loaded, 0 targets configured).
INFO: From Building repro/repro_test.jar (4 source files):
warning: [options] source value 8 is obsolete and will be removed in a future release
warning: [options] target value 8 is obsolete and will be removed in a future release
warning: [options] To suppress warnings about obsolete options, use -Xlint:-options.
FAIL: //repro:repro_test (see /home/user/.cache/bazel/_bazel_cjk/1249c2b3e8a01f2e196b44c56398532f/execroot/_main/bazel-out/k8-fastbuild/testlogs/repro/repro_test/test.log)
INFO: Found 1 test target...
Target //repro:repro_test up-to-date:
  bazel-bin/repro/repro_test
  bazel-bin/repro/repro_test.jar
INFO: Elapsed time: 0.701s, Critical Path: 0.60s
INFO: 3 processes: 1 internal, 1 processwrapper-sandbox, 1 worker.
INFO: Build completed, 1 test FAILED, 3 total actions
//repro:repro_test                                                       FAILED in 0.4s
    PASSED  com.repro.GoodTest.[0] a (test) (0.0s)
    PASSED  com.repro.GoodTest.[1] b (test) (0.0s)
    FAILED  com.repro.BadTest.[0] a (test2) (0.0s)
    FAILED  com.repro.BadTest.[1] b (test2) (0.0s)
Test cases: finished with 2 passing and 2 failing out of 4 test cases

Executed 1 out of 1 test: 1 fails locally.
```
