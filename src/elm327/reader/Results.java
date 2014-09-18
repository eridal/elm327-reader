package elm327.reader;

public class Results {

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