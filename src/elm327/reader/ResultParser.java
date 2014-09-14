package elm327.reader;

import com.google.common.base.Strings;

public class ResultParser {

    public static <T> Result<T> parse(String response, Command<T> command) {

        String[] fragments = split(response);

        switch (fragments.length) {
            case 0:
                return new Result.Error<T>(new EmptyResultException());
            case 1:
                return new Result.Response<T>(command, fragments[0]);
            case 2:
                if (command.matches(fragments[0])) {
                    return new Result.Response<T>(command, fragments[1]);
                }
                return new Result.Error<T>(new BadCommandResultException());
            default:
                return new Result.Error<T>(new TooMuchResultDataException());
        }
    }

    private static String[] split(String response) {

        if (Strings.isNullOrEmpty(response)) {
            return new String[0];
        }

        response = response.trim();

        if (Strings.isNullOrEmpty(response)) {
            return new String[0];
        }

        return response.split("\n+");
    }

    @SuppressWarnings("serial")
    public static class EmptyResultException extends Exception {

    }

    @SuppressWarnings("serial")
    public static class BadCommandResultException extends Exception {

    }

    @SuppressWarnings("serial")
    public static class TooMuchResultDataException extends Exception {

    }
}
