package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestValueExtractor {

    // the regex used to find number plates is ([A-Z]{2}[0-9]{2}\s?[A-Z]{3}) , using s? to allow for spaces
    // follows the required format from https://www.thecarexpert.co.uk/how-does-the-uk-number-plate-system-work/

    public static List<String> getInputValues() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/car_input.txt"));
        String nLine = "";
        List<String> validNumberPlates = new ArrayList<String>();
        while ( (nLine = reader.readLine()) != null) {
            Matcher matcher = Pattern.compile("([A-Z]{2}[0-9]{2}\\s?[A-Z]{3})").matcher(nLine);
            while (matcher.find()){
                validNumberPlates.add(matcher.group());
            }
        }
        return validNumberPlates;
    }

    public static List<Car> getOutputValues() throws IOException {
        List<Car> cars = new ArrayList<Car>();
        String delimiter = ",";
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/car_output.txt"));
        String nLine = "";
        while ( (nLine = reader.readLine()) != null){
            String[] possibleCar = nLine.split(delimiter);
            String possiblePlate = possibleCar[0];

            if (possiblePlate.matches("([A-Z]{2}[0-9]{2}\\s?[A-Z]{3})")){
                int possibleYear = 0;
                try{
                    possibleYear = Integer.parseInt(possibleCar[4]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Car newCar = new Car(possibleCar[0],possibleCar[1],possibleCar[2],possibleCar[3],possibleYear);
                cars.add(newCar);
            }
        }
        return cars;
    }
}
