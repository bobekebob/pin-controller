package sk.msquared.rpi;

import com.pi4j.io.gpio.*;

/**
 * Basic Raspberry PIN handling. Main method accepts 2 input parameters:
 *              PIN ID numeric (0-32)
 *              operation numeric (0 or 1), 0 = LOW, 1 = HIGH
 * @author martin.valach
 */
public class PinController {

    private static final String GPIO = "GPIO ";
    private static final int MIN_PIN_ID = 0;
    private static final int MAX_PIN_ID = 32;
    private static final int MIN_OPERATION_ID = 0;
    private static final int MAX_OPERATION_ID = 1;

    public static void main(String[] args) {

        // number of input parameters validation
        if (args == null || args.length != 2) {
            logBadInputErrorAndExit();
        }

        Integer pinID = null;
        Integer operation = null;

        // input parameters conversion
        try {
            pinID = Integer.valueOf(args[0]);
            operation = Integer.valueOf(args[1]);
        } catch (Exception e) {
            logBadInputErrorAndExit();
        }

        // range validation
        if (pinID == null || !(pinID >= MIN_PIN_ID && pinID <= MAX_PIN_ID)) {
            logBadInputErrorAndExit();
        }

        if (operation == null || !(operation >= MIN_OPERATION_ID && operation <= MAX_OPERATION_ID)) {
            logBadInputErrorAndExit();
        }

        PinState pinState = getPinStateByOperation(operation);
        Pin pin = RaspiPin.getPinByName(GPIO + pinID);

        if (pinState == null || pin == null) {
            logBadInputErrorAndExit();
        }

        // GPIO controller
        final GpioController gpio = GpioFactory.getInstance();

        // execute pin operation
        gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(GPIO + pinID), "PinLED", getPinStateByOperation(operation));

        System.out.println("PIN  + " + pin.toString() + " was set to " + pinState.toString());

        System.exit(0);
    }

    private static void logBadInputErrorAndExit() {
        System.out.print("Bad Input! Enter 2 input parameters (numeric): PI ID (0-32) and operation (0,1)");
        System.exit(1);
    }

    private static PinState getPinStateByOperation(Integer state) {
        return state==1?PinState.HIGH:PinState.LOW;
    }

}
