import com.conversor.models.*;
import com.conversor.models.utils.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppHandler extends Menu {
    private final Scanner userInput = new Scanner(System.in);
    private final Validator validate = new Validator();
    private UserHistory userHistory = new UserHistory();
    private final ConsultAPI consulter = new ConsultAPI();
    private int option = 0;
    private int apiCalls = 0;
    private String continueMessage = "";

    public void firstOption() {
        while (this.getOption() == 1) {
            CurrencyPairConversion currencyPairConversion = new CurrencyPairConversion();
            System.out.println("******************************************************************************************");
            
            List<Country> countries = this.getInitalCountryList();
            Scanner input = this.getInput();
            this.showCountries(countries);
            
            validate.setMaxIndex(countries.size());
            validate.setMinIndex(1);
            System.out.println("Selecciona el tipo de moneda que posee, para ser combertida (Por ej. 1): ");
            
            validate.setBaseIndex(input.nextInt());
            int baseIndex = validate.validateInputMintoMax(input);
            
            Country userCountryBase = countries.get(baseIndex - 1);
            currencyPairConversion.setBaseCode(userCountryBase.getCode());
            
            System.out.println("Selecciona el tipo de moneda, para entregar el valor cambiario: ");
           
            validate.setTargetIndex(input.nextInt());
            int targetIndex = validate.validateInputMintoMax(input);
            targetIndex = validate.validateBaseTargetIndexNotEquals(input);
            Country userCountryTarget = countries.get(targetIndex - 1);
            currencyPairConversion.setTargetCode(userCountryTarget.getCode());
            
            System.out.printf("Ingresa el valor que deseas cambiar de '%s' a '%s': %n", currencyPairConversion.getBaseCode(), currencyPairConversion.getTargetCode());
            double userAmount = input.nextDouble();
            currencyPairConversion.setAmount(userAmount);
            
            consulter.exchangeCurrencyPair(currencyPairConversion);
           
            userHistory.setUserHistoryList(currencyPairConversion);
            
            System.out.println("******************************************************************************************");
            System.out.println(currencyPairConversion);
            
            setContinueMessage("¿Deseas realizar otra cambio de moneda?");
            continueOptionWithTwoOptions(this.getContinueMessage(), this.getOption());
        }
    }

    public void secondOption() {
        while (this.getOption() == 2) {
            System.out.println("******************************************************************************************");
            List<Country> countries = this.getInitalCountryList();
            this.showCountries(countries);
            continueOptionToMenu();
        }
    }

    public void thirdOption() {
        while (this.getOption() == 3) {
            System.out.println("******************************************************************************************");
            int count = this.getApiCalls();
            Scanner userInput = this.getInput();
            List<Country> localCountries = this.getInitalCountryList();
            
            if (count == 0) {
                List<Country> apiValidCountries = consulter.consultCountries();
                this.setApiCountries(apiValidCountries);
                count++;
                this.setApiCalls(count);
            }
            List<Country> apiContries = this.getApiCountries();
            this.showCountries(apiContries);
            System.out.println("Selecciona el número del país que desees añadir (Por ej. 15): ");
            
            int userIndex = userInput.nextInt();
            Country userCountry = apiContries.get(userIndex - 1);
            userCountry = validate.validateAddItemtoList(userCountry, localCountries, apiContries, userInput);
           
            System.out.println("Se ha añadido tu país a la lista...");
            this.setInitalCountryList(userCountry);
            this.showCountries(localCountries);
           
            setContinueMessage("¿Deseas añadir otro país?");
            continueOptionWithTwoOptions(this.getContinueMessage(), this.getOption());
        }
    }

    public void fourthOption() {
        while (this.getOption() == 4) {
            System.out.println("******************************************************************************************");
            List<Country> countries = this.getInitalCountryList();
            boolean validateOperation = validate.validateArraySize(countries);
            if (validateOperation) {
                
                validate.setMaxIndex(countries.size());
                validate.setMinIndex(1);
                Scanner userInput = this.getInput();
                this.showCountries(countries);
                System.out.println("Selecciona el pais que deseas quitar de la lista (por ej. 5): ");
                // validaciones
                validate.setBaseIndex(userInput.nextInt());
                int index = validate.validateInputMintoMax(userInput);
                index = index - 1;
                String countryCode = countries.get(index).getCode();
                countries = this.removeCountryFormList(index);
                System.out.printf("Se ha eliminado %s de la lista...\n", countryCode);
                String continueMessage = "¿Deseas remover otro país de la lista?";
                continueOptionWithTwoOptions(continueMessage, this.getOption());
            } else {
                continueOptionToMenu();
            }
        }
    }

    public void fiveOption() {
        while (this.getOption() == 5) {
            System.out.println("******************************************************************************************");
            List<CurrencyPairConversion> userList = this.userHistory.getUserConversionList();
            this.showUserHistory(userList);
            continueOptionToMenu();
        }
    }

    private void continueOptionWithTwoOptions(String message, int currentOption) {
        System.out.println("******************************************************************************************");
        System.out.printf("%s 'S' para continuar o 'N' para regresar al menu inicial: ", message);
        validate.setDecision(userInput.next());
        setOption(validate.validateUserDecision(userInput, currentOption));
    }

    private void continueOptionToMenu() {
        System.out.println("******************************************************************************************");
        System.out.println("Ingresa cualquier número o letra para regresar al menú...");
        userInput.next();
        setOption(0);
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public Scanner getInput() {
        return userInput;
    }

    public int getApiCalls() {
        return apiCalls;
    }

    public void setApiCalls(int apiCalls) {
        this.apiCalls = apiCalls;
    }

    public String getContinueMessage() {
        return continueMessage;
    }

    public void setContinueMessage(String continueMessage) {
        this.continueMessage = continueMessage;
    }
}
