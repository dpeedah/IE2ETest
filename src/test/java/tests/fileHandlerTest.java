package tests;
import helperpackage.Car;
import helperpackage.TestValueExtractor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fileHandlerTest {

    @Test
    public void testGetInputs(){
        List<String> inputs = new ArrayList<String>();
        try {
           inputs = TestValueExtractor.getRegInputValuesCustom();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int inputSize = inputs.size();
        Assertions.assertTrue(inputSize > 0);
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
        Assertions.assertTrue(inputSize > 0);
    }
}
