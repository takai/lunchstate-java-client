package com.herokuapp.lunchstate.client;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.herokuapp.lunchstate.client.exception.LunchstateRuntimeException;
import com.herokuapp.lunchstate.client.model.State;
import com.herokuapp.lunchstate.client.model.StateContainer;
import com.herokuapp.lunchstate.client.service.GoogleAuthorizationService;
import com.herokuapp.lunchstate.client.service.LunchstateService;

public class LunchstateClient {

    private static final String USAGE = "lunchstate {create|read|update|delete} -u user -k key [-v value]";

    public static void main(String[] args) {
        Options options = null;
        try {
            options = buildOptions();

            CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption('h')) {
                printHelpAndExit(options);
            }

            String user = cmd.getOptionValue('u');
            String key = cmd.getOptionValue('k');
            String value = cmd.getOptionValue('v');
            String mode = null;

            String[] arguments = cmd.getArgs();
            if (arguments.length == 1) {
                mode = arguments[0];
            } else {
                printHelpAndExit(options);
            }

            LunchstateClient client = new LunchstateClient();
            if ("create".equals(mode)) {
                client.create(user, key, parserValue(value));
            } else if ("read".equals(mode)) {
                client.read(user, key);
            } else if ("update".equals(mode)) {
                client.update(user, key, parserValue(value));
            } else if ("delete".equals(mode)) {
                client.delete(user, key);
            } else {
                printHelpAndExit(options);
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());

            printHelpAndExit(options);
        } catch (LunchstateRuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(2);
        }
    }

    public void delete(String user, String key) {
        String token = loadOrRequestAccessToken();
        lunchstateService.deleteState(user, key, token);
    }

    public void update(String user, String key, Boolean value) {
        String token = loadOrRequestAccessToken();
        lunchstateService.updateState(user, key, value, token);
    }

    public void read(String user, String key) {
        StateContainer container = lunchstateService.readState(user, key);
        List<State> states = container.getStates();
        if (!states.isEmpty()) {
            State state = states.get(0);
            System.out.println(state.getValue());
        } else {
            System.err.println("State is not found");
        }
    }

    private static boolean parserValue(String passedValue) throws ParseException {
        if ("true".equals(passedValue)) {
            return true;
        } else if ("false".equals(passedValue)) {
            return false;
        } else {
            throw new ParseException("value must be set to 'true' or 'false'.");
        }
    }

    public void create(String user, String key, boolean value) {
        String token = loadOrRequestAccessToken();
        lunchstateService.createState(user, key, value, token);
    }

    @SuppressWarnings("static-access")
    private static Options buildOptions() {
        Option user = OptionBuilder.isRequired()
                                   .withArgName("user")
                                   .hasArg(true)
                                   .withDescription("Target user to access state")
                                   .withLongOpt("user")
                                   .create('u');
        Option key = OptionBuilder.isRequired()
                                  .withArgName("key")
                                  .hasArg(true)
                                  .withDescription("Target key")
                                  .withLongOpt("key")
                                  .create('k');
        Option value = OptionBuilder.withArgName("value")
                                    .hasArg(true)
                                    .withDescription("Target value")
                                    .withLongOpt("value")
                                    .create('v');
        Option help = OptionBuilder.withDescription("Print this help").withLongOpt("help").create('h');

        Options options = new Options();

        options.addOption(user);
        options.addOption(key);
        options.addOption(value);
        options.addOption(help);

        return options;
    }

    private static void printHelpAndExit(Options options) {
        new HelpFormatter().printHelp(USAGE, options);
        System.exit(1);
    }

    private final GoogleAuthorizationService authorizationService;

    private final LunchstateService lunchstateService;

    public LunchstateClient() {
        Injector injector = Guice.createInjector(new LunchstateClientModule());

        authorizationService = injector.getInstance(GoogleAuthorizationService.class);
        lunchstateService = injector.getInstance(LunchstateService.class);
    }

    private String loadOrRequestAccessToken() {
        try {
            String token = authorizationService.loadAccessToken();
            if (token == null) {
                token = requestAccessToken();
            }
            return token;
        } catch (IOException e) {
            throw new LunchstateRuntimeException(e);
        } catch (URISyntaxException e) {
            throw new LunchstateRuntimeException(e);
        }
    }

    private String requestAccessToken() throws IOException, URISyntaxException {
        openLoginPage();
        String code = promptCode();

        return authorizationService.requestAccessToken(code);
    }

    private void openLoginPage() throws IOException, URISyntaxException {
        String url = authorizationService.getAuthorizationUrl();
        Desktop.getDesktop().browse(new URI(url));
    }

    private String promptCode() throws IOException {
        System.out.print("Please input code: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        return reader.readLine().trim();
    }

}
