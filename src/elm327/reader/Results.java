package elm327.reader;

public class Results {

    public static <T> Result<T> parse(String response, Command<T> command) {
        try {
            String data = extractData(response, command);

            if ("?".equals(data)) {
                return new Results.Unknown<T>(command);
            }

            T value = command.parse(data);
            return new Results.Answer<T>(command, value);

        } catch(Exception e) {
            return new Results.Error<T>(command, e);
        }
    }

    private static <T> String extractData(String response, Command<T> command) throws Exception {
        String[] fragments = split(response);
        if (fragments.length == 1) {
            return fragments[0];
        }
        if (fragments.length == 2) {
            String message = command.message().code;
            if (message.equals(fragments[0])) {
                return fragments[1];
            }
            throw new BadCommandResultException();
        }
        throw new TooMuchResultDataException();
    }

    private static String[] split(String response) throws Exception {

        if (null == response || response.isEmpty()) {
            throw new Exception();
        }

        response = response.trim();

        if (null == response || response.isEmpty()) {
            throw new Exception();
        }

        return response.split("\n+");
    }

    @SuppressWarnings("serial")
    public static class BadCommandResultException extends Exception {

    }

    @SuppressWarnings("serial")
    public static class TooMuchResultDataException extends Exception {

    }

    private static abstract class ResultBase<T> implements Result<T> {

        private final Command<T> command;

        ResultBase(Command<T> command) {
            this.command = command;
        }

        @Override public Command<T> command() {
            return command;
        }
    }

    static class Answer<T> extends ResultBase<T> {
        private final T data;

        Answer(Command<T> command, T data) {
            super(command);
            this.data = data;
        }

        @Override public T data() {
            return data;
        }
    }

    static class Unknown<T> extends ResultBase<T> {

        Unknown(Command<T> command){
            super(command);
        }

        @Override public T data() {
            return null;
        }
    };

    static class Error<T> extends ResultBase<T> {
        public final Exception error;

        public Error(Command<T> command, Exception error) {
            super(command);
            this.error = error;
        }

        @Override public T data() {
            return null;
        }

        public Exception getError() {
            return error;
        }
    }
}
