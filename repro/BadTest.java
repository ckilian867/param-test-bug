package com.repro;

import static org.junit.Assert.assertEquals;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public final class BadTest {

    @Test
    @Parameters(method = "createInputs")
    public void test(String input) {

        assertEquals(input.length(), 2);
    }

    private String[] createInputs() {
        return new String[]{"a", "b"};
    }
}
