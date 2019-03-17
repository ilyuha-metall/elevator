package test.elevator.config;

import test.elevator.exceptions.WrongConfigurationException;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropertiesConfiguration implements Configuration{
    private final ResourceBundle properties;

    public PropertiesConfiguration(ResourceBundle properties) {
        this.properties = properties;
    }

    @Override
    public int getIntParam(String param) {
        try {
            return Integer.parseInt(properties.getString(param));
        }catch (NumberFormatException ex){
            throw new WrongConfigurationException(String.format("Illegal type of param: %s. expects int.", param),ex);
        }catch (MissingResourceException ex){
            throw new WrongConfigurationException(String.format("Param %s not found.", param),ex);
        }
    }
}
