package tests;
import helpers.Car;
import helpers.TestValueExtractor;
import org.junit.After;
import org.junit.Before;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Scanner;
import java.util.logging.Logger;

public class fileHandlerTest {

    @Test
    public void testGetInputs(){
        List<String> inputs = new ArrayList<String>();
        try {
           inputs = TestValueExtractor.getInputValuesBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int inputSize = inputs.size();
        Assert.assertTrue(inputSize > 0);
    }

    @Test
    public void testGetOutputs(){
        List<Car> cars = new ArrayList<Car>();
        try {
            cars = TestValueExtractor.getOutputValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int inputSize = cars.size();
        Assert.assertTrue(inputSize > 0);
    }
}
